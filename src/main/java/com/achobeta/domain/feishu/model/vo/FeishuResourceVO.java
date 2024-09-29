package com.achobeta.domain.feishu.model.vo;

import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 15:59
 */
@Data
public class FeishuResourceVO {

    private Long id;

    private String ticket;

    private String originalName;

    private String token;

    private String type;

    private String url;

}
