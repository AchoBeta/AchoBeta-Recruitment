package com.achobeta.domain.feishu.model.vo;

import com.achobeta.feishu.response.FeishuResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:42
 */
@Data
public class FeishuGetUserIdResponse extends FeishuResponse {

    @JsonProperty("data")
    private FeishuGetUserIdData data;

}
