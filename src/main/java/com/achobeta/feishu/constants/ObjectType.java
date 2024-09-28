package com.achobeta.feishu.constants;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 12:56
 */
@Getter
public class ObjectType {

    public final static ObjectType TXT = ObjectType.of("docx", "txt");
    public final static ObjectType XLSX = ObjectType.of("sheet", "xlsx");
    public final static ObjectType MD = ObjectType.of("docx", "md");
    public final static ObjectType MARKDOWN = ObjectType.of("docx", "markdown");

    @SerializedName("obj_type")
    private final String objType;

    @SerializedName("file_extension")
    private final String fileExtension;

    private static ObjectType of(String objType, String fileExtension) {
        return new ObjectType(objType, fileExtension);
    }

    private ObjectType (String objType, String fileExtension) {
        this.objType = objType;
        this.fileExtension = fileExtension;
    }

}
