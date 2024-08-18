package com.achobeta.domain.login.model.dao;

import com.achobeta.common.base.BaseIncrIDEntity;
import com.achobeta.common.enums.UserTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 14:14
 */
@Getter
@Setter
@TableName("user")
public class UserEntity extends BaseIncrIDEntity {

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户类型
     */
    private int userType;
    /**
     * 用户头像
     */
    private Long avatar;

}
