package com.achobeta.domain.feishu.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.token.FeishuTenantAccessToken;
import com.achobeta.feishu.util.FeishuRequestUtil;
import com.achobeta.util.TimeUtil;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserReqBody;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserResp;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserRespBody;
import com.lark.oapi.service.contact.v3.model.UserContactInfo;
import com.lark.oapi.service.vc.v1.model.ApplyReserveReqBody;
import com.lark.oapi.service.vc.v1.model.ApplyReserveResp;
import com.lark.oapi.service.vc.v1.model.ApplyReserveRespBody;
import com.lark.oapi.service.vc.v1.model.ReserveMeetingSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.achobeta.feishu.constants.FeishuConstants.*;
import static com.lark.oapi.service.contact.v3.enums.BatchGetIdUserUserIdTypeEnum.USER_ID;
import static com.lark.oapi.service.vc.v1.enums.ReserveMeetingSettingMeetingInitialTypeEnum.GROUP_MEETING;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:51
 */
@Service
@RequiredArgsConstructor
public class FeishuServiceImpl implements FeishuService, InitializingBean {

    private final FeishuAppConfig feishuAppConfig;

    private final FeishuTenantAccessToken feishuTenantAccessToken;

    private String defaultOwnerId;

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultOwnerId = getUserIdByMobile(feishuAppConfig.getOwner().getMobile());
    }

    @Override
    public BatchGetIdUserRespBody batchGetUserId(BatchGetIdUserReqBody batchGetIdUserReqBody) {
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                HttpRequestEnum.GET_USER_ID,
                batchGetIdUserReqBody,
                BatchGetIdUserResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                Map.of(USER_ID_TYPE_QUERY_KEY, List.of(USER_ID.getValue()))
        ).getData();
    }

    @Override
    public BatchGetIdUserRespBody batchGetUserIdByMobiles(String... mobiles) {
        BatchGetIdUserReqBody batchGetIdUserReqBody = BatchGetIdUserReqBody.newBuilder()
                .mobiles(Optional.ofNullable(mobiles).orElseGet(() -> new String[0]))
                .build();
        return batchGetUserId(batchGetIdUserReqBody);
    }

    @Override
    public String getDefaultOwnerId() {
        return defaultOwnerId;
    }

    @Override
    public String getUserIdByMobile(String mobile) {
        if(!StringUtils.hasText(mobile)) {
            return getDefaultOwnerId();
        }
        UserContactInfo[] userList = batchGetUserIdByMobiles(mobile).getUserList();
        return ArrayUtil.isEmpty(userList) ? null : userList[0].getUserId();
    }

    @Override
    public ApplyReserveRespBody reserveApply(ApplyReserveReqBody applyReserveReqBody) {
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                HttpRequestEnum.RESERVE_APPLY,
                applyReserveReqBody,
                ApplyReserveResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                Map.of(USER_ID_TYPE_QUERY_KEY, List.of(USER_ID.getValue()))
        ).getData();
    }

    @Override
    public ApplyReserveRespBody reserveApplyBriefly(String ownerId, Long endTime, String topic) {
        ApplyReserveReqBody reserveReqBody = ApplyReserveReqBody.newBuilder()
                .endTime(String.valueOf(TimeUtil.millisToSecond(endTime)))
                .ownerId(ownerId)
                .meetingSettings(ReserveMeetingSetting.newBuilder().topic(topic).meetingInitialType(GROUP_MEETING).build())
                .build();
        return reserveApply(reserveReqBody);
    }
}
