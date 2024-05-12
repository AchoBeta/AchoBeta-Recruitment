package com.achobeta.domain.recruitment.cotroller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.recruitment.model.dto.QuestionnaireDTO;
import com.achobeta.domain.recruitment.model.dto.QuestionnaireEntryDTO;
import com.achobeta.domain.recruitment.model.dto.QuestionnairePeriodDTO;
import com.achobeta.domain.recruitment.model.vo.QuestionnaireVO;
import com.achobeta.domain.recruitment.service.QuestionnaireEntryService;
import com.achobeta.domain.recruitment.service.QuestionnairePeriodService;
import com.achobeta.domain.recruitment.service.QuestionnaireService;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.NonNull;
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

    @GetMapping("/get/{recId}")
    public SystemJsonResponse getQuestionnaire(@PathVariable("recId") @NonNull Long recId) {
        // 检测
        recruitmentService.checkNotExists(recId);
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        // 尝试获取问卷
        QuestionnaireVO questionnaireVO = questionnaireService.getQuestionnaire(stuId, recId);
        // 返回问卷
        return SystemJsonResponse.SYSTEM_SUCCESS(questionnaireVO);
    }

    @GetMapping("/list/{recId}")
    public SystemJsonResponse getQuestionnaires(@PathVariable("recId") @NonNull Long recId) {
        // 检测
        recruitmentService.checkNotExists(recId);
        // 获取参与本次招新的所有用户问卷
        List<QuestionnaireVO> questionnaireVOS = recruitmentService.getStuIdsByRecId(recId)
                .stream()
                .map(stuId -> questionnaireService.getQuestionnaire(stuId, recId))
                .sorted(Comparator.comparingInt(x -> x.getTimePeriodVOS().size()))
                .collect(Collectors.toList());
        // 返回问卷
        return SystemJsonResponse.SYSTEM_SUCCESS(questionnaireVOS);
    }

    @PostMapping("/submit")
    public SystemJsonResponse submitQuestionnaire(@RequestBody QuestionnaireDTO questionnaireDTO) {
        // 检测
        ValidatorUtils.validate(questionnaireDTO);
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        questionnaireService.checkUser(stuId, questionnaireDTO.getQuestionnaireId());
        // 提交问卷
        questionnaireService.submitQuestionnaire(questionnaireDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
