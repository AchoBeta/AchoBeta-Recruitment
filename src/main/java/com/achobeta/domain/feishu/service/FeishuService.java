package com.achobeta.domain.feishu.service;

import com.achobeta.domain.feishu.model.dto.FeishuGetUserIdDTO;
import com.achobeta.domain.feishu.model.dto.FeishuScheduleDTO;
import com.achobeta.domain.feishu.model.vo.FeishuGetUserIdResponse;
import com.achobeta.domain.feishu.model.vo.FeishuScheduleResponse;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:22
 */
public interface FeishuService {

    FeishuGetUserIdResponse getUserId(FeishuGetUserIdDTO feishuGetUserIdDTO);

    String getOwnerId();

    FeishuScheduleResponse reserveApply(FeishuScheduleDTO feishuScheduleDTO);

}
