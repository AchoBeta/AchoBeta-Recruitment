package com.achobeta.domain.feishu.service.impl;

import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.feishu.token.FeishuTenantAccessToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 9:47
 */
@SpringBootTest(args = "--mpw.key=aAcChHoObBeEtTaA")
class FeishuServiceImplTest {

    @Autowired
    private FeishuService feishuService;

    @Autowired
    private FeishuTenantAccessToken feishuTenantAccessToken;

    @Test
    void getOwnerId() {
//        System.out.println(feishuTenantAccessToken.getToken());
//        System.out.println(feishuService.getDefaultOwnerId());
//        System.out.println(feishuService.getUserIdByMobile("15362558972"));
//        ApplyReserveRespBody body = feishuService.reserveApplyBriefly(feishuService.getDefaultOwnerId(), 1727625600000L, "马大帅的面试");
//        System.out.println(JSONUtil.parse(body.getReserve()).toStringPretty());
        System.out.println(feishuService.sheetResult("7419519182875820060"));
    }
}