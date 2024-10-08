package com.achobeta.domain.schedule.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.interview.model.vo.InterviewVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.recruit.model.converter.TimePeriodConverter;
import com.achobeta.domain.recruit.model.dao.mapper.ActivityParticipationMapper;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.model.vo.QuestionAnswerVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodCountVO;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.domain.resource.enums.ExcelTemplateEnum;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.schedule.model.converter.SituationConverter;
import com.achobeta.domain.schedule.model.dao.mapper.InterviewScheduleMapper;
import com.achobeta.domain.schedule.model.entity.InterviewSchedule;
import com.achobeta.domain.schedule.model.entity.Interviewer;
import com.achobeta.domain.schedule.model.vo.*;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.schedule.service.InterviewerService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.achobeta.domain.schedule.constants.InterviewScheduleConstants.*;

/**
* @author 马拉圈
* @description 针对表【interview_schedule(面试预约表)】的数据库操作Service实现
* @createDate 2024-07-25 22:34:52
*/
@Service
@RequiredArgsConstructor
public class InterviewScheduleServiceImpl extends ServiceImpl<InterviewScheduleMapper, InterviewSchedule>
    implements InterviewScheduleService{

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    private final InterviewScheduleMapper interviewScheduleMapper;

    private final ActivityParticipationMapper activityParticipationMapper;

    private final ActivityParticipationService activityParticipationService;

    private final InterviewerService interviewerService;

    private final InterviewService interviewService;

    private final TimePeriodService timePeriodService;

    private final ResourceService resourceService;

    private void timePeriodValidate(Long startTime, Long endTime) {
        long gap = endTime - startTime;
        if(startTime.compareTo(System.currentTimeMillis()) < 0 ||
                gap > GAP_UNIT.toMillis(MAX_GAP) || gap < GAP_UNIT.toMillis(MIN_GAP)) {
            throw new GlobalServiceException("时间段非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
    }

    @Override
    public Optional<InterviewSchedule> getInterviewSchedule(Long scheduleId) {
        return this.lambdaQuery()
                .eq(InterviewSchedule::getId, scheduleId)
                .oneOpt();
    }

    @Override
    public List<ScheduleResumeVO> getInterviewScheduleList(Long managerId, Long actId) {
        return interviewScheduleMapper.getInterviewScheduleList(managerId, actId);
    }

    /**
     * 1. 获取活动的可选时间段，生成【periodId --> 时间段计数器】的 map
     * 2. 获取该活动的预约情况，生成【participationId --> 用户参与情况对象】的 map
     * 3. 根据活动的预约情况得到 participationIds，查询每个学生的时间段选择情况，通过 map 设置到 VO 对象里并进行时间段次数统计
     * 4. 构造返回值返回
     */
    @Override
    public UserSituationVO getSituationsByActId(Long actId) {
        // periodId --> 时间段计数器
        Map<Long, TimePeriodCountVO> countMap = timePeriodService.getTimePeriodsByActId(actId)
                .stream()
                .collect(Collectors.toMap(
                        TimePeriodVO::getId,
                        TimePeriodConverter.INSTANCE::timePeriodVOToCountVO,
                        (oldData, newData) -> newData)
                );
        // participationId --> 用户预约情况
        Map<Long, UserParticipationVO> userParticipationVOMap = interviewScheduleMapper.getSituationsByActId(actId)
                .stream()
                .collect(Collectors.toMap(
                        ParticipationScheduleVO::getParticipationId,
                        SituationConverter.INSTANCE::participationScheduleVOToUserParticipationVO,
                        (oldData, newData) -> newData
                ));
        // 查询时间段选择情况
        List<Long> participationIds = new ArrayList<>(userParticipationVOMap.keySet());
        activityParticipationService.getParticipationPeriods(participationIds).stream()
                .filter(participationPeriodVO -> userParticipationVOMap.containsKey(participationPeriodVO.getId()))
                .forEach(participationPeriodVO -> {
                    Long participationId = participationPeriodVO.getId();
                    List<TimePeriodVO> timePeriodVOS = participationPeriodVO.getTimePeriodVOS();
                    UserParticipationVO userParticipationVO = userParticipationVOMap.get(participationId);
                    userParticipationVO.setTimePeriodVOS(timePeriodVOS);
                    // 统计时间段选中次数（未预约的参与统计） ScheduleVOS 可能为空集合不可能为 null
                    if(userParticipationVO.getScheduleVOS().isEmpty()) {
                        timePeriodVOS.stream()
                                .map(TimePeriodVO::getId)
                                .filter(countMap::containsKey)
                                .forEach(periodId -> countMap.get(periodId).increment());
                    }
                });
        // 构造返回值
        List<UserParticipationVO> userParticipationVOS = userParticipationVOMap.values()
                .stream()
                .sorted(Comparator.comparingInt(up -> up.getTimePeriodVOS().size())) // 根据选择时间段排序
                .sorted(Comparator.comparingInt(up -> up.getScheduleVOS().size())) // 没被安排的会被排在前面
                .toList();
        List<TimePeriodCountVO> timePeriodCountVOS = countMap.values()
                .stream()
                .sorted((x1, x2) -> x2.getCount().compareTo(x1.getCount()))
                .toList();
        UserSituationVO userSituationVO = new UserSituationVO();
        userSituationVO.setUserParticipationVOS(userParticipationVOS);
        userSituationVO.setTimePeriodCountVOS(timePeriodCountVOS);
        return userSituationVO;
    }


    @Override
    public ScheduleDetailVO getInterviewScheduleDetail(Long scheduleId) {
        ScheduleDetailVO scheduleDetail = interviewScheduleMapper.getInterviewerScheduleDetail(scheduleId);
        // 查询引用这个面试预约的面试
        List<InterviewVO> interviewVOList = interviewService.getInterviewListByScheduleId(scheduleId);
        scheduleDetail.setInterviewVOList(interviewVOList);
        return scheduleDetail;
    }

    @Override
    public ParticipationDetailVO getDetailActivityParticipation(Long participationId) {
        return activityParticipationService.getActivityParticipation(participationId).map(activityParticipation -> {
            // 转化
            ParticipationDetailVO participationDetailVO =
                    SituationConverter.INSTANCE.activityParticipationToParticipationDetailVO(activityParticipation);
            // 获取用户回答的问题
            List<QuestionAnswerVO> questions = activityParticipationMapper.getQuestions(participationId);
            participationDetailVO.setQuestionAnswerVOS(questions);
            // 获取用户选择的时间段
            List<TimePeriodVO> periods = activityParticipationMapper.getPeriods(participationId);
            participationDetailVO.setTimePeriodVOS(periods);
            // 获取学生的简单简历信息
            ParticipationScheduleVO situations = interviewScheduleMapper.getSituationsByParticipationId(participationId);
            participationDetailVO.setSimpleStudentVO(situations.getSimpleStudentVO());
            participationDetailVO.setScheduleVOS(situations.getScheduleVOS());
            return participationDetailVO;
        }).orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.ACTIVITY_PARTICIPATION_NOT_EXISTS));
    }

    @Override
    public OnlineResourceVO printSituations(Long managerId, RecruitmentActivity activity, ResourceAccessLevel level, Boolean synchronous) {
        // 构造数据
        Map<Long, ActivitySituationExcelTemplate> resultMap = new LinkedHashMap<>();
        getSituationsByActId(activity.getId()).getUserParticipationVOS().forEach(situation -> {
            ActivitySituationExcelTemplate activitySituationExcelTemplate = new ActivitySituationExcelTemplate();
            activitySituationExcelTemplate.setTimePeriodVOS(situation.getTimePeriodVOS());
            activitySituationExcelTemplate.setScheduleVOS(situation.getScheduleVOS());
            resultMap.put(situation.getParticipationId(), activitySituationExcelTemplate);
        });
        // 获取详尽的问答与简历
        List<Long> participationIds = new ArrayList<>(resultMap.keySet());
        activityParticipationService.getParticipationQuestions(participationIds).stream()
                .filter(participationQuestionVO -> resultMap.containsKey(participationQuestionVO.getId()))
                .forEach(participationQuestionVO -> {
                    ActivitySituationExcelTemplate activitySituationExcelTemplate = resultMap.get(participationQuestionVO.getId());
                    SituationConverter.INSTANCE.stuSimpleResumeVOMapToSituationExcelTemplate(activitySituationExcelTemplate, participationQuestionVO.getStuSimpleResumeVO());
                    activitySituationExcelTemplate.setQuestionAnswerVOS(participationQuestionVO.getQuestionAnswerVOS());
                });
        // 获取表格
        return resourceService.uploadExcel(
                managerId,
                ExcelTemplateEnum.ACHOBETA_ACTIVITY_SITUATIONS,
                ActivitySituationExcelTemplate.class,
                new ArrayList<>(resultMap.values()),
                level,
                activity.getTitle(),
                synchronous
        );
    }

    @Override
    @Transactional
    public Long createInterviewSchedule(Long managerId, Long participationId, Long startTime, Long endTime) {
        // 校验时间段
        timePeriodValidate(startTime, endTime);
        // 构造对象
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        interviewSchedule.setParticipationId(participationId);
        interviewSchedule.setStartTime(new Date(startTime));
        interviewSchedule.setEndTime(new Date(endTime));
        // 落库
        this.save(interviewSchedule);
        Long scheduleId = interviewSchedule.getId();
        // 与管理员绑定
        interviewerService.createInterviewer(managerId, scheduleId);
        return scheduleId;
    }

    @Override
    @Transactional
    public void removeInterviewSchedule(Long scheduleId) {
        checkScheduleReferenced(scheduleId);
        this.lambdaUpdate()
                .eq(InterviewSchedule::getId, scheduleId)
                .remove();
        interviewerService.removeInterviewersByScheduleId(scheduleId);
    }

    @Override
    @Transactional
    public void exitInterview(Long managerId, Long scheduleId) {
        redisLock.tryLockDoSomething(SCHEDULE_EXIT_LOCK + scheduleId, () -> {
            Set<Long> managerIds = interviewerService.getInterviewersByScheduleId(scheduleId)
                    .stream()
                    .map(Interviewer::getManagerId)
                    .collect(Collectors.toSet());
            int size = managerIds.size();
            if(managerIds.contains(managerId)) {
                interviewerService.removeInterviewer(managerId, scheduleId);
                size--;
            }
            // 若面试预约没有管理员则删除
            if(0 == size) {
                this.removeInterviewSchedule(scheduleId);
            }
        }, () -> {}, simpleLockStrategy);
    }

    @Override
    public void updateInterviewSchedule(Long scheduleId, Long startTime, Long endTime) {
        // 校验时间段
        timePeriodValidate(startTime, endTime);
        // 更新
        this.lambdaUpdate()
                .eq(InterviewSchedule::getId, scheduleId)
                .set(InterviewSchedule::getStartTime, new Date(startTime))
                .set(InterviewSchedule::getEndTime, new Date(endTime))
                .update();
    }

    @Override
    public void checkInterviewScheduleExists(Long scheduleId) {
        getInterviewSchedule(scheduleId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_SCHEDULE_NOT_EXISTS));
    }

    @Override
    public void checkScheduleReferenced(Long scheduleId) {
        List<InterviewVO> interviewVOList = interviewService.getInterviewListByScheduleId(scheduleId);
        if(!CollectionUtils.isEmpty(interviewVOList)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_SCHEDULE_IS_REFERENCED);
        }
    }
}




