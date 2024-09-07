package com.achobeta.domain.templateengine.service;

import com.achobeta.domain.templateengine.model.po.HtmlReplaceResource;
import com.achobeta.domain.templateengine.model.po.HtmlResource;
import com.achobeta.domain.templateengine.model.po.MarkdownReplaceResource;
import com.achobeta.domain.templateengine.util.TemplateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 11:07
 */
@Component
@RequiredArgsConstructor
public class HtmlEngine {

    /**
     * 注入的属性会被认为是纯字符串，防止 xss 与文本的部分字符串影响整体结构
     */
    private final TemplateEngine templateEngine;

    private final MarkdownEngine markdownEngine;

    public HtmlEngine.HtmlBuilder builder() {
        return new HtmlEngine.HtmlBuilder();
    }

    public class HtmlBuilder {

        private final StringBuffer htmlBuffer = new StringBuffer();

        public String build() {
            return HtmlBuilder.this.htmlBuffer.toString();
        }

        public HtmlBuilder clear() {
            HtmlBuilder.this.htmlBuffer.setLength(0);
            return HtmlBuilder.this;
        }

        // 追加一段 html
        public HtmlBuilder append(String html) {
            HtmlBuilder.this.htmlBuffer.append(html);
            return HtmlBuilder.this;
        }

        // 合并 html 并追加
        public <T> HtmlBuilder append(String template, T data) {
            String html = HtmlEngine.this.templateEngine.process(template, TemplateUtil.getContext(data));
            return HtmlBuilder.this.append(html);
        }

        // 合并 html 并追加
        public HtmlBuilder append(HtmlResource resource) {
            return HtmlBuilder.this.append(resource.getTemplate(), resource.getContext());
        }

        // 合并 html 并依次追加
        public HtmlBuilder append(List<HtmlResource> resourceList) {
            resourceList.forEach(HtmlBuilder.this::append);
            return HtmlBuilder.this;
        }

        // html 替换 target
        public HtmlBuilder replace(String target, String html) {
            String replacement = HtmlBuilder.this.build().replace(target, html);
            return HtmlBuilder.this.clear().append(replacement);
        }

        // html 替换 target
        public HtmlBuilder replace(HtmlReplaceResource resource) {
            return HtmlBuilder.this.replace(resource.getTarget(), resource.getHtml());
        }

        // html 集合依次替换对应的 target
        public HtmlBuilder replace(List<HtmlReplaceResource> resourceList) {
            resourceList.forEach(HtmlBuilder.this::replace);
            return HtmlBuilder.this;
        }

        /**
         * 拓展功能：
         * 通过替换标识符，向原 html 插入一段 markdown 文本转换的 html
         * @param target 待替换的标识符
         * @param markdown 待插入的 markdown 文本
         * @return HtmlBuilder.this
         * 推荐用法：
         * 假设邮件模板为 template，邮件模板数据为 data，data 里有一个字段 content，而 content 为 markdown 文本
         * 1. 通过调用 htmlEngine.getUniqueSymbol()，获得一个唯一标识符 uniqueSymbol
         * 2. content 先设置为 uniqueSymbol，然后调用 append，传入 template 和 data 合成初步的 html
         * 3. 紧接着调用 replaceMarkdown，传入 uniqueSymbol 和原 markdown 文本，文本转换为 html 并替换 uniqueSymbol 的位置
         */
        public HtmlBuilder replaceMarkdown(String target, String markdown) {
            String html = HtmlEngine.this.markdownEngine.builder().append(markdown).buildHtml();
            return HtmlBuilder.this.replace(target, html);
        }

        // md -> html 替换 target
        public HtmlBuilder replaceMarkdown(MarkdownReplaceResource resource) {
            return HtmlBuilder.this.replaceMarkdown(resource.getTarget(), resource.getMarkdown());
        }

        // md 依次转化为 html 替换对应的 target
        public HtmlBuilder replaceMarkdown(List<MarkdownReplaceResource> resourceList) {
            resourceList.forEach(HtmlBuilder.this::replaceMarkdown);
            return HtmlBuilder.this;
        }

    }

}
