package com.achobeta.feishu.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.HttpRequestUtil;
import com.lark.oapi.core.response.BaseResponse;

import java.util.List;
import java.util.Map;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 9:19
 */
public class FeishuRequestUtil {

    public static <E> void checkResponse(BaseResponse<E> response) {
        if(!response.success()) {
            throw new GlobalServiceException(response.getMsg(), GlobalServiceStatusCode.REQUEST_NOT_VALID);
        }
    }

    public static <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz, Map<String, String> headers) {
        R resp = HttpRequestUtil.jsonRequest(httpRequestEnum.getUrl(), httpRequestEnum.getMethod(), requestBody, rClazz, headers);
        checkResponse(resp);
        return resp;
    }

    public static <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz,
                                                              Map<String, String> headers, Map<String, List<String>> queryParams, Map<String, ?> pathParams) {
        R resp = HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers, queryParams, pathParams);
        checkResponse(resp);
        return resp;
    }

    public static <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz,
                                                               Map<String, String> headers, Map<String, List<String>> queryParams, Object... uriVariableValues) {
        R resp = HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers, queryParams, uriVariableValues);
        checkResponse(resp);
        return resp;
    }
}
