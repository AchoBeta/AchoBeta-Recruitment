package com.achobeta.domain.paper.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.paper.handler.chain.RemovePaperHandlerChain;
import com.achobeta.domain.paper.model.dto.QuestionPaperDTO;
import com.achobeta.domain.paper.model.vo.QuestionPaperVO;
import com.achobeta.domain.paper.service.QuestionPaperLibraryService;
import com.achobeta.domain.paper.service.QuestionPaperService;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 1:28
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question/paper")
public class QuestionPaperController {

    private final QuestionPaperLibraryService questionPaperLibraryService;

    private final QuestionPaperService questionPaperService;

    private final RemovePaperHandlerChain removePaperHandlerChain;

    @PostMapping("/add/{libId}")
    public SystemJsonResponse addQuestionPaper(@PathVariable("libId") @NotNull Long libId,
                                               @RequestParam("title") @NotBlank String title) {
        // 检查
        questionPaperLibraryService.checkPaperLibraryExists(libId);
        // 添加
        Long paperId = questionPaperService.addQuestionPaper(libId, title);
        return SystemJsonResponse.SYSTEM_SUCCESS(paperId);
    }

    @PostMapping("/rename")
    public SystemJsonResponse renamePaperTitle(@RequestBody QuestionPaperDTO questionPaperDTO){
        // 检查
        ValidatorUtils.validate(questionPaperDTO);
        Long paperId = questionPaperDTO.getPaperId();
        questionPaperService.checkPaperExists(paperId);
        // 重命名
        questionPaperService.renamePaperTitle(paperId, questionPaperDTO.getTitle());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove/{paperId}")
    public SystemJsonResponse removeQuestionPaper(@PathVariable("paperId") @NotNull Long paperId) {
        // 检查
        questionPaperService.checkPaperExists(paperId);
        // 删除
        questionPaperService.removeQuestionPaper(paperId);
        // 删除一些关联的东西
        removePaperHandlerChain.handle(paperId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/{libId}")
    public SystemJsonResponse getQuestionPapers(@PathVariable("libId") @NotNull Long libId) {
        // 检查
        questionPaperLibraryService.checkPaperLibraryExists(libId);
        // 查询
        List<QuestionPaperVO> questionPapers = questionPaperService.getQuestionPapers(libId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionPapers);
    }

}
