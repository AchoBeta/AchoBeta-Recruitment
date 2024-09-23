package com.achobeta.domain.resource.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.resource.model.converter.DigitalResourceConverter;
import com.achobeta.domain.resource.model.dto.ResourceQueryDTO;
import com.achobeta.domain.resource.model.vo.ResourceAccessLevelVO;
import com.achobeta.domain.resource.model.vo.ResourceQueryVO;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    private final DigitalResourceService digitalResourceService;

    @GetMapping("/get/{code}")
    @Intercept(ignore = true)
    public void analyzeCode(@PathVariable("code") @NotNull Long code, HttpServletResponse response) {
        String fileName = resourceService.analyzeCode(code);
        resourceService.download(fileName, response);
    }

    @PostMapping("/query")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse getResourceList(@RequestBody(required = false) ResourceQueryDTO resourceQueryDTO) {
        ResourceQueryVO resourceQueryVO = digitalResourceService.queryResources(resourceQueryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(resourceQueryVO);
    }

    @GetMapping("/list/level")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse getLevels() {
        List<ResourceAccessLevelVO> resourceAccessLevelVOList =
                DigitalResourceConverter.INSTANCE.levelListToLevelVOList(List.of(ResourceAccessLevel.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(resourceAccessLevelVOList);
    }

    @PostMapping("/upload/one")
    public SystemJsonResponse upload(@RequestParam("file") MultipartFile file) {
        long userId = BaseContext.getCurrentUser().getUserId();
        Long code = resourceService.upload(userId, file);
        return SystemJsonResponse.SYSTEM_SUCCESS(code);
    }

    @PostMapping("/upload/list")
    public SystemJsonResponse upload(@RequestParam("file") List<MultipartFile> fileList) {
        long userId = BaseContext.getCurrentUser().getUserId();
        List<Long> codeList = resourceService.uploadList(userId, fileList);
        return SystemJsonResponse.SYSTEM_SUCCESS(codeList);
    }

    @PostMapping("/set/{id}")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse setLevel(@PathVariable("id") @NotNull Long id,
                                       @RequestParam(name = "level") @NotNull Integer level) {
        ResourceAccessLevel accessLevel = ResourceAccessLevel.get(level);
        resourceService.setAccessLevel(id, accessLevel);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/remove/{code}")
    public SystemJsonResponse remove(@PathVariable("code") @NotNull Long code) {
        resourceService.remove(code);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
