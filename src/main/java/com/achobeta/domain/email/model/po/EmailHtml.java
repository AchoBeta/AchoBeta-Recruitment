package com.achobeta.domain.email.model.po;

import com.achobeta.common.enums.EmailTemplateEnum;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 11:31
 */
@Data
public class EmailHtml {

    private EmailTemplateEnum template;

    private Object context;

}
