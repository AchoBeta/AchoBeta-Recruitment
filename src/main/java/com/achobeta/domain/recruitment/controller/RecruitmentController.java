package com.achobeta.domain.recruitment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.domain.recruitment.model.dto.RecruitmentDTO;
import com.achobeta.domain.recruitment.model.dto.RecruitmentPaperDTO;
import com.achobeta.domain.recruitment.model.entity.RecruitmentActivity;
import com.achobeta.domain.recruitment.model.vo.RecruitmentTemplate;
import com.achobeta.domain.recruitment.model.vo.RecruitmentVO;
import com.achobeta.domain.recruitment.model.vo.TimePeriodVO;
import com.achobeta.domain.recruitment.service.RecruitmentActivityService;
import com.achobeta.domain.recruitment.service.TimePeriodService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ValidatorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 11:42
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitmentController {

    private final RecruitmentActivityService recruitmentActivityService;

    private final TimePeriodService timePeriodService;

    private final QuestionPaperService questionPaperService;

    @PostMapping("/create")
    public SystemJsonResponse createRecruitment(@RequestBody RecruitmentDTO recruitmentDTO) {
        // 检测
        ValidatorUtils.validate(recruitmentDTO);
        Integer batch = recruitmentDTO.getBatch();
        if(batch.compareTo(0) <= 0) {
            throw new GlobalServiceException("ab版本非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
        // 调用服务创建一次招新活动
        Date deadline = new Date(recruitmentDTO.getDeadline());
        Long recruitmentId = recruitmentActivityService.createRecruitmentActivity(batch, deadline);
        log.info("管理员({}) 创建了一次招新，版本：{} {}，id：{}",
                BaseContext.getCurrentUser().getUserId(), batch, deadline, recruitmentId);
        // 返回招新id
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentId);
    }

    @GetMapping("/list")
    public SystemJsonResponse getRecruitments(@RequestParam(name = "isRun", required = false) Boolean isRun) {
        List<RecruitmentActivity> list = null;
        if(Objects.isNull(isRun)) {
            list = recruitmentActivityService.list();
        } else {
            list = recruitmentActivityService.lambdaQuery().eq(RecruitmentActivity::getIsRun, isRun).list();
        }
        List<RecruitmentVO> recruitmentVOS = BeanUtil.copyToList(list, RecruitmentVO.class);
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentVOS);
    }

    @GetMapping("/get/{recId}")
    public SystemJsonResponse getCustomDefine(@PathVariable("recId") @NonNull Long recId) {
        RecruitmentActivity recruitment = recruitmentActivityService.checkAndGetRecruitment(recId);
        // 查询自定义项
        List<QuestionEntryVO> questionEntryVOS = recruitmentActivityService.getPaperQuestions(recId);
        // 查询时间段
        List<TimePeriodVO> timePeriodVOS = timePeriodService.getTimePeriods(recId);
        // 构造招新活动的自定义问卷模板
        RecruitmentTemplate recruitmentTemplate = RecruitmentTemplate.builder()
                .recruitmentVO(BeanUtil.copyProperties(recruitment, RecruitmentVO.class))
                .questionEntryVOS(questionEntryVOS)
                .timePeriodVOS(timePeriodVOS)
                .build();
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentTemplate);
    }

    @GetMapping("/shift/{recId}")
    public SystemJsonResponse shiftRecruitment(@PathVariable("recId") @NonNull Long recId,
                                               @RequestParam(name = "isRun") @NonNull Boolean isRun) {
        // 检测
        recruitmentActivityService.checkRecruitmentExists(recId);
        // 修改
        recruitmentActivityService.shiftRecruitmentActivity(recId, isRun);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/set/paper")
    public SystemJsonResponse setRecruitmentQuestionPaper(@RequestBody RecruitmentPaperDTO recruitmentPaperDTO) {
        // 检查
        ValidatorUtils.validate(recruitmentPaperDTO);
        Long recId = recruitmentPaperDTO.getRecId();
        recruitmentActivityService.checkActivityNotRun(recId);
        Long paperId = recruitmentPaperDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        // 设置
        recruitmentActivityService.setRecruitmentQuestionPaper(recId, paperId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
