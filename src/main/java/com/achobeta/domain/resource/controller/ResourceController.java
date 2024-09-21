package com.achobeta.domain.resource.controller;

import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.MediaUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:13
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
@Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/{code}")
    public void analyzeCode(HttpServletResponse response,
                              @PathVariable("code") @NotNull Long code) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        String url = resourceService.analyzeCode(userId, code);
        response.setContentType(ResourceConstants.DEFAULT_MEDIA_TYPE);
        byte[] bytes = MediaUtil.getBytes(url);
        try (OutputStream outputStream = response.getOutputStream()) {
            if(Objects.nonNull(bytes)) {
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
