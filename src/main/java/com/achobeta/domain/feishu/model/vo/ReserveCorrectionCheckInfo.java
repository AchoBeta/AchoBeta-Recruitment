package com.achobeta.domain.feishu.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:15
 */
@Data
public class ReserveCorrectionCheckInfo {

    @JsonProperty("invalid_host_id_list")
    private List<String> invalidHostIdList;

}
