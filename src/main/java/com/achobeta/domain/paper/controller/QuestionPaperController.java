package com.achobeta.domain.paper.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.paper.handler.chain.RemovePaperHandlerChain;
import com.achobeta.domain.paper.model.dto.QuestionPaperDTO;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.achobeta.domain.paper.service.QuestionPaperLibraryService;
import com.achobeta.domain.paper.service.QuestionPaperService;
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
 * Time: 8:35
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qpaper")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class QuestionPaperController {

    private final QuestionPaperLibraryService questionPaperLibraryService;

    private final QuestionPaperService questionPaperService;

    private final RemovePaperHandlerChain removePaperHandlerChain;

    @PostMapping("/add")
    public SystemJsonResponse addQuestionPaper(@RequestBody QuestionPaperDTO questionPaperDTO) {
        // 检查
        ValidatorUtils.validate(questionPaperDTO);
        // 添加
        Long paperId = questionPaperService.addQuestionPaper(questionPaperDTO.getLibIds(), questionPaperDTO.getTitle(), questionPaperDTO.getDescription());
        return SystemJsonResponse.SYSTEM_SUCCESS(paperId);
    }

    @PostMapping("/update/{paperId}")
    public SystemJsonResponse renamePaperTitle(@PathVariable("paperId") @NotNull Long paperId,
                                               @RequestBody QuestionPaperDTO questionPaperDTO){
        // 检查
        ValidatorUtils.validate(questionPaperDTO);
        questionPaperService.checkPaperExists(paperId);
        // 重命名
        questionPaperService.updateQuestionPaper(paperId, questionPaperDTO.getLibIds(),
                questionPaperDTO.getTitle(), questionPaperDTO.getDescription());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove/{paperId}")
    public SystemJsonResponse removeQuestionPaper(@PathVariable("paperId") @NotNull Long paperId) {
        // 检查
        questionPaperService.checkPaperExists(paperId);
        // 删除
        questionPaperService.removeQuestionPaper(paperId);
        // 触发责任链
        removePaperHandlerChain.handle(paperId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/all")
    public SystemJsonResponse getQuestionPapers() {
        // 查询
        List<QuestionPaperVO> questionPapers = questionPaperService.getQuestionPapers();
        return SystemJsonResponse.SYSTEM_SUCCESS(questionPapers);
    }

    @GetMapping("/list/{libId}")
    public SystemJsonResponse getQuestionPapers(@PathVariable("libId") @NotNull Long libId) {
        // 检查
        questionPaperLibraryService.checkPaperLibraryExists(libId);
        // 查询
        List<QuestionPaperVO> questionPapers = questionPaperService.getQuestionPapersByLibId(libId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionPapers);
    }

}