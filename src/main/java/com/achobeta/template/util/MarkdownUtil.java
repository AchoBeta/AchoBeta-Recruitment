package com.achobeta.template.util;



import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-17
 * Time: 11:16
 */
public class MarkdownUtil {

    private final static MutableDataSet OPTIONS;

    private final static Parser PARSER;

    private final static HtmlRenderer HTML_RENDERER;

    static {
        OPTIONS = new MutableDataSet()
                .set(Parser.EXTENSIONS, List.of(
                        TablesExtension.create(),
                        AutolinkExtension.create(),
                        StrikethroughExtension.create(),
                        FootnoteExtension.create(),
                        TaskListExtension.create(),
                        InsExtension.create(),
                        TocExtension.create()
                ));

        PARSER = Parser.builder(OPTIONS).build();
        HTML_RENDERER = HtmlRenderer.builder(OPTIONS).build();
    }

    public static String markdownToHtml(String markdown) {
        // 解析 Markdown 文本为节点
        Node document = PARSER.parse(markdown);
        // 将 Markdown 节点渲染为 HTML
        return HTML_RENDERER.render(document);
    }

}
