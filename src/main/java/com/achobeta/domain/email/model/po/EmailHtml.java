package com.achobeta.domain.email.model.po;

import com.achobeta.common.enums.EmailTemplateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 11:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailHtml {

    private EmailTemplateEnum template;

    private Object context;

}
