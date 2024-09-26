package com.achobeta.domain.feishu.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.token.FeishuTenantAccessToken;
import com.achobeta.feishu.util.FeishuRequestUtil;
import com.lark.oapi.service.contact.v3.model.*;
import com.lark.oapi.service.vc.v1.model.ApplyReserveReqBody;
import com.lark.oapi.service.vc.v1.model.ApplyReserveResp;
import com.lark.oapi.service.vc.v1.model.ApplyReserveRespBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private final FeishuAppConfig feishuAppConfig;

    private final FeishuTenantAccessToken feishuTenantAccessToken;

    @Override
    public BatchGetIdUserRespBody batchGetUserId(BatchGetIdUserReqBody batchGetIdUserReqBody) {
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                HttpRequestEnum.GET_USER_ID,
                batchGetIdUserReqBody,
                BatchGetIdUserResp.class,
                Map.of(
                        AUTHORIZATION_HEADER, getAuthorization(token)
                )
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
    public String getUserIdByMobile(String mobile) {
        UserContactInfo[] userList = batchGetUserIdByMobiles(mobile).getUserList();
        return ArrayUtil.isEmpty(userList) ? null : userList[0].getUserId();
    }

    @Override
    public String getOwnerId() {
        return getUserIdByMobile(feishuAppConfig.getOwner().getMobile());
    }

    @Override
    public ApplyReserveRespBody reserveApply(ApplyReserveReqBody applyReserveReqBody) {
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                HttpRequestEnum.RESERVE_APPLY,
                applyReserveReqBody,
                ApplyReserveResp.class,
                Map.of(
                        AUTHORIZATION_HEADER, getAuthorization(token)
                )
        ).getData();
    }


}
