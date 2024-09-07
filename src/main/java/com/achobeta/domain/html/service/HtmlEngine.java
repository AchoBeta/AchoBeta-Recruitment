package com.achobeta.domain.html.service;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.html.model.po.HtmlReplaceResource;
import com.achobeta.domain.html.model.po.HtmlResource;
import com.achobeta.domain.html.model.po.MarkdownReplaceResource;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;

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

    private final static MutableDataSet OPTIONS;

    private final static Parser PARSER;

    private final static HtmlRenderer HTML_RENDERER;

    static {
        OPTIONS = new MutableDataSet()
                .setFrom(ParserEmulationProfile.MARKDOWN)
                // 支持 [TOC]目录 以及 表格
                .set(Parser.EXTENSIONS, List.of(TocExtension.create(), TablesExtension.create()))
        ;
        PARSER = Parser.builder(OPTIONS).build();
        HTML_RENDERER = HtmlRenderer.builder(OPTIONS).build();
    }

    /**
     * 此类支持 html 以及 xml 的合成，注入的属性会被认为是纯字符串，防止了 xss
     * 以及文本的部分字符串影响整体结构
     */
    private final TemplateEngine templateEngine;

    public <T> Context getContext(T data) {
        Context context = new Context();
        context.setVariables(BeanUtil.beanToMap(data));
        return context;
    }

    public <T> String getHtml(String template, T data) {
        return HtmlEngine.this.templateEngine.process(template, HtmlEngine.this.getContext(data));
    }

    public String markdownToHtml(String markdown) {
        // 解析 Markdown 文本为节点
        Node document = PARSER.parse(markdown);
        // 将 Markdown 节点渲染为 HTML
        return HTML_RENDERER.render(document);
    }

    public String getUniqueSymbol() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public HtmlBuilder builder() {
        return new HtmlBuilder();
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

        public HtmlBuilder append(String html) {
            HtmlBuilder.this.htmlBuffer.append(html);
            return HtmlBuilder.this;
        }

        public HtmlBuilder appendMarkdown(String markdown) {
            return HtmlBuilder.this.append(HtmlEngine.this.markdownToHtml(markdown));
        }

        public HtmlBuilder replace(String target, String html) {
            String replacement = HtmlBuilder.this.build().replace(target, html);
            return HtmlBuilder.this.clear().append(replacement);
        }

        public HtmlBuilder replace(HtmlReplaceResource resource) {
            return HtmlBuilder.this.replace(resource.getTarget(), resource.getHtml());
        }

        public HtmlBuilder replace(List<HtmlReplaceResource> resourceList) {
            resourceList.forEach(HtmlBuilder.this::replace);
            return HtmlBuilder.this;
        }

        /**
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
            return HtmlBuilder.this.replace(target, HtmlEngine.this.markdownToHtml(markdown));
        }

        public HtmlBuilder replaceMarkdown(MarkdownReplaceResource resource) {
            return HtmlBuilder.this.replaceMarkdown(resource.getTarget(), resource.getMarkdown());
        }

        public HtmlBuilder replaceMarkdown(List<MarkdownReplaceResource> resourceList) {
            resourceList.forEach(HtmlBuilder.this::replaceMarkdown);
            return HtmlBuilder.this;
        }

        public <T> HtmlBuilder append(String template, T data) {
            return HtmlBuilder.this.append(HtmlEngine.this.getHtml(template, data));
        }

        public HtmlBuilder append(HtmlResource resource) {
            return HtmlBuilder.this.append(resource.getTemplate(), resource.getContext());
        }

        public HtmlBuilder append(List<HtmlResource> resourceList) {
            resourceList.forEach(HtmlBuilder.this::append);
            return HtmlBuilder.this;
        }

    }

}
