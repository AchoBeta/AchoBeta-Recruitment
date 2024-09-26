package com.achobeta.domain.feishu.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:08
 */
@Data
public class FeishuMeetingSettings {

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("meeting_initial_type")
    private Integer meetingInitialType;

    @JsonProperty("auto_record")
    private Boolean autoRecord;

}
