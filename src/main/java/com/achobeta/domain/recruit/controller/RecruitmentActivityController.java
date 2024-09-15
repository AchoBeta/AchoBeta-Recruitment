package com.achobeta.domain.recruit.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.recruit.model.condition.StudentGroup;
import com.achobeta.domain.recruit.model.converter.RecruitmentActivityConverter;
import com.achobeta.domain.recruit.model.dto.ActivityPaperDTO;
import com.achobeta.domain.recruit.model.dto.RecruitmentActivityDTO;
import com.achobeta.domain.recruit.model.dto.RecruitmentActivityUpdateDTO;
import com.achobeta.domain.recruit.model.dto.TimePeriodDTO;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.model.vo.RecruitmentActivityVO;
import com.achobeta.domain.recruit.model.vo.RecruitmentTemplate;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.recruit.service.RecruitmentBatchService;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 19:27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit/activity")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class RecruitmentActivityController {

    private final QuestionPaperService questionPaperService;

    private final TimePeriodService timePeriodService;

    private final RecruitmentBatchService recruitmentBatchService;

    private final RecruitmentActivityService recruitmentActivityService;

    @PostMapping("/create")
    public SystemJsonResponse createRecruitmentActivity(@RequestBody RecruitmentActivityDTO recruitmentActivityDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentActivityDTO);
        StudentGroup target = recruitmentActivityDTO.getTarget();
        Long batchId = recruitmentActivityDTO.getBatchId();
        recruitmentBatchService.checkAndGetRecruitmentBatchIsRun(batchId, Boolean.TRUE);
        // 创建
        String title = recruitmentActivityDTO.getTitle();
        String description = recruitmentActivityDTO.getDescription();
        Date deadline = new Date(recruitmentActivityDTO.getDeadline());
        Long actId = recruitmentActivityService.createRecruitmentActivity(batchId, target, title, description, deadline);
        return SystemJsonResponse.SYSTEM_SUCCESS(actId);
    }

    @GetMapping("/shift/{actId}")
    public SystemJsonResponse shiftRecruitment(@PathVariable("actId") @NotNull Long actId,
                                               @RequestParam(name = "isRun") @NotNull Boolean isRun) {
        // 检测
        recruitmentActivityService.checkRecruitmentActivityExists(actId);
        // 修改
        recruitmentActivityService.shiftRecruitmentActivity(actId, isRun);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/update")
    public SystemJsonResponse updateRecruitmentActivity(@RequestBody RecruitmentActivityUpdateDTO recruitmentActivityUpdateDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentActivityUpdateDTO);
        StudentGroup target = recruitmentActivityUpdateDTO.getTarget();
        Long actId = recruitmentActivityUpdateDTO.getActId();
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE);
        //更新
        String title = recruitmentActivityUpdateDTO.getTitle();
        String description = recruitmentActivityUpdateDTO.getDescription();
        Date deadline = new Date(recruitmentActivityUpdateDTO.getDeadline());
        recruitmentActivityService.updateRecruitmentActivity(actId, target, title, description, deadline);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/set/paper")
    public SystemJsonResponse setRecruitmentQuestionPaper(@RequestBody ActivityPaperDTO activityPaperDTO) {
        // 检查
        ValidatorUtils.validate(activityPaperDTO);
        Long actId = activityPaperDTO.getActId();
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE);
        Long paperId = activityPaperDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        // 设置
        recruitmentActivityService.setPaperForActivity(actId, paperId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/period/add")
    public SystemJsonResponse addTimePeriod(@RequestBody TimePeriodDTO timePeriodDTO) {
        // 校验
        ValidatorUtils.validate(timePeriodDTO);
        Long actId = timePeriodDTO.getActId();
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE);
        // 添加
        Long startTime = timePeriodDTO.getStartTime();
        Long endTime = timePeriodDTO.getEndTime();
        timePeriodService.setPeriodForActivity(actId, startTime, endTime);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/period/remove/{periodId}")
    public SystemJsonResponse removeTimePeriod(@PathVariable("periodId") @NotNull Long periodId) {
        // 校验
        Long actId = timePeriodService.getActIdByPeriodId(periodId);
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE);
        // 删除
        timePeriodService.removeTimePeriod(periodId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/template/{actId}")
    @Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
    public SystemJsonResponse getCustomDefine(@PathVariable("actId") @NotNull Long actId) {
        RecruitmentActivity recruitmentActivity = recruitmentActivityService.checkAndGetRecruitmentActivity(actId);
        // 查询试卷
        List<QuestionVO> questionEntryVOS = recruitmentActivityService.getQuestionsByActId(actId);
        // 查询时间段
        List<TimePeriodVO> timePeriodVOS = timePeriodService.getTimePeriodsByActId(actId);
        // 构造招新活动的自定义模板
        RecruitmentTemplate recruitmentTemplate = RecruitmentTemplate.builder()
                .recruitmentActivityVO(RecruitmentActivityConverter.INSTANCE.recruitmentActivityToRecruitmentActivityVO(recruitmentActivity))
                .questionVOS(questionEntryVOS)
                .timePeriodVOS(timePeriodVOS)
                .build();
        // 当前用户的身份，
        UserHelper currentUser = BaseContext.getCurrentUser();
        if(UserTypeEnum.USER.getCode().equals(currentUser.getRole())) {
            // 对于普通用户，检查是否可以参与活动，并隐藏一些字段
            recruitmentActivityService.checkCanUserParticipateInActivity(currentUser.getUserId(), actId);
            recruitmentTemplate.hidden();
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentTemplate);
    }

    @GetMapping("/list/manager/{batchId}")
    public SystemJsonResponse getRecruitmentActivities(@PathVariable("batchId") @NotNull Long batchId,
                                                       @RequestParam(name = "isRun", required = false) Boolean isRun) {
        List<RecruitmentActivity> list = recruitmentActivityService.getRecruitmentActivities(batchId, isRun);
        List<RecruitmentActivityVO> activityVOS =
                RecruitmentActivityConverter.INSTANCE.recruitmentActivityListToRecruitmentActivityVOList(list);
        return SystemJsonResponse.SYSTEM_SUCCESS(activityVOS);
    }

    @GetMapping("/list/user/{batchId}")
    @Intercept(permit = {UserTypeEnum.USER})
    public SystemJsonResponse getRecruitmentActivities(@PathVariable("batchId") @NotNull Long batchId) {
        // 当前用户
        Long stuId = BaseContext.getCurrentUser().getUserId();
        List<RecruitmentActivityVO> activityVOS =
                recruitmentActivityService.getRecruitmentActivities(batchId, stuId)
                        .stream()
                        .map(recruitmentActivity -> {
                            RecruitmentActivityVO recruitmentActivityVO =
                                    RecruitmentActivityConverter.INSTANCE.recruitmentActivityToRecruitmentActivityVO(recruitmentActivity);
                            recruitmentActivityVO.hidden();
                            return recruitmentActivityVO;
                        }).toList();
        return SystemJsonResponse.SYSTEM_SUCCESS(activityVOS);
    }

}
