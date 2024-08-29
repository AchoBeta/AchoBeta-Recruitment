package com.achobeta.common.enums;

import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 15:02
 */
@Getter
/**
 * 用于插入邮件的 html 元素的 style 属性：
 * th:style="${style}"
 */
public enum TextStyle {

    RED("color: red"),
    GREEN("color: green"),
    GREY("color: grey"),

    ;

    private String style;

    TextStyle(String style) {
        this.style = style;
    }
}
