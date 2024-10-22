package com.achobeta.domain.question.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.paper.model.converter.LibraryConverter;
import com.achobeta.domain.question.model.dto.LibraryReferenceQuestionDTO;
import com.achobeta.domain.question.model.dto.QuestionLibraryDTO;
import com.achobeta.domain.question.model.entity.QuestionLibrary;
import com.achobeta.domain.question.service.QuestionLibraryService;
import com.achobeta.domain.question.service.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
 * Time: 12:15
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/question")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class QuestionLibraryController {

    private final QuestionLibraryService questionLibraryService;

    private final QuestionService questionService;

    @PostMapping("/create")
    public SystemJsonResponse createQuestionLibrary(@RequestParam("libType") @NotBlank String libType) {
        Long questionLibraryId = questionLibraryService.createQuestionLibrary(libType);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionLibraryId);
    }

    @PostMapping("/reference")
    public SystemJsonResponse referenceQuestions(@Valid @RequestBody LibraryReferenceQuestionDTO libraryReferenceQuestionDTO) {
        Long libId = libraryReferenceQuestionDTO.getLibId();
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 引用
        questionService.referenceQuestions(libraryReferenceQuestionDTO.getLibId(), libraryReferenceQuestionDTO.getQuestionIds());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/rename")
    public SystemJsonResponse renameQuestionLibrary(@Valid @RequestBody QuestionLibraryDTO questionLibraryDTO) {
        // 检查
        Long libId = questionLibraryDTO.getLibId();
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 重命名
        questionLibraryService.renameQuestionLibrary(libId, questionLibraryDTO.getLibType());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list")
    public SystemJsonResponse getQuestionLibraries() {
        List<QuestionLibrary> questionLibraries = questionLibraryService.getQuestionLibraries();
        return SystemJsonResponse.SYSTEM_SUCCESS(LibraryConverter.INSTANCE.questionLibraryListToQuestionLibraryVOList(questionLibraries));
    }
}
