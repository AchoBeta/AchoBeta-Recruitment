package com.achobeta.domain.users.service;

import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.users.model.dto.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:26
 */
public interface UserService extends IService<UserEntity> {

    Optional<UserEntity> getUserById(Long id);

    Optional<UserEntity> getUserByUsername(String username);

    void confirmByManagerId(Long managerId);

    void updateUser(Long userId, UserDTO userDTO);

}
