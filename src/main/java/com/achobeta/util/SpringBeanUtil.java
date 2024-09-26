package com.achobeta.util;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 11:55
 */
public class SpringBeanUtil {

    public static <T> String getBeanName(T bean) {
        return Introspector.decapitalize(ClassUtils.getShortName(bean.getClass()));
    }

    public static <S> void registerBean(ConfigurableListableBeanFactory beanFactory, S service) {
        String beanName = getBeanName(service);
        beanFactory.autowireBean(service);
        beanFactory.registerSingleton(beanName, service);
    }

}
