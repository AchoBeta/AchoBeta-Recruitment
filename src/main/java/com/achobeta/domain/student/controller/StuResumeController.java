package com.achobeta.domain.student.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.dto.QueryResumeOfUserDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.util.ValidatorUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public SystemJsonResponse submitResume(@RequestBody StuResumeDTO stuResumeDTO) {
        //校验
        ValidatorUtils.validate(stuResumeDTO.getStuSimpleResumeDTO());
        Optional.ofNullable(stuResumeDTO.getStuAttachmentDTOList())
                .ifPresent(data->ValidatorUtils.validate(data));
        /*ValidatorUtils.validate(stuResumeDTO.getStuAttachmentDTOList());*/
        //当前用户id
        Long userId = BaseContext.getCurrentUser().getUserId();
        //检查简历提交否超过最大次数
        StuResume stuResume = stuResumeService.checkResumeSubmitCount(stuResumeDTO.getStuSimpleResumeDTO(),userId);
        //提交简历
        stuResumeService.submitResume(stuResumeDTO,stuResume);

        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    /**
     * @description 根据简历id或(batchId+userId)查询个人详细简历信息
     * @param queryResumeDTO
     * @return stuResumeVO
     */
    @PostMapping("/query")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse queryResumeInfo(@RequestBody QueryResumeDTO queryResumeDTO) {
        //校验
        Optional.ofNullable(queryResumeDTO.getQueryResumeOfUserDTO())
                .ifPresent(data->ValidatorUtils.validate(data));
        //获取简历信息
        StuResumeVO stuResumeVO= stuResumeService.getResumeInfo(queryResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResumeVO);
    }

    // todo: 资源上传的接口，最好限制上传的文件格式以及文件大小，并且检查真实的文件内容与文件后缀是否符合（可以用责任链审核）

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
