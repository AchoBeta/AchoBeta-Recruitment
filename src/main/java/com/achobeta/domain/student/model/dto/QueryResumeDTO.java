package com.achobeta.domain.student.model.dto;

import lombok.Data;

import javax.validation.Valid;

/**
 * @author cattleYuan
 * @date 2024/7/10
 */
@Data
public class QueryResumeDTO {

    //查询个人简历
    @Valid
    QueryResumeOfUserDTO queryResumeOfUserDTO;
    //简历id
    Long resumeId;
}
