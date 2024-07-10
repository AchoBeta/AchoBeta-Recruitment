package com.achobeta.domain.student.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.service.StuResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public SystemJsonResponse submitResume( @Valid @RequestBody StuResumeDTO stuResumeDTO) {
        stuResumeService.submitResume(stuResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    /**
     * 根据简历id或(batchId+userId)查询个人详细简历信息
     * @param queryResumeDTO
     * @return stuResumeVO
     */
    @PostMapping("/query")
    public SystemJsonResponse queryResumeInfo(@RequestBody @Valid QueryResumeDTO queryResumeDTO) {
        StuResumeVO stuResumeVO= stuResumeService.getResumeInfo(queryResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResumeVO);
    }


}
