package com.achobeta.common.base;

import lombok.Data;

@Data
public class BasePageQueryEntity {

    /*页码*/
    Integer pageNo;

    /*页面大小*/
    Integer pageSize;

}