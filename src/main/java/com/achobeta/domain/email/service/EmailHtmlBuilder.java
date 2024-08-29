package com.achobeta.domain.email.service;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.EmailTemplateEnum;
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
public class EmailHtmlBuilder {

    private final TemplateEngine templateEngine;

    public HtmlBuilder builder() {
        return new HtmlBuilder();
    }

    public class HtmlBuilder {

        private final StringBuilder htmlBuilder = new StringBuilder();

        private <T> Context getContext(T data) {
            Context context = new Context();
            context.setVariables(BeanUtil.beanToMap(data));
            return context;
        }

        public HtmlBuilder append(String html) {
            htmlBuilder.append(html);
            return this;
        }

        public <T> HtmlBuilder append(EmailTemplateEnum emailTemplateEnum, T data) {
            return append(templateEngine.process(emailTemplateEnum.getTemplate(), getContext(data)));
        }

        public HtmlBuilder append(EmailHtml emailHtml) {
            return append(emailHtml.getTemplate(), emailHtml.getContext());
        }

        public HtmlBuilder append(List<EmailHtml> emailHtmlList) {
            emailHtmlList.forEach(this::append);
            return this;
        }

        public String build() {
            return htmlBuilder.toString();
        }

    }

}
