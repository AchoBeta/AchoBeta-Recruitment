package com.achobeta.domain.paper.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.paper.model.dto.PaperLibraryDTO;
import com.achobeta.domain.paper.model.entity.QuestionPaperLibrary;
import com.achobeta.domain.paper.model.vo.PaperLibraryVO;
import com.achobeta.domain.paper.service.QuestionPaperLibraryService;
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
 * Time: 0:59
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/paper")
public class PaperLibraryController {

    private final QuestionPaperLibraryService questionPaperLibraryService;

    @PostMapping("/create")
    public SystemJsonResponse createPaperLibrary(@RequestParam("libType") @NotBlank String libType) {
        Long paperLibraryId = questionPaperLibraryService.createPaperLibrary(libType);
        return SystemJsonResponse.SYSTEM_SUCCESS(paperLibraryId);
    }

    @PostMapping("/rename")
    public SystemJsonResponse renamePaperLibrary(@RequestBody PaperLibraryDTO paperLibraryDTO) {
        // 检查
        ValidatorUtils.validate(paperLibraryDTO);
        Long libId = paperLibraryDTO.getLibId();
        questionPaperLibraryService.checkPaperLibraryExists(libId);
        // 重命名
        questionPaperLibraryService.renamePaperLibrary(libId, paperLibraryDTO.getLibType());
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/list")
    public SystemJsonResponse getPaperLibraries() {
        List<QuestionPaperLibrary> paperLibraries = questionPaperLibraryService.getPaperLibraries();
        return SystemJsonResponse.SYSTEM_SUCCESS(BeanUtil.copyToList(paperLibraries, PaperLibraryVO.class));
    }

}
