package com.achobeta.domain.paper.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.paper.handler.chain.RemoveQuestionFromPaperHandlerChain;
import com.achobeta.domain.paper.model.dto.PaperQuestionLinkDTO;
import com.achobeta.domain.paper.model.vo.QuestionPaperDetailVO;
import com.achobeta.domain.paper.service.PaperQuestionLinkService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 9:00
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pqlink")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class PaperQuestionLinkController {

    private final QuestionPaperService questionPaperService;

    private final PaperQuestionLinkService paperQuestionLinkService;

    private final RemoveQuestionFromPaperHandlerChain removeQuestionFromPaperHandlerChain;

    @PostMapping("/add")
    SystemJsonResponse addQuestionForPaper(@Valid @RequestBody PaperQuestionLinkDTO paperQuestionLinkDTO) {
        // 检查
        Long paperId = paperQuestionLinkDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        // 为题单添加若干题
        paperQuestionLinkService.addQuestionsForPaper(paperId, paperQuestionLinkDTO.getQuestionIds());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove")
    SystemJsonResponse removeQuestionFromPaper(@Valid @RequestBody PaperQuestionLinkDTO paperQuestionLinkDTO) {
        // 检查
        Long paperId = paperQuestionLinkDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        // 从题单里移除一道题
        List<Long> questionIds = paperQuestionLinkDTO.getQuestionIds();
        paperQuestionLinkService.removeQuestionsFromPaper(paperId, questionIds);
        // 触发责任链
        removeQuestionFromPaperHandlerChain.handle(paperId, questionIds);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/detail/{paperId}")
    SystemJsonResponse getPaperDetail(@PathVariable("paperId") @NotNull Long paperId) {
        // 检查
        questionPaperService.checkPaperExists(paperId);
        // 查询一份题单的所有题
        QuestionPaperDetailVO questionPaperDetailVO = paperQuestionLinkService.getPaperDetail(paperId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionPaperDetailVO);
    }

}
