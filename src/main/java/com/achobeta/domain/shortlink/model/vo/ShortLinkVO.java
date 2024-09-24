package com.achobeta.domain.shortlink.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 22:10
 */
@Data
public class ShortLinkVO {

    private Long id;

    private String originUrl;

    private String shortCode;

    private Boolean isUsed;

    protected LocalDateTime createTime;

    private LocalDateTime updateTime;

}
