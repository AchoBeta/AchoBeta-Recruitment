package com.achobeta.domain.paper.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.paper.handler.chain.RemoveQuestionHandlerChain;
import com.achobeta.domain.paper.model.dto.PaperEntryDTO;
import com.achobeta.domain.paper.model.vo.PaperQuestionsVO;
import com.achobeta.domain.paper.service.PaperEntryService;
import com.achobeta.domain.paper.service.QuestionEntryService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 2:02
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paperentry")
public class PaperEntryController {

    private final QuestionEntryService questionEntryService;

    private final QuestionPaperService questionPaperService;

    private final PaperEntryService paperEntryService;

    private final RemoveQuestionHandlerChain removeQuestionHandlerChain;

    @PostMapping("/add")
    SystemJsonResponse addQuestionForPaper(@RequestBody PaperEntryDTO paperEntryDTO) {
        // 检查
        ValidatorUtils.validate(paperEntryDTO);
        Long paperId = paperEntryDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        Long questionId = paperEntryDTO.getQuestionId();
        questionEntryService.checkQuestionExists(questionId);
        // 为题单添加一道题
        paperEntryService.addQuestionForPaper(paperId, questionId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove")
    SystemJsonResponse removeQuestionFromPaper(@RequestBody PaperEntryDTO paperEntryDTO) {
        // 检查
        ValidatorUtils.validate(paperEntryDTO);
        Long paperId = paperEntryDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        Long questionId = paperEntryDTO.getQuestionId();
        questionEntryService.checkQuestionExists(questionId);
        // 从题单里移除一道题
        paperEntryService.removeQuestionFromPaper(paperId, questionId);
        // 删除关联的东西
        removeQuestionHandlerChain.handle(paperId, questionId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/get/{paperId}")
    SystemJsonResponse getQuestionsOnPaper(@PathVariable("paperId") @NotNull Long paperId) {
        // 检查
        questionPaperService.checkPaperExists(paperId);
        // 查询一份题单的所有题
        PaperQuestionsVO questionsOnPaper = paperEntryService.getQuestionsOnPaper(paperId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionsOnPaper);
    }

}
