package com.achobeta.domain.feishu.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.domain.resource.util.MediaUtil;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.config.ResourceProperties;
import com.achobeta.feishu.constants.ObjectType;
import com.achobeta.feishu.token.FeishuTenantAccessToken;
import com.achobeta.feishu.util.FeishuRequestUtil;
import com.achobeta.util.GsonUtil;
import com.achobeta.util.TimeUtil;
import com.lark.oapi.Client;
import com.lark.oapi.service.contact.v3.enums.BatchGetIdUserUserIdTypeEnum;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserReqBody;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserResp;
import com.lark.oapi.service.contact.v3.model.BatchGetIdUserRespBody;
import com.lark.oapi.service.contact.v3.model.UserContactInfo;
import com.lark.oapi.service.drive.v1.enums.ImportTaskMountPointMountTypeEnum;
import com.lark.oapi.service.drive.v1.enums.JobStatusEnum;
import com.lark.oapi.service.drive.v1.enums.UploadAllFileParentTypeEnum;
import com.lark.oapi.service.drive.v1.enums.UploadAllMediaParentTypeEnum;
import com.lark.oapi.service.drive.v1.model.*;
import com.lark.oapi.service.vc.v1.enums.ApplyReserveUserIdTypeEnum;
import com.lark.oapi.service.vc.v1.model.ApplyReserveReqBody;
import com.lark.oapi.service.vc.v1.model.ApplyReserveResp;
import com.lark.oapi.service.vc.v1.model.ApplyReserveRespBody;
import com.lark.oapi.service.vc.v1.model.ReserveMeetingSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.Adler32;

import static com.achobeta.common.enums.HttpRequestEnum.*;
import static com.achobeta.feishu.constants.FeishuConstants.*;
import static com.lark.oapi.service.vc.v1.enums.ReserveMeetingSettingMeetingInitialTypeEnum.GROUP_MEETING;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:51
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FeishuServiceImpl implements FeishuService, InitializingBean {

    private final Client feishuClient;

    private final FeishuAppConfig feishuAppConfig;

    private final FeishuTenantAccessToken feishuTenantAccessToken;

    private String defaultOwnerId;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.defaultOwnerId = getUserIdByMobile(feishuAppConfig.getOwner().getMobile());
    }

    @Override
    public BatchGetIdUserRespBody batchGetUserId(BatchGetIdUserReqBody batchGetIdUserReqBody) {
//        try {
//            BatchGetIdUserReq batchGetIdUserReq = BatchGetIdUserReq.newBuilder()
//                    .userIdType(BatchGetIdUserUserIdTypeEnum.USER_ID)
//                    .batchGetIdUserReqBody(batchGetIdUserReqBody)
//                    .build();
//            BatchGetIdUserResp batchGetIdUserResp = feishuClient.contact()
//                    .user()
//                    .batchGetId(batchGetIdUserReq);
//            FeishuRequestUtil.checkResponse(batchGetIdUserResp);
//            return batchGetIdUserResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                GET_USER_ID,
                batchGetIdUserReqBody,
                BatchGetIdUserResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                Map.of(USER_ID_TYPE_QUERY_KEY, List.of(BatchGetIdUserUserIdTypeEnum.USER_ID.getValue()))
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
//        try {
//            ApplyReserveReq applyReserveReq = ApplyReserveReq.newBuilder()
//                    .userIdType(ApplyReserveUserIdTypeEnum.USER_ID)
//                    .applyReserveReqBody(applyReserveReqBody)
//                    .build();
//            ApplyReserveResp applyReserveResp = feishuClient.vc()
//                    .reserve()
//                    .apply(applyReserveReq);
//            FeishuRequestUtil.checkResponse(applyReserveResp);
//            return applyReserveResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                RESERVE_APPLY,
                applyReserveReqBody,
                ApplyReserveResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                Map.of(USER_ID_TYPE_QUERY_KEY, List.of(ApplyReserveUserIdTypeEnum.USER_ID.getValue()))
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

    @Override
    public UploadAllMediaRespBody uploadMedia(UploadAllMediaReqBody uploadAllMediaReqBody) {
        UploadAllMediaReq uploadAllMediaReq = UploadAllMediaReq.newBuilder().uploadAllMediaReqBody(uploadAllMediaReqBody).build();
        try {
            UploadAllMediaResp uploadAllMediaResp = feishuClient.drive().media().uploadAll(uploadAllMediaReq);
            FeishuRequestUtil.checkResponse(uploadAllMediaResp);
            return uploadAllMediaResp.getData();
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    @Override
    public UploadAllMediaRespBody uploadMediaBriefly(String originalName, byte[] bytes, ObjectType objectType) {
        return MediaUtil.createTempFileGetSomething(originalName, bytes, file -> {
            UploadAllMediaReqBody uploadAllMediaReqBody = UploadAllMediaReqBody.newBuilder()
                    .fileName(originalName)
                    .parentType(UploadAllMediaParentTypeEnum.CCM_IMPORT_OPEN)
                    .size(bytes.length)
                    .extra(GsonUtil.toJson(objectType))
                    .checksum(MediaUtil.getAdler32(bytes))
                    .file(file)
                    .build();
            return uploadMedia(uploadAllMediaReqBody);
        });
    }

    @Override
    public UploadAllFileRespBody uploadFile(UploadAllFileReqBody uploadAllFileReqBody) {
        UploadAllFileReq uploadAllFileRespBody = UploadAllFileReq.newBuilder().uploadAllFileReqBody(uploadAllFileReqBody).build();
        try {
            UploadAllFileResp uploadAllFileResp = feishuClient.drive().file().uploadAll(uploadAllFileRespBody);
            FeishuRequestUtil.checkResponse(uploadAllFileResp);
            return uploadAllFileResp.getData();
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    @Override
    public UploadAllFileRespBody uploadFileBriefly(String originalName, byte[] bytes, ObjectType objectType) {
        return MediaUtil.createTempFileGetSomething(originalName, bytes, file -> {
            UploadAllFileReqBody uploadAllFileReqBody = UploadAllFileReqBody.newBuilder()
                    .fileName(originalName)
                    .parentType(UploadAllFileParentTypeEnum.EXPLORER)
                    .parentNode(feishuAppConfig.getResource().getParentNode())
                    .size(bytes.length)
                    .checksum(MediaUtil.getAdler32(bytes))
                    .file(file)
                    .build();
            return uploadFile(uploadAllFileReqBody);
        });
    }

    @Override
    public CreateImportTaskRespBody importTask(ImportTask importTask) {
//        CreateImportTaskReq createImportTaskReq = CreateImportTaskReq.newBuilder()
//                .importTask(importTask)
//                .build();
//        try {
//            CreateImportTaskResp createImportTaskResp = feishuClient.drive().importTask().create(createImportTaskReq);
//            FeishuRequestUtil.checkResponse(createImportTaskResp);
//            return createImportTaskResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantAccessToken.getToken();
        return FeishuRequestUtil.request(
                IMPORT_TASK,
                importTask,
                CreateImportTaskResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                null
        ).getData();
    }

    @Override
    public CreateImportTaskRespBody importTaskBriefly(String originalName, byte[] bytes, ObjectType objectType) {
        String fileToken = uploadFileBriefly(originalName, bytes, objectType).getFileToken();
        ImportTask importTask = ImportTask.newBuilder()
                .fileName(originalName)
                .fileExtension(objectType.getFileExtension())
                .fileToken(fileToken)
                .type(objectType.getObjType())
                .point(ImportTaskMountPoint.newBuilder()
                        .mountType(ImportTaskMountPointMountTypeEnum.SPACE)
                        .mountKey(feishuAppConfig.getResource().getParentNode())
                        .build())
                .build();
        return importTask(importTask);
    }

    @Override
    public GetImportTaskRespBody getImportTask(String ticket) {
//        GetImportTaskReq getImportTaskReq = GetImportTaskReq.newBuilder().ticket(ticket).build();
//        try {
//            GetImportTaskResp getImportTaskResp = feishuClient.drive().importTask().get(getImportTaskReq);
//            FeishuRequestUtil.checkResponse(getImportTaskResp);
//            return getImportTaskResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantAccessToken.getToken();
        GetImportTaskRespBody importTaskRespBody = FeishuRequestUtil.request(
                GET_IMPORT_TASK,
                null,
                GetImportTaskResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                null,
                Map.of(MEDIA_TICKET_QUERY_KEY, ticket)
        ).getData();

        return importTaskRespBody;
    }

    @Override
    public ImportTask getImportTaskPolling(String ticket) {
        try {
            // 参数信息
            ResourceProperties resource = feishuAppConfig.getResource();
            Integer maxCount = resource.getMaxCount();
            long sleepTime = resource.getTryAgainUnit().toMillis(resource.getTryAgain());
            // 第一次尝试
            int count = 1;
            ImportTask importTask = getImportTask(ticket).getResult();
            // 轮询直到处理完为止（最多 maxAgainCount）
            while (JobStatusEnum.PROCESSING.getValue().equals(importTask.getJobStatus()) && maxCount.compareTo(count) > 0) {
                log.warn("{} {}", importTask.getJobStatus(), importTask.getJobErrorMsg());
                Thread.sleep(sleepTime);
                importTask = getImportTask(ticket).getResult();
                count++;
            }
            // 循环结束直接返回
            return importTask;
        } catch (InterruptedException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
