package com.achobeta.feishu.constants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 12:56
 */

/**
 * 此类不应该设置为枚举，因为需要序列化为 json
 */
@Getter
@AllArgsConstructor
public class ObjectType {

    private final static String ACHOBETA_RECRUITMENT_DOCX_FOLDER_RESOURCE = "LpDBf0emllgCc2dKNfKcsqxJnjc";
    private final static String ACHOBETA_RECRUITMENT_SHEET_FOLDER_RESOURCE = "OQbUfIGR9lqdFVdbJCDcQ3CSntd";
    private final static String ACHOBETA_RECRUITMENT_DOCX_FOLDER_COOPERATE = "LpDBf0emllgCc2dKNfKcsqxJnjc";
    private final static String ACHOBETA_RECRUITMENT_SHEET_FOLDER_COOPERATE = "SyBEf61JglcR5NdCp34cs2tGnMd";

    public final static ObjectType TXT = ObjectType.of("docx", "txt", ACHOBETA_RECRUITMENT_DOCX_FOLDER_RESOURCE, ACHOBETA_RECRUITMENT_DOCX_FOLDER_COOPERATE);
    public final static ObjectType XLSX = ObjectType.of("sheet", "xlsx", ACHOBETA_RECRUITMENT_SHEET_FOLDER_RESOURCE, ACHOBETA_RECRUITMENT_SHEET_FOLDER_COOPERATE);
    public final static ObjectType MD = ObjectType.of("docx", "md", ACHOBETA_RECRUITMENT_DOCX_FOLDER_RESOURCE, ACHOBETA_RECRUITMENT_DOCX_FOLDER_COOPERATE);
    public final static ObjectType MARKDOWN = ObjectType.of("docx", "markdown", ACHOBETA_RECRUITMENT_DOCX_FOLDER_RESOURCE, ACHOBETA_RECRUITMENT_DOCX_FOLDER_COOPERATE);

    @SerializedName("obj_type")
    private final String objType;

    @SerializedName("file_extension")
    private final String fileExtension;

    @Expose(deserialize = false, serialize = false)
    private final String parentNode;

    @Expose(deserialize = false, serialize = false)
    private final String mountKey;

    private static ObjectType of(String objType, String fileExtension, String parentNode, String mountKey) {
        return new ObjectType(objType, fileExtension, parentNode, mountKey);
    }

}
