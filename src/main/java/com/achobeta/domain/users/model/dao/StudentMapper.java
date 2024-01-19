package com.achobeta.domain.users.model.dao;

import com.achobeta.domain.users.model.po.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author cattleyuan
* @description 针对表【student(学生用户简历表)】的数据库操作Mapper
* @createDate 2024-01-19
* @Entity com.achobeta.domain.users.model.po.Student
*/
public interface StudentMapper extends BaseMapper<Student> {

}




