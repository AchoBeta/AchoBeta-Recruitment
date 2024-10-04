package com.achobeta.domain.message.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-04
 * Time: 13:26
 */
@Data
public class QueryStuListVO {

    private Long total;

    private Long pages;

    private List<StuOfMessageVO> records;

}
