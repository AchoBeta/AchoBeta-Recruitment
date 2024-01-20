package com.achobeta.domain.users.service.impl;

import cn.hutool.core.util.StrUtil;
import com.achobeta.domain.users.interpretor.UserInterpretor;
import com.achobeta.domain.users.jwt.propertities.JwtProperties;
import com.achobeta.domain.users.jwt.util.JwtUtil;
import com.achobeta.domain.users.model.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.users.model.po.Student;
import com.achobeta.domain.users.service.StudentService;
import com.achobeta.domain.users.model.dao.StudentMapper;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{
    private final JwtProperties jwtProperties;

    @Override
    public LoginVO saveStudentByEmail(String email) {
        HashMap<String, Object> claims = new HashMap<>();
        //从数据库查找该符合该邮箱的同学
        SecretKey secretKey = JwtUtil.generalKey(jwtProperties.getUserSecretKey());
        Student student = this.lambdaQuery().eq(Student::getEmail, email).one();
        //若student为空则存入新用户
        if(!Optional.ofNullable(student).isPresent()){
            student = getStudent(email);
            this.save(student);
        }
        //将id存入claims

        if (!Optional.ofNullable(student.getId()).isPresent()) {
            claims.put(UserInterpretor.USER_ID, student.getId());
        }

        String token = JwtUtil.createJWT(secretKey, jwtProperties.getUserTtl(), claims);
        LoginVO loginVO = LoginVO.builder()
                .access_token(token)
                .expires_in(jwtProperties.getUserTtl())
                .build();

        return loginVO;
    }

    private  Student getStudent(String email) {
        Student student;
        student=new Student();
        student.setAwards("");
        student.setExperience("");
        student.setIntroduce("");
        student.setReason("");
        student.setEmail(email);
        student.setVersion(1);
        student.setDeleted(0);
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        return student;
    }
}




