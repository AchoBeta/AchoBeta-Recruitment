package com.achobeta.domain.question.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.qpaper.service.QuestionPaperLibraryService;
import com.achobeta.domain.question.model.dto.QuestionLibraryDTO;
import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.achobeta.domain.question.model.vo.QuestionLibraryVO;
import com.achobeta.domain.question.service.QuestionLibraryService;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 12:15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/question")
public class QuestionLibraryController {

    private final QuestionLibraryService questionLibraryService;

    @PostMapping("/create")
    public SystemJsonResponse createQuestionLibrary(@RequestParam("libType") @NotBlank String libType) {
        Long questionLibraryId = questionLibraryService.createQuestionLibrary(libType);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionLibraryId);
    }

    @PostMapping("/rename")
    public SystemJsonResponse renameQuestionLibrary(@RequestBody QuestionLibraryDTO questionLibraryDTO) {
        // 检查
        ValidatorUtils.validate(questionLibraryDTO);
        Long libId = questionLibraryDTO.getLibId();
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 重命名
        questionLibraryService.renameQuestionLibrary(libId, questionLibraryDTO.getLibType());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list")
    public SystemJsonResponse getQuestionLibraries() {
        List<QuestionLibrary> questionLibraries = questionLibraryService.getQuestionLibraries();
        return SystemJsonResponse.SYSTEM_SUCCESS(BeanUtil.copyToList(questionLibraries, QuestionLibraryVO.class));
    }
}
