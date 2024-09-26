package com.achobeta.domain.feishu.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:43
 */
@Data
public class FeishuGetUserIdData {

    @JsonProperty("user_list")
    private List<FeishuGetUserIdUser> userList;

}
