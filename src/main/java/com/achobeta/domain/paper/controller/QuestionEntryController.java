package com.achobeta.domain.paper.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.paper.model.dto.QuestionEntryDTO;
import com.achobeta.domain.paper.model.vo.QuestionEntryVO;
import com.achobeta.domain.paper.service.QuestionEntryService;
import com.achobeta.domain.paper.service.QuestionLibraryService;
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
 * Time: 1:25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question/entry")
public class QuestionEntryController {

    private final QuestionLibraryService questionLibraryService;

    private final QuestionEntryService questionEntryService;

    @PostMapping("/add/{libId}")
    public SystemJsonResponse addQuestionEntry(@PathVariable("libId") @NotNull Long libId,
                                               @RequestParam("title") @NotBlank String title) {
        // 检查
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 添加
        Long questionId = questionEntryService.addQuestionEntry(libId, title);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionId);
    }

    @PostMapping("/rename")
    public SystemJsonResponse renameQuestionTitle(@RequestBody QuestionEntryDTO questionEntryDTO){
        // 检查
        ValidatorUtils.validate(questionEntryDTO);
        Long questionId = questionEntryDTO.getQuestionId();
        questionEntryService.checkQuestionExists(questionId);
        // 重命名
        questionEntryService.renameQuestionTitle(questionId, questionEntryDTO.getTitle());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/remove/{questionId}")
    public SystemJsonResponse removeQuestionEntry(@PathVariable("questionId") @NotNull Long questionId) {
        // 检查
        questionEntryService.checkQuestionExists(questionId);
        // 删除
        questionEntryService.removeQuestionEntry(questionId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list/{libId}")
    public SystemJsonResponse getQuestionEntries(@PathVariable("libId") @NotNull Long libId) {
        // 检查
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 查询
        List<QuestionEntryVO> questionEntries = questionEntryService.getQuestionEntries(libId);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionEntries);
    }

}
