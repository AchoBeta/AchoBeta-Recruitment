package com.achobeta.domain.student.controller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.domain.student.model.dto.StuResumeDTO;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.context.BaseContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 22:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resume")
public class StuResumeController {

    private final StuResumeService stuResumeService;

    /**
     * 此接口单纯为了提供简历
     * todo：需要编写更多细节的业务~
     * @param stuResumeDTO
     * @return
     */
    @PostMapping("/submit")
    public SystemJsonResponse submitResume(@RequestBody StuResumeDTO stuResumeDTO) {
        // 当前用户
        long stuId = BaseContext.getCurrentUser().getUserId();
        StuResume stuResume = BeanUtil.copyProperties(stuResumeDTO, StuResume.class);
        stuResume.setUserId(stuId);
        stuResumeService.save(stuResume);
        return SystemJsonResponse.SYSTEM_SUCCESS(stuResume.getId());
    }

}
