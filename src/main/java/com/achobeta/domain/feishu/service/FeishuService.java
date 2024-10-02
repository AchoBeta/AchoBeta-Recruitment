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

    // 此接口若 mobile 不是合作伙伴，则对应的 userId、email、status 都为 null
    BatchGetIdUserRespBody batchGetUserIdByMobiles(String... mobiles);

    String getUserIdByMobile(String mobile);

    String getDefaultOwnerId();

    ApplyReserveRespBody reserveApply(ApplyReserveReqBody applyReserveReqBody);

    ApplyReserveRespBody reserveApplyBriefly(String ownerId, Long endTime, String topic);

    /**
     * 上传资源（云空间，不存储）
     */
    UploadAllMediaRespBody uploadMedia(UploadAllMediaReqBody uploadAllMediaReqBody);

    UploadAllMediaRespBody uploadMediaBriefly(String originalName, byte[] bytes, ObjectType objectType);

    /**
     * 上传文件（存储到指定目录）
     */
    UploadAllFileRespBody uploadFile(UploadAllFileReqBody uploadAllFileReqBody);

    UploadAllFileRespBody uploadFileBriefly(String originalName, byte[] bytes, ObjectType objectType);

    String getFileTokenBriefly(String originalName, byte[] bytes, ObjectType objectType);

    /**
     * 创建导入任务，指定挂载的链接
     */
    CreateImportTaskRespBody importTask(ImportTask importTask);

    CreateImportTaskRespBody importTaskBriefly(String originalName, byte[] bytes, ObjectType objectType);

    /**
     * 查询导入结果
     * 根据 ticket 查询会创建一个额外的协作链接，这个与原本不同的点就是，原本那个是存储，现在这个是协作文件（一般不会共享存储的文件）
     * 这个额外的协作链接生成具有飞书风格的协作文件（加载也会快点，更适合协作）
     */
    GetImportTaskRespBody getImportTask(String ticket);

    ImportTask getImportTaskPolling(String ticket);
}
