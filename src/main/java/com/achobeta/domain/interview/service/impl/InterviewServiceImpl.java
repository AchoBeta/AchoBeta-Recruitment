package com.achobeta.domain.interview.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.evaluate.model.entity.InterviewQuestionScore;
import com.achobeta.domain.interview.machine.constants.InterviewStateMachineConstants;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.model.converter.InterviewConverter;
import com.achobeta.domain.interview.model.dao.mapper.InterviewMapper;
import com.achobeta.domain.interview.model.dto.InterviewConditionDTO;
import com.achobeta.domain.interview.model.dto.InterviewCreateDTO;
import com.achobeta.domain.interview.model.dto.InterviewUpdateDTO;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.machine.StateMachineUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
        // 获取当前状态
        InterviewStatus fromState = currentInterview.getStatus();
        // 执行状态机
        InterviewStatus finalState = StateMachineUtil.fireEvent(
                InterviewStateMachineConstants.INTERVIEW_STATE_MACHINE_ID,
                fromState,
                interviewEvent,
                interviewContext
        );
        // 状态发生改变则进行更新
        if(!Objects.equals(fromState, finalState)) {
            switchInterview(currentInterview.getId(), finalState);
        }
        // 返回最终状态
        return finalState;
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
    public void checkInterviewStatus(Long interviewId, InterviewStatus interviewStatus) {
        checkAndGetInterviewExists(interviewId)
                .getStatus()
                .check(interviewStatus);
    }
}
