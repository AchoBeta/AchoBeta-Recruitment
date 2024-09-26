package com.achobeta.domain.feishu.service.impl;

import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.domain.feishu.model.dto.FeishuGetUserIdDTO;
import com.achobeta.domain.feishu.model.dto.FeishuScheduleDTO;
import com.achobeta.domain.feishu.model.vo.FeishuGetUserIdResponse;
import com.achobeta.domain.feishu.model.vo.FeishuScheduleResponse;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.feishu.token.FeishuTenantAccessToken;
import com.achobeta.feishu.util.FeishuRequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.achobeta.feishu.constants.FeishuConstants.AUTHORIZATION_HEADER;
import static com.achobeta.feishu.constants.FeishuConstants.getAuthorization;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:51
 */
@Service
@RequiredArgsConstructor
public class FeishuServiceImpl implements FeishuService {

    @Value("${ab.feishu.owner.mobile}")
    private String ownerMobile;

    private final FeishuTenantAccessToken feishuTenantAccessToken;

    @Override
    public FeishuGetUserIdResponse getUserId(FeishuGetUserIdDTO feishuGetUserIdDTO) {
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                HttpRequestEnum.GET_USER_ID,
                feishuGetUserIdDTO,
                FeishuGetUserIdResponse.class,
                Map.of(
                        AUTHORIZATION_HEADER, getAuthorization(token)
                )
        );
    }

    @Override
    public String getOwnerId() {
        return getUserId(FeishuGetUserIdDTO.of(ownerMobile)).getData()
                .getUserList()
                .getFirst()
                .getUserId();
    }

    @Override
    public FeishuScheduleResponse reserveApply(FeishuScheduleDTO feishuScheduleDTO) {
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                HttpRequestEnum.RESERVE_APPLY,
                feishuScheduleDTO,
                FeishuScheduleResponse.class,
                Map.of(
                        AUTHORIZATION_HEADER, getAuthorization(token)
                )
        );
    }


}
