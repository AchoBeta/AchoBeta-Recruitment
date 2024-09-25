package com.achobeta.domain.evaluate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.achobeta.config.DateTimeConfig.DATE_TIME_FORMAT;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 10:35
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewExperienceTemplate {

    private String studentId;

    private String title;

    private List<InterviewExperienceTemplateInner> inners;

    private Date startTime;

    private Date endTime;

    public String getStartTime() {
        return DATE_TIME_FORMAT.format(startTime);
    }

    public String getEndTime() {
        return DATE_TIME_FORMAT.format(endTime);
    }

}
