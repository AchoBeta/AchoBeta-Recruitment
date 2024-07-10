package com.achobeta.domain.student.model.dto;

import lombok.Data;

/**
 * @author cattleYuan
 * @date 2024/7/10
 */
@Data
public class QueryResumeDTO {

    //查询个人简历
    QueryResumeOfUserDTO queryResumeOfUserDTO;
    //简历id
    Long resumeId;
}
