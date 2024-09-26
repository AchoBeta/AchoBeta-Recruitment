package com.achobeta.domain.interview.model.vo;

import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 14:00
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewNoticeTemplate {

    private String studentId;

    private String title;

    private String description;

    private String address;

    private Date startTime;

    private Date endTime;

    private InterviewStatus status;

    public String getStartTime() {
        return TimeUtil.getDateTime(startTime);
    }

    public String getEndTime() {
        return TimeUtil.getDateTime(endTime);
    }

    public String getStatus() {
        return status.getDescription();
    }
}
