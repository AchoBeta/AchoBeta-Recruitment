package com.achobeta.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-27
 * Time: 9:01
 */
public class GsonUtil {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
//                .setPrettyPrinting() // 美化 json
                .disableHtmlEscaping() // 取消对 html 代码的转义（可能的场景是需要保存 html 代码的字段）
                .create();
    }

    public static <T> String toJson(T data) {
        return GSON.toJson(data);
    }

    public static <T> byte[] toBytes(T data) {
        return toJson(data).getBytes(UTF_8);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromBytes(byte[] data, Class<T> clazz) {
        return fromJson(new String(data, UTF_8), clazz);
    }

}
