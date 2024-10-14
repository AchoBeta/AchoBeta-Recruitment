package com.achobeta.domain.resource.factory.locator;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.access.strategy.ResourceAccessStrategy;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.factory.AccessStrategyFactory;
import com.achobeta.exception.GlobalServiceException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:31
 */
@Component
public class AccessServiceLocatorFactoryBean extends ServiceLocatorFactoryBean {

    @Override
    public void afterPropertiesSet() {
        // 设置服务接口
        super.setServiceLocatorInterface(AccessStrategyFactory.class);

        Properties properties = new Properties();
        // 设置规则
        Arrays.stream(ResourceAccessLevel.values()).forEach(level -> properties.setProperty(
                String.valueOf(level), level.getBeanPrefix() + ResourceAccessStrategy.BASE_NAME
        ));
        super.setServiceMappings(properties);
        // 自定义异常
        super.setServiceLocatorExceptionClass(GlobalServiceException.class);
        // 执行原初始化方法
        super.afterPropertiesSet();
    }

    @Override
    protected Exception createServiceLocatorException(Constructor<Exception> exceptionConstructor, BeansException cause) {
        return new GlobalServiceException(GlobalServiceStatusCode.REQUEST_NOT_VALID);
    }
}
