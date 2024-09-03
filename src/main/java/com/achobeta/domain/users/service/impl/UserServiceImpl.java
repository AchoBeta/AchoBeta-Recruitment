package com.achobeta.domain.users.service.impl;

import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.users.model.converter.UserConverter;
import com.achobeta.domain.users.model.dto.UserDTO;
import com.achobeta.domain.users.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
        implements UserService {

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return this.lambdaQuery()
                .eq(UserEntity::getId, id)
                .oneOpt();
    }

    @Override
    public void confirmByManagerId(Long managerId) {
        this.lambdaUpdate()
                .eq(UserEntity::getId, managerId)
                .set(UserEntity::getUserType, UserTypeEnum.ADMIN.getCode())
                .update();
    }

    @Override
    public void updateUser(Long userId, UserDTO userDTO) {
        this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .update(UserConverter.INSTANCE.userDTOToUser(userDTO));
    }
}
