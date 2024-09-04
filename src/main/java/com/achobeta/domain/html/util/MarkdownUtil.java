package com.achobeta.domain.html.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-04
 * Time: 18:21
 */
public class MarkdownUtil {

    private final static MutableDataSet OPTIONS = new MutableDataSet();

    private final static Parser PARSER = Parser.builder(OPTIONS).build();

    private final static HtmlRenderer HTML_RENDERER = HtmlRenderer.builder(OPTIONS).build();

    public static String getHtml(String markdown) {
        // 解析 Markdown 文本为节点
        Node document = PARSER.parse(markdown);
        // 将 Markdown 节点渲染为 HTML
        return HTML_RENDERER.render(document);
    }

}
