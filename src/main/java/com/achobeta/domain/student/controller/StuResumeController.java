package com.achobeta.domain.student.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.util.ValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public SystemJsonResponse queryResumeInfo(@RequestBody QueryResumeDTO queryResumeDTO) {
        //校验
        Optional.ofNullable(queryResumeDTO.getQueryResumeOfUserDTO())
                .ifPresent(data->ValidatorUtils.validate(data));
        //获取简历信息
        StuResumeVO stuResumeVO= stuResumeService.getResumeInfo(queryResumeDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResumeVO);
    }


}
