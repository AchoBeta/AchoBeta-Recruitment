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
public class MarkdownEngine {

    /**
     * 忽略模板类型，本类此属性用于识别 .md 文件
     */
    private final UnsafeTemplateEngine unsafeTemplateEngine;

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

        public MarkdownBuilder reset(String markdown) {
            return clear().append(markdown);
        }

        public <T> MarkdownBuilder append(String template, T data) {
            String markdown = unsafeTemplateEngine.process(template, TemplateUtil.getContext(data));
            return append(markdown);
        }

        public MarkdownBuilder append(Resource resource) {
            return append(resource.getTemplate(), resource.getContext());
        }

        public MarkdownBuilder append(List<Resource> resourceList) {
            resourceList.forEach(this::append);
            return this;
        }

        // markdown 替换 target
        public MarkdownBuilder replace(String target, String markdown) {
            String finalMarkdown = build().replace(target, markdown);
            return reset(finalMarkdown);
        }

        // markdown 替换 target
        public MarkdownBuilder replace(ReplaceResource resource) {
            return replace(resource.getTarget(), resource.getReplacement());
        }

        // markdown 集合替换元素对应的 target
        public MarkdownBuilder replace(List<ReplaceResource> resourceList) {
            String finalMarkdown = TemplateUtil.replaceSafely(build(), resourceList, markdown -> markdown);
            return reset(finalMarkdown);
        }

    }

}
