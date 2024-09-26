package com.achobeta.feishu.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.response.FeishuResponse;
import com.achobeta.util.HttpRequestUtil;

import java.util.Map;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 9:19
 */
public class FeishuRequestUtil {

    private final static Integer SUCCESS_CODE = 0;

    public static <T, R extends FeishuResponse> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz, Map<String, String> headers) {
        R responseBody = HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers);
        if(SUCCESS_CODE.equals(responseBody.getCode())) {
            return responseBody;
        } else {
            throw new GlobalServiceException(responseBody.getMsg(), GlobalServiceStatusCode.REQUEST_NOT_VALID);
        }
    }

}
