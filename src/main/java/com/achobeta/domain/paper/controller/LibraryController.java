package com.achobeta.domain.paper.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.paper.model.dto.PaperLibraryDTO;
import com.achobeta.domain.paper.model.dto.QuestionLibraryDTO;
import com.achobeta.domain.paper.model.entity.QuestionLibrary;
import com.achobeta.domain.paper.model.entity.QuestionPaperLibrary;
import com.achobeta.domain.paper.model.vo.PaperLibraryVO;
import com.achobeta.domain.paper.model.vo.QuestionLibraryVO;
import com.achobeta.domain.paper.service.QuestionLibraryService;
import com.achobeta.domain.paper.service.QuestionPaperLibraryService;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-15
 * Time: 1:27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library")
public class LibraryController {

    private final QuestionLibraryService questionLibraryService;

    private final QuestionPaperLibraryService questionPaperLibraryService;

    @PostMapping("/question/create")
    public SystemJsonResponse createQuestionLibrary(@RequestParam("libType") @NotBlank String libType) {
        Long questionLibraryId = questionLibraryService.createQuestionLibrary(libType);
        return SystemJsonResponse.SYSTEM_SUCCESS(questionLibraryId);
    }

    @PostMapping("/paper/create")
    public SystemJsonResponse createPaperLibrary(@RequestParam("libType") @NotBlank String libType) {
        Long paperLibraryId = questionPaperLibraryService.createPaperLibrary(libType);
        return SystemJsonResponse.SYSTEM_SUCCESS(paperLibraryId);
    }

    @PostMapping("/question/rename")
    public SystemJsonResponse renameQuestionLibrary(@RequestBody QuestionLibraryDTO questionLibraryDTO) {
        // 检查
        ValidatorUtils.validate(questionLibraryDTO);
        Long libId = questionLibraryDTO.getLibId();
        questionLibraryService.checkQuestionLibraryExists(libId);
        // 重命名
        questionLibraryService.renameQuestionLibrary(libId, questionLibraryDTO.getLibType());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/paper/rename")
    public SystemJsonResponse renamePaperLibrary(@RequestBody PaperLibraryDTO paperLibraryDTO) {
        // 检查
        ValidatorUtils.validate(paperLibraryDTO);
        Long libId = paperLibraryDTO.getLibId();
        questionPaperLibraryService.checkPaperLibraryExists(libId);
        // 重命名
        questionPaperLibraryService.renamePaperLibrary(libId, paperLibraryDTO.getLibType());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/question/list")
    public SystemJsonResponse getQuestionLibraries() {
        List<QuestionLibrary> questionLibraries = questionLibraryService.getQuestionLibraries();
        return SystemJsonResponse.SYSTEM_SUCCESS(BeanUtil.copyToList(questionLibraries, QuestionLibraryVO.class));
    }

    @GetMapping("/paper/list")
    public SystemJsonResponse getPaperLibraries() {
        List<QuestionPaperLibrary> paperLibraries = questionPaperLibraryService.getPaperLibraries();
        return SystemJsonResponse.SYSTEM_SUCCESS(BeanUtil.copyToList(paperLibraries, PaperLibraryVO.class));
    }

}
