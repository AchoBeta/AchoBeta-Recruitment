package com.achobeta.domain.recruit.model.vo;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-26
 * Time: 3:40
 */
@Data
public class TimePeriodCountVO extends TimePeriodVO {

    private Integer count;

    public void increment() {
        this.count++;
    }

}
