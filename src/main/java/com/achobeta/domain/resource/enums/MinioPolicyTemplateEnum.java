package com.achobeta.domain.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 14:08
 */
@Getter
@AllArgsConstructor
public enum MinioPolicyTemplateEnum {

    ALLOW_ALL_GET("minio-allow-all-get.json"),

    ;

    private final String template;



}
