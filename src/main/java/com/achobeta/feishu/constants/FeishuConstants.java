package com.achobeta.feishu.constants;

import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.feishu.config.FeishuAppConfig;
import com.achobeta.feishu.config.FolderProperties;
import com.achobeta.util.HttpRequestUtil;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-25
 * Time: 23:24
 */
public interface FeishuConstants {

    FolderProperties ACHOBETA_RESOURCE_FOLDER = SpringUtil.getBean(FeishuAppConfig.class).getResource().getFolder();

    String ACHOBETA_RECRUITMENT_ROOT_FOLDER = ACHOBETA_RESOURCE_FOLDER.getRoot();
    String ACHOBETA_RECRUITMENT_ROOT_BASE_URL = "https://j16c0vnbedn.feishu.cn/drive/folder/{folderToken}";
    String ACHOBETA_RECRUITMENT_ROOT_URL = HttpRequestUtil.buildUrl(ACHOBETA_RECRUITMENT_ROOT_BASE_URL, null, ACHOBETA_RECRUITMENT_ROOT_FOLDER);
    String ACHOBETA_RECRUITMENT_DOCX_FOLDER_RESOURCE = ACHOBETA_RESOURCE_FOLDER.getDocxResource();
    String ACHOBETA_RECRUITMENT_SHEET_FOLDER_RESOURCE = ACHOBETA_RESOURCE_FOLDER.getSheetResource();
    String ACHOBETA_RECRUITMENT_DOCX_FOLDER_COOPERATE = ACHOBETA_RESOURCE_FOLDER.getDocxCooperate();
    String ACHOBETA_RECRUITMENT_SHEET_FOLDER_COOPERATE = ACHOBETA_RESOURCE_FOLDER.getSheetCooperate();

    String AUTHORIZATION_HEADER = "Authorization";

    String AUTHORIZATION_PREFIX = "Bearer ";

    String USER_ID_TYPE_QUERY_KEY = "use_id_type";

    String MEDIA_TICKET_QUERY_KEY = "ticket";

    int SUCCESS_CODE = 0;

    static String getAuthorization(String accessToken) {
        return Optional.ofNullable(accessToken)
                .map(token -> AUTHORIZATION_PREFIX + token)
                .orElse(null);
    }

}
