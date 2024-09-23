package com.achobeta.domain.resource.model.vo;

import com.achobeta.common.enums.ResourceAccessLevel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 2:04
 */
@Data
public class DigitalResourceVO {

    private Long id;

    private Long code;

    private Long userId;

    private ResourceAccessLevel accessLevel;

    private String originalName;

//    private String fileName;

    protected LocalDateTime createTime;

    private LocalDateTime updateTime;

}
