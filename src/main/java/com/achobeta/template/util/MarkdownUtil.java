package com.achobeta.template.util;

import lombok.Data;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.footnotes.FootnotesExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-17
 * Time: 11:16
 */
@Data
public class MarkdownUtil {

    private final static List<Extension> OPTIONS;

    private final static Parser PARSER;

    private final static HtmlRenderer HTML_RENDERER;

    static {
        OPTIONS = List.of(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                FootnotesExtension.create(),
                TaskListItemsExtension.create()
        );
        PARSER = Parser.builder().extensions(OPTIONS).build();
        HTML_RENDERER = HtmlRenderer.builder().extensions(OPTIONS).build();
    }

    public static String markdownToHtml(String markdown) {
        // 解析 Markdown 文本为节点
        Node document = PARSER.parse(markdown);
        // 将 Markdown 节点渲染为 HTML
        return HTML_RENDERER.render(document);
    }

}
