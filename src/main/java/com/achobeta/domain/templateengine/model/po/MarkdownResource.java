package com.achobeta.domain.templateengine.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-07
 * Time: 16:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkdownResource {

    private String template;

    private Object context;

}
