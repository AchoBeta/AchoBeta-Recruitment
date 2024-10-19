package com.achobeta.domain.feishu.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.config.ResourceProperties;
import com.achobeta.feishu.constants.ObjectType;
import com.achobeta.feishu.request.FeishuRequestEngine;
import com.achobeta.feishu.token.FeishuTenantSession;
import com.achobeta.util.GsonUtil;
import com.achobeta.util.MediaUtil;
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
import com.lark.oapi.service.vc.v1.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private final FeishuTenantSession feishuTenantSession;

    private final FeishuRequestEngine feishuRequestEngine;

    private String defaultOwnerId;

    private Boolean backup;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.defaultOwnerId = getUserIdByMobile(feishuAppConfig.getOwner().getMobile());
        this.backup = Optional.ofNullable(feishuAppConfig.getResource().getBackup()).orElse(Boolean.FALSE);
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
//            feishuRequestEngine.checkResponse(batchGetIdUserResp);
//            return batchGetIdUserResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantSession.getToken();
        return feishuRequestEngine.jsonRequest(
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
//            feishuRequestEngine.checkResponse(applyReserveResp);
//            return applyReserveResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantSession.getToken();
        return feishuRequestEngine.jsonRequest(
                RESERVE_APPLY,
                applyReserveReqBody,
                ApplyReserveResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                Map.of(USER_ID_TYPE_QUERY_KEY, List.of(ApplyReserveUserIdTypeEnum.USER_ID.getValue()))
        ).getData();
    }

    @Override
    public ApplyReserveRespBody reserveApplyBriefly(String ownerId, Long endTime, String topic) {
        // 这里预约的会议不会出现在日历里
        // 其中，是否自动录制（云录制，无法自动本地录制），不会根据飞书管理后台的全局配置中的是否自动录制，在飞书网页/软件申请的会议根据的才会用到这个全局配置
        // 而我们这次的请求参数若不设置，默认为 false（局部优先），设置为 true 后，若允许云录制会议有若的时候就会自动录制
        ApplyReserveReqBody reserveReqBody = ApplyReserveReqBody.newBuilder()
                .endTime(String.valueOf(TimeUtil.millisToSecond(endTime)))
                .ownerId(ownerId)
                .meetingSettings(ReserveMeetingSetting.newBuilder().topic(topic)
//                        .autoRecord(Boolean.TRUE)
                        .meetingInitialType(GROUP_MEETING).build())
                .build();
        return reserveApply(reserveReqBody);
    }

    @Override
    public UploadAllMediaRespBody uploadMedia(UploadAllMediaReqBody uploadAllMediaReqBody) {
        UploadAllMediaReq uploadAllMediaReq = UploadAllMediaReq.newBuilder().uploadAllMediaReqBody(uploadAllMediaReqBody).build();
        try {
            UploadAllMediaResp uploadAllMediaResp = feishuClient.drive().media().uploadAll(uploadAllMediaReq);
            feishuRequestEngine.checkResponse(uploadAllMediaResp);
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
            feishuRequestEngine.checkResponse(uploadAllFileResp);
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
                    .parentNode(objectType.getParentNode())
                    .size(bytes.length)
                    .checksum(MediaUtil.getAdler32(bytes))
                    .file(file)
                    .build();
            return uploadFile(uploadAllFileReqBody);
        });
    }

    @Override
    public String getFileTokenBriefly(String originalName, byte[] bytes, ObjectType objectType) {
        if(Boolean.TRUE.equals(backup)) {
            return uploadFileBriefly(originalName, bytes, objectType).getFileToken();
        }else {
            return uploadMediaBriefly(originalName, bytes, objectType).getFileToken();
        }
    }

    @Override
    public CreateImportTaskRespBody importTask(ImportTask importTask) {
//        CreateImportTaskReq createImportTaskReq = CreateImportTaskReq.newBuilder()
//                .importTask(importTask)
//                .build();
//        try {
//            CreateImportTaskResp createImportTaskResp = feishuClient.drive().importTask().create(createImportTaskReq);
//            feishuRequestEngine.checkResponse(createImportTaskResp);
//            return createImportTaskResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantSession.getToken();
        return feishuRequestEngine.jsonRequest(
                IMPORT_TASK,
                importTask,
                CreateImportTaskResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                null
        ).getData();
    }

    @Override
    public CreateImportTaskRespBody importTaskBriefly(String originalName, byte[] bytes, ObjectType objectType) {
        String fileToken = getFileTokenBriefly(originalName, bytes, objectType);
        ImportTask importTask = ImportTask.newBuilder()
                .fileName(originalName)
                .fileExtension(objectType.getFileExtension())
                .fileToken(fileToken)
                .type(objectType.getObjType())
                .point(ImportTaskMountPoint.newBuilder()
                        .mountType(ImportTaskMountPointMountTypeEnum.SPACE)
                        .mountKey(objectType.getMountKey())
                        .build())
                .build();
        return importTask(importTask);
    }

    @Override
    public GetImportTaskRespBody getImportTask(String ticket) {
//        GetImportTaskReq getImportTaskReq = GetImportTaskReq.newBuilder().ticket(ticket).build();
//        try {
//            GetImportTaskResp getImportTaskResp = feishuClient.drive().importTask().get(getImportTaskReq);
//            feishuRequestEngine.checkResponse(getImportTaskResp);
//            return getImportTaskResp.getData();
//        } catch (Exception e) {
//            throw new GlobalServiceException(e.getMessage());
//        }
        String token = feishuTenantSession.getToken();
        return feishuRequestEngine.jsonRequest(
                GET_IMPORT_TASK,
                null,
                GetImportTaskResp.class,
                Map.of(AUTHORIZATION_HEADER, getAuthorization(token)),
                null,
                ticket
        ).getData();
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
                log.warn("任务状态 {}，错误信息 {}", importTask.getJobStatus(), importTask.getJobErrorMsg());
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
