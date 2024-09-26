package com.achobeta.domain.feishu.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:07
 */
@Data
public class FeishuScheduleDTO {

    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("owner_id")
    private String ownerId;

    @JsonProperty("meeting_settings")
    private FeishuMeetingSettings meetingSettings;

}
