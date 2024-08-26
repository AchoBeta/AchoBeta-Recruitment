package com.achobeta.domain.evaluate.model.vo;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 18:17
 */
@Data
public class InterviewSummaryVO {

    private Long id;

    private Long interviewId;

    private Integer basis;

    private Integer coding;

    private Integer thinking;

    private Integer express;

    private String evaluate;

    private String suggest;

    private String playback;

    public void hidden() {

    }

}
