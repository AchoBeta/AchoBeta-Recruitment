package com.achobeta.domain.feishu.service.impl;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.feishu.model.converter.FeishuResourceConverter;
import com.achobeta.domain.feishu.model.dao.mapper.FeishuResourceMapper;
import com.achobeta.domain.feishu.model.dto.FeishuResourceQueryDTO;
import com.achobeta.domain.feishu.model.entity.FeishuResource;
import com.achobeta.domain.feishu.model.vo.FeishuResourceQueryVO;
import com.achobeta.domain.feishu.service.FeishuResourceService;
import com.achobeta.domain.feishu.service.FeishuService;
import com.achobeta.redis.cache.RedisCache;
import com.achobeta.redis.lock.RedisLock;
import com.achobeta.redis.lock.strategy.SimpleLockStrategy;
import com.achobeta.util.HttpRequestUtil;
import com.achobeta.util.HttpServletUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lark.oapi.service.drive.v1.model.ImportTask;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

import static com.achobeta.domain.feishu.constants.FeishuResourceConstants.*;

/**
* @author 马拉圈
* @description 针对表【feishu_resource(飞书资源表)】的数据库操作Service实现
* @createDate 2024-09-28 15:11:38
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class FeishuResourceServiceImpl extends ServiceImpl<FeishuResourceMapper, FeishuResource>
    implements FeishuResourceService{

    private final FeishuService feishuService;

    private final RedisCache redisCache;

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    @Override
    public FeishuResource createAndGetFeishuResource(String ticket, String originalName) {
        return redisLock.tryLockGetSomething(FEISHU_RESOURCE_CREATE_LOCK + ticket, () -> {
            return this.lambdaQuery().eq(FeishuResource::getTicket, ticket).oneOpt().orElseGet(() -> {
                // 创建一个新的
                FeishuResource feishuResource = new FeishuResource();
                feishuResource.setTicket(ticket);
                feishuResource.setOriginalName(originalName);
                this.save(feishuResource);
                return feishuResource;
            });
        }, () -> null, simpleLockStrategy);
    }

    @Override
    public void updateFeishuResource(Long id, ImportTask importTask) {
        FeishuResource feishuResource = FeishuResourceConverter.INSTANCE.importTaskToFeishuResource(importTask);
        this.lambdaUpdate().eq(FeishuResource::getId, id).update(feishuResource);
    }

    @Override
    @Transactional
    public String redirectByTicket(String ticket) {
        String redisKey = FEISHU_RESOURCE_REDIRECT_KEY + ticket;
        return (String) redisCache.getCacheObject(redisKey).orElseGet(() -> {
            FeishuResource feishuResource = createAndGetFeishuResource(ticket, DEFAULT_NAME);
            String url = feishuResource.getUrl();
            if(StringUtils.hasText(url)) {
                redisCache.setCacheObject(redisKey, url, FEISHU_RESOURCE_REDIRECT_TIMEOUT, FEISHU_RESOURCE_REDIRECT_UNIT);
                return url;
            }else {
                // 尝试通过 ticket 获取
                ImportTask result = feishuService.getImportTask(ticket).getResult();
                log.warn("任务状态 {}，额外提示 {}, 任务若失败，失败的原因 {}，参考 {}", (
                        result.getJobStatus()), Arrays.toString(result.getExtra()), result.getJobErrorMsg(),
                        "https://open.feishu.cn/document/server-docs/docs/drive-v1/import_task/get?appId=cli_a67eb2cebcf99013"
                );
                url = result.getUrl();
                if(StringUtils.hasText(url)) {
                    updateFeishuResource(feishuResource.getId(), result);
                    return url;
                }else {
                    return DEFAULT_URL; // 只返回不落库
                }
            }
        });
    }

    @Override
    public FeishuResourceQueryVO queryResource(FeishuResourceQueryDTO feishuResourceQueryDTO) {
        // 解析分页参数获取 page
        IPage<FeishuResource> page = null;
        String type = null;
        if (Objects.isNull(feishuResourceQueryDTO)) {
            page = new BasePageQuery().toMpPage();
        } else {
            page = FeishuResourceConverter.INSTANCE.feishuResourceQueryDTOToBasePageQuery(feishuResourceQueryDTO).toMpPage();
            type =  feishuResourceQueryDTO.getType();
        }
        // 分页
        IPage<FeishuResource> resourceIPage = this.lambdaQuery()
                .eq(StringUtils.hasText(type), FeishuResource::getType, type)
                .page(page);
        // 封装
        BasePageResult<FeishuResource> pageResult = BasePageResult.of(resourceIPage);
        // 转化
        return FeishuResourceConverter.INSTANCE.basePageResultToFeishuResourceQueryVO(pageResult);
    }

    @Override
    public String getSystemUrl(HttpServletRequest request, String ticket) {
        String baseUrl = HttpServletUtil.getBaseUrl(request, "/api/v1/feishu/resource", "/{ticket}");
        return HttpRequestUtil.buildUrl(baseUrl, null, ticket);
    }
}




