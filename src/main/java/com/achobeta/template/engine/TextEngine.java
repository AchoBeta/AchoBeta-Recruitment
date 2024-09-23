package com.achobeta.template.engine;

import com.achobeta.template.model.po.ReplaceResource;
import com.achobeta.template.model.po.Resource;
import com.achobeta.template.util.TemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-07
 * Time: 16:43
 */
@Component
@RequiredArgsConstructor
public class TextEngine {

    /**
     * 忽略模板类型，本类此属性用于识别 .md 等文件
     */
    private final UnsafeTemplateEngine unsafeTemplateEngine;

    public TextBuilder builder() {
        return new TextBuilder();
    }
    
    public class TextBuilder {
        
        private final StringBuffer textBuffer = new StringBuffer();

        public String build() {
            return textBuffer.toString();
        }

        public TextBuilder clear() {
            textBuffer.setLength(0);
            return this;
        }

        public TextBuilder append(String text) {
            textBuffer.append(text);
            return this;
        }

        public TextBuilder reset(String text) {
            return clear().append(text);
        }

        public <T> TextBuilder append(String template, T data) {
            String text = unsafeTemplateEngine.process(template, TemplateUtil.getContext(data));
            return append(text);
        }

        public TextBuilder append(Resource resource) {
            return append(resource.getTemplate(), resource.getContext());
        }

        public TextBuilder append(List<Resource> resourceList) {
            resourceList.forEach(this::append);
            return this;
        }

        // text 替换 target
        public TextBuilder replace(String target, String text) {
            String finalMarkdown = build().replace(target, text);
            return reset(finalMarkdown);
        }

        // text 替换 target
        public TextBuilder replace(ReplaceResource resource) {
            return replace(resource.getTarget(), resource.getReplacement());
        }

        // text 集合替换元素对应的 target
        public TextBuilder replace(List<ReplaceResource> resourceList) {
            String finalMarkdown = TemplateUtil.replaceSafely(build(), resourceList, text -> text);
            return reset(finalMarkdown);
        }

    }

}
