package com.achobeta.domain.message.model.dto;

import com.achobeta.common.enums.ResumeStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/12
 */
@Getter
@Setter
public class StuOfMessageVO implements Serializable {
    private Long userId;

    private String name;

    private Integer gender;

    private Integer grade;

    private ResumeStatus status;

}
