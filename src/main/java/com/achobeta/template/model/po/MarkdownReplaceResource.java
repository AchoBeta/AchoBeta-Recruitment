package com.achobeta.template.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 9:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkdownReplaceResource {

    private String target;

    private String markdown;

}
