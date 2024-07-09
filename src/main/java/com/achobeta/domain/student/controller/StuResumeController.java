package com.achobeta.domain.student.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.service.StuResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 22:50
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class StuResumeController {

    private final StuResumeService stuResumeService;

    /**
     * 提交简历
     * @param stuResumeDTO
     * @return
     */
    @PostMapping("/submit")
    public SystemJsonResponse submitResume(@RequestBody @Valid StuResumeDTO stuResumeDTO) {
        stuResumeService.submitResume(stuResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    /**
     * 查询详细简历信息
     * @param resumeId
     * @return stuResumeVO
     */
    @GetMapping("/get/{resumeId}")
    public SystemJsonResponse queryResumeInfo(@PathVariable("resumeId") @NotNull(message = "简历id不能为空") Long resumeId) {
        StuResumeVO stuResumeVO= stuResumeService.getResumeInfo(resumeId);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResumeVO);
    }
}
