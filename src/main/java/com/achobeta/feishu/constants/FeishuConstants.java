package com.achobeta.feishu.constants;

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

    String ACHOBETA_RECRUITMENT_ROOT_FOLDER = "Sx9KfdvAzlY0YudYhEMcziRznpe";
    String ACHOBETA_RECRUITMENT_ROOT_URL = HttpRequestUtil.buildUrl("https://j16c0vnbedn.feishu.cn/drive/folder/{folderToken}", null, ACHOBETA_RECRUITMENT_ROOT_FOLDER);
    String ACHOBETA_RECRUITMENT_DOCX_FOLDER_RESOURCE = "LpDBf0emllgCc2dKNfKcsqxJnjc";
    String ACHOBETA_RECRUITMENT_SHEET_FOLDER_RESOURCE = "OQbUfIGR9lqdFVdbJCDcQ3CSntd";
    String ACHOBETA_RECRUITMENT_DOCX_FOLDER_COOPERATE = "LpDBf0emllgCc2dKNfKcsqxJnjc";
    String ACHOBETA_RECRUITMENT_SHEET_FOLDER_COOPERATE = "SyBEf61JglcR5NdCp34cs2tGnMd";

    String AUTHORIZATION_HEADER = "Authorization";

    String AUTHORIZATION_PREFIX = "Bearer ";

    String USER_ID_TYPE_QUERY_KEY = "use_id_type";

    String MEDIA_TICKET_QUERY_KEY = "ticket";

    static String getAuthorization(String accessToken) {
        return Optional.ofNullable(accessToken)
                .map(token -> AUTHORIZATION_PREFIX + token)
                .orElse(null);
    }

}
