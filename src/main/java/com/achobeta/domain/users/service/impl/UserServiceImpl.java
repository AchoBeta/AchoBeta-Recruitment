package com.achobeta.domain.users.service.impl;

import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dao.mapper.UserMapper;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.resource.util.ResourceUtil;
import com.achobeta.domain.users.model.converter.UserConverter;
import com.achobeta.domain.users.model.dto.UserDTO;
import com.achobeta.domain.users.service.UserService;
import com.achobeta.util.MediaUtil;
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
        getUserById(userId).map(UserEntity::getAvatar).filter(avatar::equals).ifPresentOrElse(code -> {}, () -> {
            // 加载资源
            byte[] bytes = resourceService.load(avatar);
            // 判断是否为图片
            ResourceUtil.checkImage(MediaUtil.getContentType(bytes));
            // 更新
            this.lambdaUpdate()
                    .eq(UserEntity::getId, userId)
                    .update(UserConverter.INSTANCE.userDTOToUser(userDTO));
            // 删除原图片
            resourceService.remove(avatar);
        });
    }
}
