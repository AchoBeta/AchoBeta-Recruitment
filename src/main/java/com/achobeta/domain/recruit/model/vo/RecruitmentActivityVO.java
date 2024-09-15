package com.achobeta.domain.recruit.model.vo;

import com.achobeta.domain.recruit.model.condition.StudentGroup;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-07-06
 * Time: 23:40
 */
@Data
public class RecruitmentActivityVO {

    private Long id;

    private Long paperId;

    private StudentGroup target;

    private String title;

    private String description;

    private Boolean isRun;

    private Date deadline;

    private LocalDateTime createTime;

    public void hidden() {
        setPaperId(null);
        setTarget(null);
        setIsRun(null);
    }
}
