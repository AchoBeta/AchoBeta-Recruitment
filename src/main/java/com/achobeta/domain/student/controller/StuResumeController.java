package com.achobeta.domain.student.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.dto.QueryResumeOfUserDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * @description 提交简历
     * @param stuResumeDTO
     * @return
     */
    @PostMapping("/submit")
    @Intercept(permit = {UserTypeEnum.USER})
    public SystemJsonResponse submitResume(@Valid @RequestBody StuResumeDTO stuResumeDTO) {
        //当前用户id
        Long userId = BaseContext.getCurrentUser().getUserId();
        //提交简历
        stuResumeService.submitResume(stuResumeDTO, userId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    /**
     * @description 根据简历id或(batchId+userId)查询个人详细简历信息
     * @param queryResumeDTO
     * @return stuResumeVO
     */
    @PostMapping("/query")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse queryResumeInfo(@Valid @RequestBody QueryResumeDTO queryResumeDTO) {
        //获取简历信息
        StuResumeVO stuResumeVO= stuResumeService.getResumeInfo(queryResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResumeVO);
    }

    @GetMapping("/query/{batchId}")
    @Intercept(permit = {UserTypeEnum.USER})
    public SystemJsonResponse queryResumeInfo(@PathVariable("batchId") @NotNull Long batchId) {
        QueryResumeDTO queryResumeDTO = new QueryResumeDTO();
        QueryResumeOfUserDTO queryResumeOfUserDTO = new QueryResumeOfUserDTO();
        queryResumeOfUserDTO.setBatchId(batchId);
        queryResumeOfUserDTO.setUserId(BaseContext.getCurrentUser().getUserId());
        queryResumeDTO.setQueryResumeOfUserDTO(queryResumeOfUserDTO);
        //获取简历信息
        StuResumeVO stuResumeVO= stuResumeService.getResumeInfo(queryResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResumeVO);
    }

}
