package com.achobeta.domain.paper.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.paper.model.converter.LibraryConverter;
import com.achobeta.domain.paper.model.dto.PaperLibraryDTO;
import com.achobeta.domain.paper.model.entity.QuestionPaperLibrary;
import com.achobeta.domain.paper.service.QuestionPaperLibraryService;
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
 * Time: 0:59
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/paper")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class PaperLibraryController {

    private final QuestionPaperLibraryService questionPaperLibraryService;

    @PostMapping("/create")
    public SystemJsonResponse createPaperLibrary(@RequestParam("libType") @NotBlank String libType) {
        Long paperLibraryId = questionPaperLibraryService.createPaperLibrary(libType);
        return SystemJsonResponse.SYSTEM_SUCCESS(paperLibraryId);
    }

    @PostMapping("/rename")
    public SystemJsonResponse renamePaperLibrary(@Valid @RequestBody PaperLibraryDTO paperLibraryDTO) {
        // 检查
        Long libId = paperLibraryDTO.getLibId();
        questionPaperLibraryService.checkPaperLibraryExists(libId);
        // 重命名
        questionPaperLibraryService.renamePaperLibrary(libId, paperLibraryDTO.getLibType());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list")
    public SystemJsonResponse getPaperLibraries() {
        List<QuestionPaperLibrary> paperLibraries = questionPaperLibraryService.getPaperLibraries();
        return SystemJsonResponse.SYSTEM_SUCCESS(LibraryConverter.INSTANCE.paperLibraryListToPaperLibraryVOList(paperLibraries));
    }

}
