package com.achobeta.domain.resource.model.dto;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 11:19
 */
@Data
public class ResourceQueryDTO {

    private Integer current;

    private Integer pageSize;

    private Long userId;

    private Integer level;

}
