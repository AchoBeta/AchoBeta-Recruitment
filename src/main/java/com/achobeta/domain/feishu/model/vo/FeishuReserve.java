package com.achobeta.domain.feishu.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:13
 */
@Data
public class FeishuReserve {

    @JsonProperty("id")
    private String id;

    @JsonProperty("meeting_no")
    private String meetingNo;

    @JsonProperty("password")
    private String password;

    @JsonProperty("url")
    private String url;

    @JsonProperty("app_link")
    private String appLink;

    @JsonProperty("live_link")
    private String liveLink;

    @JsonProperty("end_time")
    private String endTime;

}
