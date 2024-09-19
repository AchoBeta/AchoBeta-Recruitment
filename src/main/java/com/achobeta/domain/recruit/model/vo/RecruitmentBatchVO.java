package com.achobeta.domain.recruit.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 19:18
 */
@Data
public class RecruitmentBatchVO {

    private Long id;

    private Integer batch;

    private String title;

    private Date deadline;

    private Boolean isRun;

}
