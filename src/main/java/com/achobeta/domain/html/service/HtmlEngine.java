package com.achobeta.domain.html.service;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.html.model.po.HtmlResource;
import com.achobeta.domain.html.util.MarkdownUtil;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
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

    private final static MutableDataSet OPTIONS = new MutableDataSet();

    private final static Parser PARSER = Parser.builder(OPTIONS).build();

    private final static HtmlRenderer HTML_RENDERER = HtmlRenderer.builder(OPTIONS).build();

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

        private final StringBuffer stringBuffer = new StringBuffer();

        public String build() {
            return HtmlBuilder.this.stringBuffer.toString();
        }

        public HtmlBuilder clear() {
            HtmlBuilder.this.stringBuffer.setLength(0);
            return HtmlBuilder.this;
        }

        public HtmlBuilder append(String html) {
            HtmlBuilder.this.stringBuffer.append(html);
            return HtmlBuilder.this;
        }

        public HtmlBuilder appendMarkdown(String markdown) {
            return HtmlBuilder.this.append(HtmlEngine.this.markdownToHtml(markdown));
        }

        public HtmlBuilder replace(String uniqueSymbol, String html) {
            return HtmlBuilder.this.clear().append(HtmlBuilder.this.build().replace(uniqueSymbol, html));
        }

        public HtmlBuilder replaceMarkdown(String uniqueSymbol, String markdown) {
            return HtmlBuilder.this.replace(uniqueSymbol, HtmlEngine.this.markdownToHtml(markdown));
        }

        public <T> HtmlBuilder append(String template, T data) {
            return HtmlBuilder.this.append(HtmlEngine.this.getHtml(template, data));
        }

        public HtmlBuilder append(HtmlResource htmlResource) {
            return HtmlBuilder.this.append(htmlResource.getTemplate(), htmlResource.getContext());
        }

        public HtmlBuilder append(List<HtmlResource> htmlResourceList) {
            htmlResourceList.forEach(HtmlBuilder.this::append);
            return HtmlBuilder.this;
        }

    }

}
