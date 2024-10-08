package com.achobeta.domain.paper.model.dto;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 11:25
 */
@Data
public class PaperQueryDTO {

    private Integer current;

    private Integer pageSize;

    private List<Long> libIds;

}
