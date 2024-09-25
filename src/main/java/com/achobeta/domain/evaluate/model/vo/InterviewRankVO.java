package com.achobeta.domain.evaluate.model.vo;

import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 19:21
 */
@Data
public class InterviewRankVO {

    private Long summaryId;

    private Integer average;

    private Long interviewId;

    private String title;

    private InterviewStatus status;

    private SimpleStudentVO simpleStudentVO;

}
