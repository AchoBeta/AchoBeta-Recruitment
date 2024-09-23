package com.achobeta.domain.resource.factory;

import com.achobeta.domain.resource.service.ObjectStorageService;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 10:49
 */
@Component
public class ObjectStorageServiceFactory {

    private final ServiceLoader<ObjectStorageService> services = ServiceLoader.load(ObjectStorageService.class);

    public ObjectStorageService load() {
        // 选取服务
        Iterator<ObjectStorageService> iterator = services.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

}
