package com.achobeta.domain.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-29
 * Time: 10:58
 */
@Getter
@AllArgsConstructor
public enum ResourceType {

    IMAGE("image"),
    VIDEO("video"),
    TEXT("text"),
    ;

    private final String contentTypeSuffix;

}
