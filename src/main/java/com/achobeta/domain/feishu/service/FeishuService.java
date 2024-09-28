package com.achobeta.domain.feishu.service;

import com.achobeta.feishu.constants.ObjectType;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserReqBody;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserRespBody;
import com.lark.oapi.service.drive.v1.model.*;
import com.lark.oapi.service.vc.v1.model.ApplyReserveReqBody;
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

    String getDefaultOwnerId();

    ApplyReserveRespBody reserveApply(ApplyReserveReqBody applyReserveReqBody);

    ApplyReserveRespBody reserveApplyBriefly(String ownerId, Long endTime, String topic);

    UploadAllMediaRespBody uploadMedia(UploadAllMediaReqBody uploadAllMediaReqBody);

    UploadAllMediaRespBody uploadMediaBriefly(String originalName, byte[] bytes, ObjectType objectType);

    UploadAllFileRespBody uploadFile(UploadAllFileReqBody uploadAllFileReqBody);

    UploadAllFileRespBody uploadFile(String originalName, byte[] bytes, ObjectType objectType);

    CreateImportTaskRespBody importTask(ImportTask importTask);

    CreateImportTaskRespBody importTaskBriefly(String originalName, byte[] bytes, ObjectType objectType);

    GetImportTaskRespBody getImportTask(String ticket);

    GetImportTaskRespBody getImportTaskBriefly(String originalName, byte[] bytes, ObjectType objectType);
}
