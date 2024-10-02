package com.achobeta.feishu.request;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.HttpRequestEnum;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.HttpRequestUtil;
import com.lark.oapi.core.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-02
 * Time: 18:43
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FeishuRequestEngine {

    public <E> void checkResponse(BaseResponse<E> response) {
        if(!response.success()) {
            // 能到这里就是不成功的情况
            throw new GlobalServiceException(response.getMsg(), GlobalServiceStatusCode.REQUEST_NOT_VALID);
        }
    }

    public <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz, Map<String, String> headers) {
        return HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers);
    }

    public <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz,
                                                              Map<String, String> headers, Map<String, List<String>> queryParams, Map<String, ?> pathParams) {
        return HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers, queryParams, pathParams);
    }

    public <T, E, R extends BaseResponse<E>> R request(HttpRequestEnum httpRequestEnum, T requestBody, Class<R> rClazz,
                                                              Map<String, String> headers, Map<String, List<String>> queryParams, Object... uriVariableValues) {
        return HttpRequestUtil.jsonRequest(httpRequestEnum, requestBody, rClazz, headers, queryParams, uriVariableValues);
    }
}
