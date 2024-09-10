package com.achobeta.template.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-09
 * Time: 11:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    private String template;

    private Object context;

}
