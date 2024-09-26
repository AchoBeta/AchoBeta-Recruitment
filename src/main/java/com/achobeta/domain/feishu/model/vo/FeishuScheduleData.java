package com.achobeta.domain.feishu.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:12
 */
@Data
public class FeishuScheduleData {

    @JsonProperty("reserve")
    private FeishuReserve reserve;

    @JsonProperty("reserve_correction_check_info")
    private ReserveCorrectionCheckInfo reserveCorrectionCheckInfo;

}
