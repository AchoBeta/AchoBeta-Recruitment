package com.achobeta.domain.resource.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.converter.DigitalResourceConverter;
import com.achobeta.domain.resource.model.dto.ResourceQueryDTO;
import com.achobeta.domain.resource.model.vo.ResourceAccessLevelVO;
import com.achobeta.domain.resource.model.vo.ResourceQueryVO;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.service.UserService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.MediaUtil;
import com.achobeta.util.ResourceUtil;
import com.achobeta.util.TimeUtil;
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

    private final UserService userService;

    private final ResourceService resourceService;

    private final DigitalResourceService digitalResourceService;

    @GetMapping("/preview/{code}")
    @Intercept(ignore = true)
    public void preview(@PathVariable("code") @NotNull Long code, HttpServletResponse response) {
        resourceService.preview(code, response);
    }

    @GetMapping("/download/{code}")
    @Intercept(ignore = true)
    public void download(@PathVariable("code") @NotNull Long code, HttpServletResponse response) {
        resourceService.download(code, response);
    }

    @GetMapping("/share/{code}")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse gerObjectUrl(@PathVariable("code") @NotNull Long code,
                                           @RequestParam(name = "hidden", required = false) Boolean hidden) {
        String url = resourceService.gerObjectUrl(code, hidden);
        return SystemJsonResponse.SYSTEM_SUCCESS(url);
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
    public SystemJsonResponse upload(@RequestPart("file") MultipartFile file) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        Long code = resourceService.upload(userId, file);
        return SystemJsonResponse.SYSTEM_SUCCESS(code);
    }

    @PostMapping("/upload/image")
    public SystemJsonResponse uploadImage(@RequestPart("file") MultipartFile file) {
        // 检查
        ResourceUtil.checkImage(MediaUtil.getContentType(file));
        Long userId = BaseContext.getCurrentUser().getUserId();
        Long code = resourceService.upload(userId, file);
        return SystemJsonResponse.SYSTEM_SUCCESS(code);
    }

    @PostMapping("/upload/video")
    public SystemJsonResponse uploadVideo(@RequestPart("file") MultipartFile file) {
        // 检查
        ResourceUtil.checkVideo(MediaUtil.getContentType(file));
        Long userId = BaseContext.getCurrentUser().getUserId();
        Long code = resourceService.upload(userId, file);
        return SystemJsonResponse.SYSTEM_SUCCESS(code);
    }

    @PostMapping("/upload/list")
    public SystemJsonResponse upload(@RequestPart("file") List<MultipartFile> fileList) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        List<Long> codeList = resourceService.uploadList(userId, fileList);
        return SystemJsonResponse.SYSTEM_SUCCESS(codeList);
    }

    @PostMapping("/set/level/{id}")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse setLevel(@PathVariable("id") @NotNull Long id,
                                       @RequestParam("level") @NotNull Integer level) {
        ResourceAccessLevel accessLevel = ResourceAccessLevel.get(level);
        resourceService.setAccessLevel(id, accessLevel);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/remove/{code}")
    public SystemJsonResponse remove(@PathVariable("code") @NotNull Long code) {
        resourceService.remove(code);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/block/upload/{userId}")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse blockUser(@PathVariable("userId") @NotNull Long userId,
                                        @RequestParam("date") @NotNull Long date) {
        Long managerId = BaseContext.getCurrentUser().getUserId();
        String blockDDL = TimeUtil.getDateTime(date);
        log.warn("管理员 {} 尝试封禁账号 {} 的资源上传，解封时间：{}", managerId, userId, blockDDL);
        // 检测一下
        userService.getUserById(userId).ifPresentOrElse(user -> {
            if(managerId.equals(userId) || UserTypeEnum.ADMIN.getCode().equals(user.getUserType())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
            }
        }, () -> {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_ACCOUNT_NOT_EXIST);
        });
        // 封禁
        resourceService.blockUser(userId, date);
        log.warn("管理员 {} 成功限制账号 {} 的资源上传，解封时间：{}", managerId, userId, blockDDL);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
