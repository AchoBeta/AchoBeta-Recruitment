package com.achobeta.domain.feishu.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:41
 */
@Data
public class FeishuGetUserIdDTO {

    @JsonProperty("mobiles")
    private List<String> mobiles;

    public static FeishuGetUserIdDTO of(String... mobiles) {
        return new FeishuGetUserIdDTO(){{
            setMobiles(List.of(mobiles));
        }};
    }

}
