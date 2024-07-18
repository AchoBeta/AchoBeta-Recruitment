package com.achobeta.domain.student.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @date 2024/7/9
 */
@Data
public class QueryResumeOfUserDTO implements Serializable {
    //用户id
    @NotNull(message = "用户Id不能为空")
    Long userId;

    //版本号
    @NotNull(message = "版本号不能为空")
    Long batchId;
}
