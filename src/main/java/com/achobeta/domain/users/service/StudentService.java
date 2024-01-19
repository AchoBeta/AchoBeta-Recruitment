package com.achobeta.domain.users.service;

import com.achobeta.domain.users.model.po.Student;
import com.achobeta.domain.users.model.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author cattleyuan
* @description 针对表【student(学生用户简历表)】的数据库操作Service
* @createDate 2024-01-19 19:05:21
*/
public interface StudentService extends IService<Student> {

    LoginVO saveStudentByEmail(String email);
}
