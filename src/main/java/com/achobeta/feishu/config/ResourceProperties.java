package com.achobeta.feishu.config;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 16:51
 */
@Data
public class ResourceProperties {

    @SerializedName("parent_node")
    private String parentNode;

}
