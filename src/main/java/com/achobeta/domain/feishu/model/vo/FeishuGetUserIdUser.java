package com.achobeta.domain.feishu.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:43
 */
@Data
public class FeishuGetUserIdUser {

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("user_id")
    private String userId;

}
