package com.achobeta.domain.resource.service.impl;

import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.domain.resource.service.ResourceService;
import org.springframework.stereotype.Service;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:17
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public boolean isValid(Long code) {
        // todo: 资源可以上传后请继续完成业务
        return Boolean.TRUE;
    }

    @Override
    public boolean isPermit(Long userId, Long code) {
        // todo: 资源可以上传后请继续完成业务
        return isValid(code) && Boolean.TRUE;
    }

    @Override
    public String analyzeCode(Long userId, Long code) {
        // todo: 资源可以上传后请继续完成获取头像 url 的业务
        return isPermit(userId, code) ? ResourceConstants.DEFAULT_IMAGE : null;
    }

}
