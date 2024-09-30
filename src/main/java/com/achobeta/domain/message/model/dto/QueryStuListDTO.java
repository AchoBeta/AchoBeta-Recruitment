package com.achobeta.domain.message.model.dto;

import com.achobeta.common.base.BasePageQueryEntity;
import com.achobeta.common.base.BasePageResultEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/12
 */
@Getter
@Setter
public class QueryStuListDTO extends BasePageQueryEntity implements Serializable {
    /*招新批次*/
    @NotNull(message = "招新批次不能为空")
    Integer batchId;
    /*年级*/
    Integer grade;

    /*简历状态*/
    Integer status;
    /*学生名称*/
    String name;
}
