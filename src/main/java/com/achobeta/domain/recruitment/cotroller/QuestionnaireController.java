package com.achobeta.domain.recruitment.cotroller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.recruitment.model.dto.QuestionnaireEntryDTO;
import com.achobeta.domain.recruitment.model.dto.QuestionnairePeriodDTO;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireVO;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.achobeta.domain.recruitment.service.QuestionnairePeriodService;
import com.achobeta.domain.recruitment.service.QuestionnaireService;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 15:54
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questionnaire")
public class QuestionnaireController {

    private final RecruitmentService recruitmentService;

    private final QuestionnaireService questionnaireService;

    private final QuestionnaireEntryService questionnaireEntryService;

    private final QuestionnairePeriodService questionnairePeriodService;

    @GetMapping("/get/{recId}")
    public SystemJsonResponse createRecruitment(@PathVariable("recId") @NonNull Long recId) {
        // 检测
        recruitmentService.checkNotExists(recId);
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        // 尝试获取问卷
        QuestionnaireVO questionnaireVO = questionnaireService.getQuestionnaire(stuId, recId);
        // 返回问卷
        return SystemJsonResponse.SYSTEM_SUCCESS(questionnaireVO);
    }

    @PostMapping("/update/entry")
    public SystemJsonResponse updateEntry(@RequestBody QuestionnaireEntryDTO questionnaireEntryDTO) {
        // 检查
        ValidatorUtils.validate(questionnaireEntryDTO);
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        Long questionnaireId = questionnaireEntryDTO.getQuestionnaireId();
        // 检测是否可以对问卷进行操作
        questionnaireService.checkUser(stuId, questionnaireId);
        // 修改
        Long entryId = questionnaireEntryDTO.getEntryId();
        String content = questionnaireEntryDTO.getContent();
        questionnaireEntryService.updateQuestionnaireEntry(questionnaireId, entryId, content);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/add/period")
    public SystemJsonResponse addPeriod(@RequestBody QuestionnairePeriodDTO questionnairePeriodDTO) {
        // 检查
        ValidatorUtils.validate(questionnairePeriodDTO);
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        Long questionnaireId = questionnairePeriodDTO.getQuestionnaireId();
        // 检测是否可以对问卷进行操作
        questionnaireService.checkUser(stuId, questionnaireId);
        Long periodId = questionnairePeriodDTO.getPeriodId();
        // 添加
        questionnairePeriodService.addQuestionnairePeriod(questionnaireId, periodId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove/period")
    public SystemJsonResponse removePeriod(@RequestBody QuestionnairePeriodDTO questionnairePeriodDTO) {
        // 检查
        ValidatorUtils.validate(questionnairePeriodDTO);
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        Long questionnaireId = questionnairePeriodDTO.getQuestionnaireId();
        // 检测是否可以对问卷进行操作
        questionnaireService.checkUser(stuId, questionnaireId);
        Long periodId = questionnairePeriodDTO.getPeriodId();
        // 添加
        questionnairePeriodService.removeQuestionnairePeriod(questionnaireId, periodId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
