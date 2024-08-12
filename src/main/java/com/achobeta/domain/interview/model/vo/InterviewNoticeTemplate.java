package com.achobeta.domain.interview.model.vo;

import com.achobeta.common.enums.InterviewStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
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

    private InterviewStatusEnum status;

    public String getStartTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
    }

    public String getEndTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
    }

    public String getStatus() {
        return status.getDescription();
    }
}
