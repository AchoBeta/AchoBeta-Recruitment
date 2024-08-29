package com.achobeta.domain.email.service;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.email.model.po.EmailHtml;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
public class EmailHtmlEngine {

    private final TemplateEngine templateEngine;

    public EmailHtmlBuilder builder() {
        return new EmailHtmlBuilder();
    }

    public class EmailHtmlBuilder {

        private final StringBuilder htmlBuilder = new StringBuilder();

        private <T> Context getContext(T data) {
            Context context = new Context();
            context.setVariables(BeanUtil.beanToMap(data));
            return context;
        }

        private <T> String getHtml(String template, T data) {
            return templateEngine.process(template, getContext(data));
        }

        public EmailHtmlBuilder append(String html) {
            htmlBuilder.append(html);
            return this;
        }

        public <T> EmailHtmlBuilder append(String template, T data) {
            return append(getHtml(template, data));
        }

        public EmailHtmlBuilder append(EmailHtml emailHtml) {
            return append(emailHtml.getTemplate(), emailHtml.getContext());
        }

        public EmailHtmlBuilder append(List<EmailHtml> emailHtmlList) {
            emailHtmlList.forEach(this::append);
            return this;
        }

        public String build() {
            return htmlBuilder.toString();
        }

    }

}
