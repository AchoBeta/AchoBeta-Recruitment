package com.achobeta.domain.users.service.login.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.achobeta.common.constants.LoginType;
import com.achobeta.common.constants.RoleKinds;
import com.achobeta.domain.users.jwt.propertities.JwtProperties;
import com.achobeta.domain.users.jwt.util.JwtUtil;
import com.achobeta.domain.users.model.po.StudentEntity;
import com.achobeta.domain.users.model.vo.LoginVO;
import com.achobeta.domain.users.service.StudentService;
import com.achobeta.domain.users.service.login.LoginStrategy;
import com.achobeta.interpretor.UserInterpretor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author cattleYuan
 * @date 2024/1/20
 */
@Component("mail")
@Slf4j
@RequiredArgsConstructor
public class MailLoginStrategy implements LoginStrategy {
    private final StudentService studentService;
    private final JwtProperties jwtProperties;
    @Override
    public boolean match(LoginType loginType) {
        return ObjectUtil.equal(loginType,LoginType.LOGINBYEMAIL);
    }
    @Override
    public LoginVO login(String authenticationInfo) {

        HashMap<String, Object> claims = new HashMap<>();
        //从数据库查找该符合该邮箱的同学
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getUserSecretKey());
        StudentEntity studentEntity = studentService.lambdaQuery().eq(StudentEntity::getEmail, authenticationInfo).one();
        //若student为空则存入新用户
        if(!Optional.ofNullable(studentEntity).isPresent()){
            studentEntity=registWithEmail(authenticationInfo, studentEntity);
        }

        //将id存入claims
        if (Optional.ofNullable(studentEntity.getId()).isPresent()) {
            claims.put(UserInterpretor.USER_ID, studentEntity.getId());
            claims.put(RoleKinds.USER.getRoleName(), RoleKinds.USER.getRoleNumber());
        }

        String token = JwtUtil.createJWT(secretKey, jwtProperties.getUserTtl(), claims);
        LoginVO loginVO = LoginVO.builder()
                .accessToken(token)
                .expiresIn(jwtProperties.getUserTtl())
                .build();

        log.info("邮箱登录");

        return loginVO;
    }
    private StudentEntity registWithEmail(String email, StudentEntity studentEntity) {
        studentEntity=new StudentEntity();
        studentEntity.setEmail(email);
        studentService.save(studentEntity);
        return studentEntity;

    }
}
