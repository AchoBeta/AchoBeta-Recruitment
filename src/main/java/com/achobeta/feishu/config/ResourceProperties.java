package com.achobeta.feishu.config;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 16:51
 */
@Data
public class ResourceProperties {

    private Boolean backup;

    private Long tryAgain;

    private TimeUnit tryAgainUnit;

    private Integer maxCount;

    private FolderProperties folder;

}
