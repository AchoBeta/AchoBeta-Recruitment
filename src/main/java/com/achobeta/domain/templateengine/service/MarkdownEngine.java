package com.achobeta.domain.templateengine.service;

import com.achobeta.domain.templateengine.model.po.MarkdownResource;
import com.achobeta.domain.templateengine.util.TemplateUtil;
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

    public MarkdownEngine.MarkdownBuilder builder() {
        return new MarkdownEngine.MarkdownBuilder();
    }
    
    public class MarkdownBuilder {

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
        
        private final StringBuffer markdownBuffer = new StringBuffer();

        public String build() {
            return MarkdownEngine.MarkdownBuilder.this.markdownBuffer.toString();
        }

        public String buildHtml() {
            // 解析 Markdown 文本为节点
            Node document = MarkdownEngine.MarkdownBuilder.PARSER.parse(MarkdownBuilder.this.build());
            // 将 Markdown 节点渲染为 HTML
            return MarkdownEngine.MarkdownBuilder.HTML_RENDERER.render(document);
        }

        public MarkdownEngine.MarkdownBuilder clear() {
            MarkdownEngine.MarkdownBuilder.this.markdownBuffer.setLength(0);
            return MarkdownEngine.MarkdownBuilder.this;
        }

        public MarkdownEngine.MarkdownBuilder append(String markdown) {
            MarkdownEngine.MarkdownBuilder.this.markdownBuffer.append(markdown);
            return MarkdownEngine.MarkdownBuilder.this;
        }

        public <T> MarkdownEngine.MarkdownBuilder append(String template, T data) {
            String markdown = MarkdownEngine.this.unsafeTemplateEngine.process(template, TemplateUtil.getContext(data));
            return MarkdownEngine.MarkdownBuilder.this.append(markdown);
        }

        public MarkdownEngine.MarkdownBuilder append(MarkdownResource resource) {
            return MarkdownEngine.MarkdownBuilder.this.append(resource.getTemplate(), resource.getContext());
        }

        public MarkdownEngine.MarkdownBuilder append(List<MarkdownResource> resourceList) {
            resourceList.forEach(MarkdownEngine.MarkdownBuilder.this::append);
            return MarkdownEngine.MarkdownBuilder.this;
        }
        
    }

}
