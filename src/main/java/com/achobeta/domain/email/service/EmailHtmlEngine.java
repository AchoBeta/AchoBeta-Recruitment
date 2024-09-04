package com.achobeta.domain.email.service;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.email.model.po.EmailHtml;
import com.achobeta.util.MarkdownUtil;
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
public class EmailHtmlEngine {

    private final TemplateEngine templateEngine;

    public <T> Context getContext(T data) {
        Context context = new Context();
        context.setVariables(BeanUtil.beanToMap(data));
        return context;
    }

    public <T> String getHtml(String template, T data) {
        return EmailHtmlEngine.this.templateEngine.process(template, EmailHtmlEngine.this.getContext(data));
    }

    public String getUniqueSymbol() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public EmailHtmlBuilder builder() {
        return new EmailHtmlEngine.EmailHtmlBuilder();
    }

    public class EmailHtmlBuilder {

        private final StringBuffer stringBuffer = new StringBuffer();

        public String build() {
            return EmailHtmlBuilder.this.stringBuffer.toString();
        }

        public EmailHtmlBuilder clear() {
            EmailHtmlBuilder.this.stringBuffer.setLength(0);
            return EmailHtmlBuilder.this;
        }

        public EmailHtmlBuilder append(String html) {
            EmailHtmlBuilder.this.stringBuffer.append(html);
            return EmailHtmlBuilder.this;
        }

        public EmailHtmlBuilder appendMarkdown(String markdown) {
            return EmailHtmlBuilder.this.append(MarkdownUtil.markdownToHtml(markdown));
        }

        public EmailHtmlBuilder replace(String uniqueSymbol, String html) {
            return EmailHtmlBuilder.this.clear().append(EmailHtmlBuilder.this.build().replace(uniqueSymbol, html));
        }

        public EmailHtmlBuilder replaceMarkdown(String uniqueSymbol, String markdown) {
            return EmailHtmlBuilder.this.replace(uniqueSymbol, MarkdownUtil.markdownToHtml(markdown));
        }

        public <T> EmailHtmlBuilder append(String template, T data) {
            return EmailHtmlBuilder.this.append(EmailHtmlEngine.this.getHtml(template, data));
        }

        public EmailHtmlBuilder append(EmailHtml emailHtml) {
            return EmailHtmlBuilder.this.append(emailHtml.getTemplate(), emailHtml.getContext());
        }

        public EmailHtmlBuilder append(List<EmailHtml> emailHtmlList) {
            emailHtmlList.forEach(EmailHtmlBuilder.this::append);
            return EmailHtmlBuilder.this;
        }

    }

}
