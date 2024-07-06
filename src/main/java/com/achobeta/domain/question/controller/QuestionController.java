package com.achobeta.domain.question.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.question.handler.chain.RemoveQuestionHandlerChain;
import com.achobeta.domain.question.model.dto.QuestionDTO;
import com.achobeta.domain.question.model.vo.QuestionDetailVO;
import com.achobeta.domain.question.model.vo.QuestionVO;
import com.achobeta.domain.question.service.QuestionLibraryService;
import com.achobeta.domain.question.service.QuestionService;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 0:59
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class QuestionController {

    private final QuestionLibraryService questionLibraryService;

    private final QuestionService questionService;

    private final RemoveQuestionHandlerChain removeQuestionHandlerChain;

    @PostMapping("/add")
    public SystemJsonResponse addQuestion(@RequestBody QuestionDTO questionDTO) {
        // 检查
        ValidatorUtils.validate(questionDTO);
        // 添加
        questionService.addQuestion(questionDTO.getLibIds(), questionDTO.getTitle(), questionDTO.getStandard());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/update/{questionId}")
    public SystemJsonResponse updateQuestion(@PathVariable("questionId") @NotNull Long questionId,
                                             @RequestBody QuestionDTO questionDTO){
        // 检查
        ValidatorUtils.validate(questionDTO);
        questionService.checkQuestionExists(questionId);
        // 更新
        questionService.updateQuestion(questionId, questionDTO.getLibIds(),
                questionDTO.getTitle(), questionDTO.getStandard());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove/{questionId}")
    public SystemJsonResponse removeQuestionEntry(@PathVariable("questionId") @NotNull Long questionId) {
        // 检查
        questionService.checkQuestionExists(questionId);
        // 删除
        questionService.removeQuestion(questionId);
        // 触发责任链
        removeQuestionHandlerChain.handle(questionId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/{libId}")
    public SystemJsonResponse getQuestionEntries(@PathVariable("libId") @NotNull Long libId) {
        // 检查
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 查询
        List<QuestionVO> questions = questionService.getQuestionsByLibId(libId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questions);
    }

    @GetMapping("/detail/{questionId}")
    SystemJsonResponse getQuestionDetail(@PathVariable("questionId") @NotNull Long questionId) {
        // 检查
        questionService.checkQuestionExists(questionId);
        // 查询这道题
        QuestionDetailVO questionDetailVO = questionService.getQuestionDetail(questionId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionDetailVO);
    }

}