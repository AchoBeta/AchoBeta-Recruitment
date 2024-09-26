package com.achobeta.feishu.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.HttpRequestUtil;
import com.lark.oapi.core.response.BaseResponse;

import java.util.Map;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 9:19
 */
public class FeishuRequestUtil {

    public static <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz, Map<String, String> headers) {
        R resp = HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers);
        if(resp.success()) {
            return resp;
        } else {
            throw new GlobalServiceException(resp.getMsg(), GlobalServiceStatusCode.REQUEST_NOT_VALID);
        }
    }

}
