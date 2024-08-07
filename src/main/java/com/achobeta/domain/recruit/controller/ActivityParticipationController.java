package com.achobeta.domain.recruit.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.recruit.model.dto.ActivityParticipationDTO;
import com.achobeta.domain.recruit.model.vo.ParticipationVO;
import com.achobeta.domain.recruit.service.ActivityParticipationService;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 20:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participate")
public class ActivityParticipationController {

    private final RecruitmentActivityService recruitmentActivityService;

    private final ActivityParticipationService activityParticipationService;

    /**
     * 用户参与活动获得草稿
     * @param actId
     * @return
     */
    @GetMapping("/get/{actId}")
    public SystemJsonResponse userGetParticipationSituation(@PathVariable("actId") @NotNull Long actId) {
        // 检测
        recruitmentActivityService.checkRecruitmentActivityExists(actId);
        // 当前用户
        Long stuId = BaseContext.getCurrentUser().getUserId();
        // 获取参与情况
        recruitmentActivityService.checkCanUserParticipateInActivity(stuId, actId);
        ParticipationVO participationUserVO = activityParticipationService.getActivityParticipation(stuId, actId);
        return SystemJsonResponse.SYSTEM_SUCCESS(participationUserVO);
    }

    /**
     * 用户提交试卷回答/时间段选择
     * @param activityParticipationDTO
     * @return
     */
    @PostMapping("/submit")
    public SystemJsonResponse userParticipateInActivity(@RequestBody ActivityParticipationDTO activityParticipationDTO) {
        // 检测
        ValidatorUtils.validate(activityParticipationDTO);
        // 当前用户
        Long stuId = BaseContext.getCurrentUser().getUserId();
        Long participationId = activityParticipationDTO.getParticipationId();
        activityParticipationService.checkActivityParticipationUser(stuId, participationId);
        Long actId = activityParticipationService.getActIdByParticipationId(participationId);
        recruitmentActivityService.checkCanUserParticipateInActivity(stuId, actId);
        // 参与
        activityParticipationService.participateInActivity(participationId,
                activityParticipationDTO.getQuestionAnswerDTOS(), activityParticipationDTO.getPeriodIds());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
