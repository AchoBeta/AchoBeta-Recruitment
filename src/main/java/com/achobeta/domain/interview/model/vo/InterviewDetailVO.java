package com.achobeta.domain.interview.model.vo;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-06
 * Time: 3:09
 */
@Data
public class InterviewDetailVO extends InterviewVO {

    private Long stuId;

    private String description;

    private String address;

    public void hidden() {
        setStuId(null);
    }

}
