package com.achobeta.domain.feishu.model.dto;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 16:03
 */
@Data
public class FeishuResourceQueryDTO {

    private Integer current;

    private Integer pageSize;

    private String type;

}
