package com.achobeta.domain.template.engine;

import com.achobeta.domain.template.model.po.MarkdownResource;
import com.achobeta.domain.template.util.TemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

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
public class MarkdownEngine {

    /**
     * 忽略模板类型，本类此属性用于识别 .md 文件
     * 注入的属性会被认为是纯字符串，防止 xss 与文本的部分字符串影响整体结构
     */
    private final TemplateEngine unsafeTemplateEngine;

    public MarkdownBuilder builder() {
        return new MarkdownBuilder();
    }
    
    public class MarkdownBuilder {
        
        private final StringBuffer markdownBuffer = new StringBuffer();

        public String build() {
            return markdownBuffer.toString();
        }

        public MarkdownBuilder clear() {
            markdownBuffer.setLength(0);
            return this;
        }

        public MarkdownBuilder append(String markdown) {
            markdownBuffer.append(markdown);
            return this;
        }

        public <T> MarkdownBuilder append(String template, T data) {
            String markdown = unsafeTemplateEngine.process(template, TemplateUtil.getContext(data));
            return append(markdown);
        }

        public MarkdownBuilder append(MarkdownResource resource) {
            return append(resource.getTemplate(), resource.getContext());
        }

        public MarkdownBuilder append(List<MarkdownResource> resourceList) {
            resourceList.forEach(this::append);
            return this;
        }
        
    }

}
