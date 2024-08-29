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
