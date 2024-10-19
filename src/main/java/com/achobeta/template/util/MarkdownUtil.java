package com.achobeta.template.util;


import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.admonition.AdmonitionExtension;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.aside.AsideExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.media.tags.MediaTagsExtension;
import com.vladsch.flexmark.ext.resizable.image.ResizableImageExtension;
import com.vladsch.flexmark.ext.superscript.SuperscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
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

    private final static FlexmarkHtmlConverter HTML_CONVERTER;

    static {
        OPTIONS = new MutableDataSet()
                // 指定 Markdown 标准为 COMMONMARK（使用 ParserEmulationProfile.MARKDOWN 可能会有一些语法失效！）
                .setFrom(ParserEmulationProfile.COMMONMARK)
                .set(Parser.EXTENSIONS, List.of(new Parser.ParserExtension[]{
                        // 设置一些常扩展
                        TocExtension.create(),
                        TablesExtension.create(),
                        AbbreviationExtension.create(),
                        AutolinkExtension.create(),
                        AnchorLinkExtension.create(),
                        AsideExtension.create(),
                        AdmonitionExtension.create(),
                        GfmIssuesExtension.create(),
                        GfmUsersExtension.create(),
                        SuperscriptExtension.create(),
                        StrikethroughSubscriptExtension.create(),
                        GitLabExtension.create(),
                        FootnoteExtension.create(),
                        TaskListExtension.create(),
                        InsExtension.create(),
                        TypographicExtension.create(),
                        DefinitionExtension.create(),
                        AbbreviationExtension.create(),
                        ResizableImageExtension.create(),
                        MediaTagsExtension.create(),
                        EmojiExtension.create(),
                        WikiLinkExtension.create(),
                })).set(HtmlRenderer.SOFT_BREAK, "<br/ >\n");
        PARSER = Parser.builder(OPTIONS).build();
        HTML_RENDERER = HtmlRenderer.builder(OPTIONS).build();
        HTML_CONVERTER = FlexmarkHtmlConverter.builder(OPTIONS).build();
    }

    public static String markdownToHtml(String markdown) {
        // 解析 Markdown 文本为节点
        Node document = PARSER.parse(markdown);
        // 将 Markdown 节点渲染为 HTML
        return HTML_RENDERER.render(document);
    }

    public static String htmlToMarkdown(String html) {
        // 将 HTML 转化为 Markdown
        return HTML_CONVERTER.convert(html);
    }

}
