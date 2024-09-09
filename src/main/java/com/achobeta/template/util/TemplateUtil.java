package com.achobeta.template.util;

import cn.hutool.core.bean.BeanUtil;
import org.thymeleaf.context.Context;

import java.util.UUID;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-07
 * Time: 17:00
 */
public class TemplateUtil {

    public static <T> Context getContext(T data) {
        Context context = new Context();
        context.setVariables(BeanUtil.beanToMap(data));
        return context;
    }

    public static String getUniqueSymbol() {
        return UUID.randomUUID().toString();
    }

}
