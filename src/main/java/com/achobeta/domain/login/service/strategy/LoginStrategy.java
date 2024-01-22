package com.achobeta.domain.login.service.strategy;

import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.domain.login.model.vo.LoginVO;
import com.achobeta.exception.GlobalServiceException;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 15:39
 */
public interface LoginStrategy {

    String BASE_NAME = "LoginStrategy";

    static LoginVO doLogin(String body, String loginType) {
        String beanName = loginType + BASE_NAME;
        ListableBeanFactory beanFactory = SpringUtil.getBeanFactory();
        if (!beanFactory.containsBean(beanName)) {
            throw new GlobalServiceException(String.format("ioc 容器未找到 bean:'%s'", beanName));
        }
        LoginStrategy instance = (LoginStrategy) beanFactory.getBean(beanName);
        return instance.doLogin(body);
    }

    /**
     * 登录
     * @param body
     * @return
     */
    LoginVO doLogin(String body);

}
