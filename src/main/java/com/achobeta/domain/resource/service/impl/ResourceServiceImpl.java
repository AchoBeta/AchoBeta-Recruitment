package com.achobeta.domain.resource.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.feishu.model.entity.FeishuResource;
import com.achobeta.domain.feishu.service.FeishuResourceService;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.domain.resource.access.strategy.ResourceAccessStrategy;
import com.achobeta.domain.resource.config.UploadLimitProperties;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.enums.ExcelTemplateEnum;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.factory.AccessStrategyFactory;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.domain.resource.service.DigitalResourceService;
import com.achobeta.domain.resource.service.ObjectStorageService;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.domain.resource.util.ExcelUtil;
import com.achobeta.domain.resource.util.MediaUtil;
import com.achobeta.domain.resource.util.ResourceUtil;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.constants.ObjectType;
import com.achobeta.redis.cache.RedisCache;
import com.achobeta.util.HttpRequestUtil;
import com.achobeta.util.HttpServletUtil;
import com.achobeta.util.TimeUtil;
import com.lark.oapi.service.drive.v1.model.ImportTask;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.achobeta.domain.resource.constants.ResourceConstants.DEFAULT_RESOURCE_ACCESS_LEVEL;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:17
 */
@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ResourceServiceImpl implements ResourceService {

    private final UploadLimitProperties uploadLimitProperties;

    private final AccessStrategyFactory accessStrategyFactory;

    private final DigitalResourceService digitalResourceService;

    private final ObjectStorageService objectStorageService;

    private final FeishuService feishuService;

    private final FeishuResourceService feishuResourceService;

    private final RedisCache redisCache;

    @Override
    public DigitalResource checkAndGetResource(Long code, ResourceAccessLevel level) {
        DigitalResource resource = digitalResourceService.getResourceByCode(code);
        // 获取策略并判断是否可以访问（level 取最高的那个）
        ResourceAccessStrategy accessStrategy = accessStrategyFactory.getStrategy(resource.getAccessLevel().and(level));
        if (accessStrategy.isAccessible(resource)) {
            return resource;
        } else {
            throw new GlobalServiceException(
                    String.format("尝试访问资源 %s，已阻拦", resource.getCode()),
                    GlobalServiceStatusCode.RESOURCE_CANNOT_BE_ACCESSED
            );
        }
    }

    @Override
    public DigitalResource analyzeCode(Long code) {
        return checkAndGetResource(code, null);
    }

    @Override
    public void download(Long code, HttpServletResponse response) {
        DigitalResource resource = analyzeCode(code);
        objectStorageService.download(resource.getOriginalName(), resource.getFileName(), response);
    }

    @Override
    public void preview(Long code, HttpServletResponse response) {
        DigitalResource resource = analyzeCode(code);
        objectStorageService.preview(resource.getFileName(), response);
    }

    @Override
    public byte[] load(Long code) {
        DigitalResource resource = analyzeCode(code);
        return objectStorageService.load(resource.getFileName());
    }

    @Override
    public void checkImage(Long code) {
        // 加载资源
        byte[] bytes = load(code);
        // 判断是否为图片
        ResourceUtil.checkImage(MediaUtil.getContentType(bytes));
    }

    @Override
    @Transactional
    public void checkAndRemoveImage(Long code, Long old) {
        if(!code.equals(old)) {
            checkImage(code);
            removeKindly(old);
        }
    }

    @Override
    public String getSystemUrl(HttpServletRequest request, Long code) {
        String baseUrl = HttpServletUtil.getBaseUrl(request, "/api/v1/resource/download", "/{code}");
        return HttpRequestUtil.buildUrl(baseUrl, null, code);
    }

    @Override
    public String gerObjectUrl(Long code, Boolean hidden) {
        DigitalResource resource = analyzeCode(code);
        return objectStorageService.getObjectUrl(resource.getFileName(), hidden);
    }

    @Override
    public Long upload(Long userId, String originalName, byte[] data, ResourceAccessLevel level) {
        checkBlockUser(userId);
        // 进行上传次数的检测
        String redisKey = ResourceConstants.REDIS_USER_UPLOAD_LIMIT + userId;
        // 获得已上传的次数
        Integer times = (Integer) redisCache.getCacheObject(redisKey).orElseGet(() -> {
            redisCache.setCacheObject(redisKey, 0, uploadLimitProperties.getCycle(), uploadLimitProperties.getUnit());
            return 0;
        });
        // 判断次数是否达到上限
        if(uploadLimitProperties.getFrequency().compareTo(times) <= 0) {
            throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_UPLOAD_TOO_FREQUENT);
        }
        DigitalResource resource = new DigitalResource();
        resource.setUserId(userId);
        resource.setOriginalName(originalName);
        resource.setFileName(objectStorageService.upload(userId, originalName, data));
        resource.setAccessLevel(Optional.ofNullable(level).orElse(DEFAULT_RESOURCE_ACCESS_LEVEL));
        Long code = digitalResourceService.createResource(resource).getCode();
        // 自增
        redisCache.incrementCacheNumber(redisKey);
        return code;
    }

    @Override
    public Long upload(Long userId, MultipartFile file, ResourceAccessLevel level) {
        return upload(userId, ResourceUtil.getOriginalName(file), MediaUtil.getBytes(file), level);
    }

    @Override
    public Long upload(Long userId, MultipartFile file) {
        return upload(userId, file, DEFAULT_RESOURCE_ACCESS_LEVEL);
    }

    @Override
    @Transactional
    public OnlineResourceVO synchronousFeishuUpload(Long managerId, String originalName, byte[] bytes, ResourceAccessLevel level, ObjectType objectType, Boolean synchronous) {
        String fileName = ResourceUtil.forceChangeExtension(originalName, objectType.getFileExtension());
        OnlineResourceVO onlineResourceVO = new OnlineResourceVO();
        // 上传对象存储系统
        Long code = upload(managerId, fileName, bytes, level);
        HttpServletUtil.getRequest().ifPresent(request -> {
            onlineResourceVO.setDownloadUrl(getSystemUrl(request, code));
            // 是否同步飞书文档
            if(Boolean.TRUE.equals(synchronous)) {
                try {
                    String ticket = feishuService.importTaskBriefly(fileName, bytes, objectType).getTicket();
                    FeishuResource resource = feishuResourceService.createAndGetFeishuResource(ticket, fileName);
                    ImportTask importTask = feishuService.getImportTaskPolling(ticket);
                    feishuResourceService.updateFeishuResource(resource.getId(), importTask);
                    onlineResourceVO.setFeishuUrl(feishuResourceService.getSystemUrl(request, ticket));
                } catch (GlobalServiceException e) {
                    log.warn("{} {}", e.getStatusCode(), e.getMessage());
                }
            }

        });
        return onlineResourceVO;
    }

    @Override
    @Transactional
    public OnlineResourceVO synchronousFeishuUpload(Long managerId, MultipartFile file, ResourceAccessLevel level, ObjectType objectType, Boolean synchronous) {
        return synchronousFeishuUpload(managerId, ResourceUtil.getOriginalName(file), MediaUtil.getBytes(file), level, objectType, synchronous);
    }

    @Override
    @Transactional
    public <E> OnlineResourceVO uploadExcel(Long managerId, ExcelTemplateEnum excelTemplateEnum, Class<E> clazz, List<E> data, ResourceAccessLevel level, Boolean synchronous) {
        // 获取数据
        byte[] bytes = ExcelUtil.exportXlsxFile(excelTemplateEnum.getTitle(), excelTemplateEnum.getSheetName(), clazz, data);
        return synchronousFeishuUpload(managerId, excelTemplateEnum.getOriginalName(), bytes, level, ObjectType.XLSX, synchronous);
    }

    @Override
    @Transactional
    public List<Long> uploadList(Long userId, List<MultipartFile> fileList) {
        return fileList.stream().map(file -> upload(userId, file)).toList();
    }

    @Override
    public void setAccessLevel(Long resourceId, ResourceAccessLevel level) {
        digitalResourceService.setAccessLevel(resourceId, level);
    }

    @Override
    @Transactional
    public void remove(Long code) {
        // 若权限小于 USER_ACCESS 就按 USER_ACCESS 权限
        DigitalResource resource = checkAndGetResource(code, DEFAULT_RESOURCE_ACCESS_LEVEL);
        objectStorageService.remove(resource.getFileName());
        digitalResourceService.removeDigitalResource(resource.getId());
    }

    @Override
    @Transactional
    public void removeKindly(Long code) {
        try {
            if(Objects.nonNull(code)) {
                remove(code);
            }
        } catch (GlobalServiceException e) {
            log.warn("尝试删除 code： {}，失败：{} {}", code, e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    public void checkBlockUser(Long userId) {
        // 查询是否被封禁
        String blockKey = ResourceConstants.REDIS_USER_UPLOAD_BLOCK + userId;
        redisCache.getCacheObject(blockKey).ifPresent(date -> {
            long blockDDL = (long) date;
            // 理论上 blockDDL 是肯定小于等于当前时间的，因为大于的时候 redisKey 应该到期了
            // （但还是进行判断，如果出现大于的情况，则把本该过期的 key 删了）
            if(blockDDL <= System.currentTimeMillis()) {
                String message = String.format("用户当前无法上传资源，将于 %s 解封，如有异议请联系管理员！", TimeUtil.getDateTime(blockDDL));
                throw new GlobalServiceException(message, GlobalServiceStatusCode.RESOURCE_UPLOAD_BLOCKED);
            }else {
                redisCache.deleteObject(blockKey);
            }
        });
    }

    @Override
    public void blockUser(Long userId, Long blockDDL) {
        String blockKey = ResourceConstants.REDIS_USER_UPLOAD_BLOCK + userId;
        long timeout = blockDDL - System.currentTimeMillis(); // 现在距离解封时间的时长
        if(timeout > 0) {
            redisCache.setCacheObject(blockKey, blockDDL, timeout, TimeUnit.MILLISECONDS);
        } else {
            // 若为过去时，则直接封禁
            redisCache.deleteObject(blockKey);
        }
    }

}
