package com.achobeta.domain.users.model.vo;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:37
 */
@Data
public class UserVO {

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Integer userType;

    private Long avatar;

}
