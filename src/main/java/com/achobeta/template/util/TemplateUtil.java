package com.achobeta.template.util;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.template.model.po.ReplaceResource;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

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
        return String.format("[(${%s})]", UUID.randomUUID());
    }

    public static String replace(String text, List<ReplaceResource> resourceList, Function<String, String> textConverter) {
        StringReplacer textReplacer = new StringReplacer(text);
        resourceList.forEach(resource -> {
            String target = resource.getTarget();
            String replacement = textConverter.apply(resource.getReplacement());
            textReplacer.replace(target, replacement);
        });
        return textReplacer.toString();
    }

    /**
     * 此方法可以解决问题：替换资源集合中 “后续的 replacement” 中包含 “之前的 target”，导致不可预期的效果
     * @param text 待替换的文本
     * @param resourceList 替换资源集合
     * @param textConverter 文本转换器
     * @return 替换后的文本
     */
    public static String replaceSafely(String text, List<ReplaceResource> resourceList, Function<String, String> textConverter) {
        Map<String, String> tempTargetMap = new HashMap<>();
        StringReplacer textReplacer = new StringReplacer(text);
        resourceList.stream().peek(resource -> {
            // 将每个 target 替换为临时的唯一标识，并保存映射关系
            String originTarget = resource.getTarget();
            if (tempTargetMap.containsKey(originTarget)) {
                // 若映射表种存在，说明 text 中 originTarget 已经全被替换了，直接忽略
                return;
            }
            String tmpSymbol = getUniqueSymbol();
            textReplacer.replace(originTarget, tmpSymbol);
            tempTargetMap.put(originTarget, tmpSymbol);
        }).forEach(resource -> {
            // 通过映射表获取临时的唯一标识，获取欲替换的文本，进行替换
            String target = tempTargetMap.get(resource.getTarget());
            String replacement = textConverter.apply(resource.getReplacement());
            textReplacer.replace(target, replacement);
        });
        return textReplacer.toString();
    }

}
