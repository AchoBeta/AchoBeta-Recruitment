package com.achobeta.domain.feishu.service;

import com.lark.oapi.service.contact.v3.model.BatchGetIdUserReqBody;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserRespBody;
import com.lark.oapi.service.vc.v1.model.ApplyReserveReq;
import com.lark.oapi.service.vc.v1.model.ApplyReserveReqBody;
import com.lark.oapi.service.vc.v1.model.ApplyReserveResp;
import com.lark.oapi.service.vc.v1.model.ApplyReserveRespBody;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:22
 */
public interface FeishuService {

    BatchGetIdUserRespBody batchGetUserId(BatchGetIdUserReqBody batchGetIdUserReqBody);

    BatchGetIdUserRespBody batchGetUserIdByMobiles(String... mobiles);

    String getUserIdByMobile(String mobile);

    String getOwnerId();

    ApplyReserveRespBody reserveApply(ApplyReserveReqBody applyReserveReqBody);

}
