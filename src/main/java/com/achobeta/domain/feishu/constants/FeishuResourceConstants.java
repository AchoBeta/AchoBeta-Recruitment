package com.achobeta.domain.feishu.constants;

import com.achobeta.feishu.constants.FeishuConstants;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 15:19
 */
public interface FeishuResourceConstants {

    String FEISHU_RESOURCE_REDIRECT_KEY = "feishuResourceKey:";

    Long FEISHU_RESOURCE_REDIRECT_TIMEOUT = 1L;

    TimeUnit FEISHU_RESOURCE_REDIRECT_UNIT = TimeUnit.DAYS;

    String FEISHU_RESOURCE_CREATE_LOCK = "feishuResourceCreateLock:";

    String DEFAULT_URL = "https://j16c0vnbedn.feishu.cn/drive/folder/Sx9KfdvAzlY0YudYhEMcziRznpe";

    String DEFAULT_NAME = "achoBeta-recruitment-resource";

}
