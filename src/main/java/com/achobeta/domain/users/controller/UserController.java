package com.achobeta.domain.users.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.domain.users.model.converter.UserConverter;
import com.achobeta.domain.users.model.dto.UserDTO;
import com.achobeta.domain.users.model.po.UserHelper;
import com.achobeta.domain.users.model.vo.UserTypeVO;
import com.achobeta.domain.users.model.vo.UserVO;
import com.achobeta.domain.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:25
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Intercept(permit = {UserTypeEnum.ADMIN, UserTypeEnum.USER})
public class UserController {

    private final UserService userService;

    @GetMapping("/list/type")
    @Intercept(permit = {UserTypeEnum.ADMIN})
    public SystemJsonResponse getUserTypeList() {
        List<UserTypeVO> userTypeVOList =
                UserConverter.INSTANCE.userTypeEnumListToUserTypeVOList(List.of(UserTypeEnum.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(userTypeVOList);
    }

    @GetMapping("/info")
    public SystemJsonResponse getCurrentInfo() {
        Long userId = BaseContext.getCurrentUser().getUserId();
        UserVO userVO = userService.getUserById(userId)
                .map(UserConverter.INSTANCE::userToUserVO)
                .orElseGet(UserVO::new);
        return SystemJsonResponse.SYSTEM_SUCCESS(userVO);
    }

    @PostMapping("/update")
    public SystemJsonResponse updateCurrentInfo(@Valid @RequestBody UserDTO userDTO) {
        UserHelper currentUser = BaseContext.getCurrentUser();
        // 更新
        userService.updateUser(currentUser.getUserId(), userDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

}
