package com.achobeta.domain.users.service.impl;

import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.users.model.converter.UserConverter;
import com.achobeta.domain.users.model.dto.UserDTO;
import com.achobeta.domain.users.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:26
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
        implements UserService {

    private final ResourceService resourceService;

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return this.lambdaQuery()
                .eq(UserEntity::getId, id)
                .oneOpt();
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return this.lambdaQuery()
                .eq(UserEntity::getUsername, username)
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
    @Transactional
    public void updateUser(Long userId, UserDTO userDTO) {
        // 设置默认头像
        Long avatar = userDTO.getAvatar();
        UserEntity userEntity = UserConverter.INSTANCE.userDTOToUser(userDTO);
        Long oldAvatar = getUserById(userId).map(UserEntity::getAvatar).orElse(null);
        Boolean shouldRemove = resourceService.shouldRemove(avatar, oldAvatar);
        // 更新
        this.lambdaUpdate()
                .eq(UserEntity::getId, userId)
                .update(userEntity);
        // 更新成功再删除
        if(Boolean.TRUE.equals(shouldRemove)) {
            resourceService.removeKindly(oldAvatar);
        }
    }
}
