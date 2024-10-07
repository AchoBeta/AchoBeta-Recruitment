package com.achobeta.domain.message.model.vo;


import com.achobeta.domain.resumestate.enums.ResumeStatus;
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

    private String email;

    private ResumeStatus status;

}
