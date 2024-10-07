package com.achobeta.domain.interview.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.domain.interview.constants.InterviewConstants;
import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.constants.InterviewStateMachineConstants;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.model.converter.InterviewConverter;
import com.achobeta.domain.interview.model.dao.mapper.InterviewMapper;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewExcelTemplate;
import com.achobeta.domain.interview.model.vo.InterviewReserveVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.resource.enums.ExcelTemplateEnum;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.machine.StateMachineUtil;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.lark.oapi.service.vc.v1.model.ApplyReserveRespBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【interview(面试表)】的数据库操作Service实现
* @createDate 2024-08-05 23:45:13
*/
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview>
    implements InterviewService{

    private final InterviewMapper interviewMapper;

    private final InterviewerService interviewerService;

    private final PaperQuestionLinkService paperQuestionLinkService;

    private final ResourceService resourceService;

    private final FeishuService feishuService;

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    @Override
    public Optional<Interview> getInterview(Long interviewId) {
        return this.lambdaQuery()
                .eq(Interview::getId, interviewId)
                .oneOpt();
    }

    @Override
    public List<InterviewVO> getInterviewListByScheduleId(Long scheduleId) {
        List<Interview> interviewList = this.lambdaQuery()
                .eq(Interview::getScheduleId, scheduleId)
                .list();
        return InterviewConverter.INSTANCE.interviewListToInterviewVoList(interviewList);
    }

    @Override
    public List<InterviewVO> managerGetInterviewList(Long managerId, InterviewConditionDTO condition) {
        return interviewMapper.managerGetInterviewList(managerId, condition);
    }

    @Override
    public OnlineResourceVO printAllInterviewList(Long managerId, InterviewConditionDTO condition, ResourceAccessLevel level, Boolean synchronous) {
        List<InterviewVO> interviewVOList = managerGetInterviewList(null, condition);
        // 上传表格到对象存储服务器
        ExcelTemplateEnum achobetaInterviewAll = ExcelTemplateEnum.ACHOBETA_INTERVIEW_ALL;
        return resourceService.uploadExcel(
                managerId,
                achobetaInterviewAll,
                InterviewExcelTemplate.class,
                InterviewConverter.INSTANCE.interviewVOListToInterviewExcelTemplateList(interviewVOList),
                level,
                achobetaInterviewAll.getTitle(),
                synchronous);
    }

    @Override
    public List<InterviewVO> userGetInterviewList(Long userId, InterviewConditionDTO condition) {
        return interviewMapper.userGetInterviewList(userId, condition);
    }

    @Override
    public InterviewDetailVO getInterviewDetail(Long interviewId) {
        return interviewMapper.getInterviewDetail(interviewId);
    }

    @Override
    public Long getInterviewPaperId(Long interviewId) {
        return checkAndGetInterviewExists(interviewId).getPaperId();
    }

    @Override
    public InterviewReserveVO interviewReserveApply(Long interviewId, String mobile) {
        InterviewDetailVO interviewDetail = getInterviewDetail(interviewId);
        // 如果输入的手机号有效则是该手机号对应的 userId 作为此处的 ownerId，但 ownerId 不是同租户下的合法飞书用户，可能会在后续过程中报错
        String ownerId = feishuService.getUserIdByMobile(mobile);
        String title = interviewDetail.getTitle();
        Long endTime = interviewDetail.getScheduleVO().getEndTime().getTime();
        if(endTime.compareTo(System.currentTimeMillis()) < 0) {
            throw new GlobalServiceException("面试预约时间为过去时", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
        // 预约会议
        ApplyReserveRespBody reserveRespBody = feishuService.reserveApplyBriefly(ownerId, endTime, title);
        return InterviewConverter.INSTANCE.feishuReserveToInterviewReserveVO(reserveRespBody.getReserve());
    }

    @Override
    @Transactional
    public Long createInterview(InterviewCreateDTO interviewCreateDTO, Long managerId) {
        Interview interview = InterviewConverter.INSTANCE.interviewCreateDTOtoInterview(interviewCreateDTO);
        this.save(interview);
        // 管理员参与面试预约
        interviewerService.createInterviewer(managerId, interviewCreateDTO.getScheduleId());
        // 返回面试 id
        return interview.getId();
    }

    @Override
    public void updateInterview(InterviewUpdateDTO interviewUpdateDTO) {
        Interview interview = InterviewConverter.INSTANCE.interviewUpdateDTOtoInterview(interviewUpdateDTO);
        this.updateById(interview);
    }

    @Override
    public void switchInterview(Long interviewId, InterviewStatus interviewStatus) {
        Interview interview = new Interview();
        interview.setId(interviewId);
        interview.setStatus(interviewStatus);
        this.updateById(interview);
    }

    @Override
    @Transactional
    public InterviewStatus executeInterviewStateEvent(InterviewEvent interviewEvent, InterviewContext interviewContext) {
        Interview currentInterview = interviewContext.getInterview();
        Long interviewId = currentInterview.getId();
        return redisLock.tryLockGetSomething(InterviewConstants.EXECUTE_INTERVIEW_EVENT_LOCK + interviewId, () -> {
            // 获取当前状态
            InterviewStatus fromState = currentInterview.getStatus();
            // 执行状态机
            InterviewStatus finalState = StateMachineUtil.fireEvent(
                    InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID,
                    fromState,
                    interviewEvent,
                    interviewContext
            );
            // 能执行到这里，只有两种情况：1. 未命中、2. 轮转成功
            if(!Boolean.TRUE.equals(interviewContext.getHit())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_STATUS_TRANS_EVENT_ERROR);
            }
            // 更新状态
            switchInterview(interviewId, finalState);
            // 返回最终状态
            return finalState;
        }, () -> null, simpleLockStrategy);
    }

    @Override
    @Transactional
    public void setPaperForInterview(Long interviewId, Long paperId) {
        // 删除面试相关的打分
        Db.lambdaUpdate(InterviewQuestionScore.class)
                .eq(InterviewQuestionScore::getInterviewId, interviewId)
                .remove();
        // 拷贝一份试卷
        Long newPaperId = paperQuestionLinkService.cloneQuestionPaper(paperId);
        // 设置试卷
        this.lambdaUpdate()
                .eq(Interview::getId, interviewId)
                .set(Interview::getPaperId, newPaperId)
                .update();
    }

    @Override
    public void checkInterviewExists(Long interviewId) {
        getInterview(interviewId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_NOT_EXISTS));
    }

    @Override
    public Interview checkAndGetInterviewExists(Long interviewId) {
        return getInterview(interviewId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_NOT_EXISTS));
    }

    @Override
    public void checkInterviewStatus(Long interviewId, List<InterviewStatus> interviewStatus) {
        checkAndGetInterviewExists(interviewId)
                .getStatus()
                .check(interviewStatus);
    }
}
