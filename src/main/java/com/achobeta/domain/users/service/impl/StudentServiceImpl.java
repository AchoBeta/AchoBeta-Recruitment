package com.achobeta.domain.users.service.impl;

import com.achobeta.common.constants.RoleType;
import com.achobeta.interpretor.UserInterpretor;
import com.achobeta.domain.users.jwt.propertities.JwtProperties;
import com.achobeta.domain.users.jwt.util.JwtUtil;
import com.achobeta.domain.users.model.po.StudentEntity;
import com.achobeta.domain.users.model.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.users.service.StudentService;
import com.achobeta.domain.users.model.dao.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

/**
* @author cattleyuan
* @description 针对表【student(学生用户简历表)】的数据库操作Service实现
* @createDate 2024-01-19
*/
@RequiredArgsConstructor
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentEntity>
    implements StudentService{
    private final JwtProperties jwtProperties;

    @Override
    public LoginVO login(String email) {
        HashMap<String, Object> claims = new HashMap<>();
        //从数据库查找该符合该邮箱的同学
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getUserSecretKey());
        StudentEntity studentEntity = this.lambdaQuery().eq(StudentEntity::getEmail, email).one();
        //若student为空则存入新用户
        if(!Optional.ofNullable(studentEntity).isPresent()){
            studentEntity=registWithEmail(email, studentEntity);
        }

        //将id存入claims
        if (Optional.ofNullable(studentEntity.getId()).isPresent()) {
            claims.put(UserInterpretor.USER_ID, studentEntity.getId());
            claims.put(RoleType.USER.getRoleName(),RoleType.USER.getRoleNumber());
        }

        String token = JwtUtil.createJWT(secretKey, jwtProperties.getUserTtl(), claims);
        LoginVO loginVO = LoginVO.builder()
                .accessToken(token)
                .expiresIn(jwtProperties.getUserTtl())
                .build();

        return loginVO;
    }

    private StudentEntity registWithEmail(String email, StudentEntity studentEntity) {
            studentEntity=new StudentEntity();
            studentEntity.setEmail(email);
            this.save(studentEntity);
            return studentEntity;

    }




}




