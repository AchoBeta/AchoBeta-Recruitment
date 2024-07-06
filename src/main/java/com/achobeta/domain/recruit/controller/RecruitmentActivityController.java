package com.achobeta.domain.recruit.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.recruit.model.dto.ActivityPaperDTO;
import com.achobeta.domain.recruit.model.dto.RecruitmentActivityDTO;
import com.achobeta.domain.recruit.model.dto.RecruitmentActivityUpdateDTO;
import com.achobeta.domain.recruit.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruit.model.entity.StudentGroup;
import com.achobeta.domain.recruit.model.vo.RecruitmentActivityVO;
import com.achobeta.domain.recruit.model.vo.RecruitmentTemplate;
import com.achobeta.domain.recruit.model.vo.TimePeriodVO;
import com.achobeta.domain.recruit.service.RecruitmentActivityService;
import com.achobeta.domain.recruit.service.RecruitmentBatchService;
import com.achobeta.domain.recruit.service.TimePeriodService;
import com.achobeta.domain.users.context.BaseContext;
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
public class RecruitmentActivityController {

    private final static Integer STUDENT_TYPE = 1; // 学生

    private final QuestionPaperService questionPaperService;

    private final TimePeriodService timePeriodService;

    private final RecruitmentBatchService recruitmentBatchService;

    private final RecruitmentActivityService recruitmentActivityService;

    @PostMapping("/create")
    public SystemJsonResponse createRecruitmentActivity(@RequestBody RecruitmentActivityDTO recruitmentActivityDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentActivityDTO);
        Long batchId = recruitmentActivityDTO.getBatchId();
        recruitmentBatchService.checkRecruitmentBatchExists(batchId);
        // 创建
        StudentGroup target = recruitmentActivityDTO.getTarget();
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

    @GetMapping("/update")
    public SystemJsonResponse updateRecruitmentActivity(@RequestBody RecruitmentActivityUpdateDTO recruitmentActivityUpdateDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentActivityUpdateDTO);
        Long actId = recruitmentActivityUpdateDTO.getActId();
        recruitmentActivityService.checkAndGetRecruitmentActivityIsRun(actId, Boolean.FALSE);
        //更新
        StudentGroup target = recruitmentActivityUpdateDTO.getTarget();
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

    @GetMapping("/template/{actId}")
    public SystemJsonResponse getCustomDefine(@PathVariable("actId") @NotNull Long actId) {
        RecruitmentActivity recruitmentActivity = recruitmentActivityService.checkAndGetRecruitmentActivity(actId);
        // 查询试卷
        List<QuestionVO> questionEntryVOS = recruitmentActivityService.getQuestionsByActId(actId);
        // 查询时间段
        List<TimePeriodVO> timePeriodVOS = timePeriodService.getTimePeriodsByActId(actId);

        // 构造招新活动的自定义模板
        RecruitmentTemplate recruitmentTemplate = RecruitmentTemplate.builder()
                .recruitmentActivityVO(BeanUtil.copyProperties(recruitmentActivity, RecruitmentActivityVO.class))
                .questionVOS(questionEntryVOS)
                .timePeriodVOS(timePeriodVOS)
                .build();
        // 当前用户的身份，
        Integer role = BaseContext.getCurrentUser().getRole();
        if(STUDENT_TYPE.equals(role)) {
            recruitmentTemplate.getRecruitmentActivityVO().shield();
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentTemplate);
    }

    @GetMapping("/list/{batchId}")
    public SystemJsonResponse getRecruitmentActivities(@PathVariable("batchId") @NotNull Long batchId,
                                                       @RequestParam(name = "isRun", required = false) Boolean isRun) {
        List<RecruitmentActivity> list = recruitmentActivityService.getRecruitmentActivities(batchId, isRun);
        List<RecruitmentActivityVO> activityVOS = BeanUtil.copyToList(list, RecruitmentActivityVO.class);
        // 当前用户的身份，
        Integer role = BaseContext.getCurrentUser().getRole();
        if(STUDENT_TYPE.equals(role)) {
            activityVOS.forEach(RecruitmentActivityVO::shield);
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(activityVOS);
    }

}
