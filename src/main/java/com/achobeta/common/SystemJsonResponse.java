package com.achobeta.common;

import com.achobeta.common.constants.GlobalServiceStatusCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * <span>
 * 统一数据返回,采用了LinkedHashMap的方式，不会造成多余字段
 * <p>
 * code: {@link GlobalServiceStatusCode}
 * </p>
 * </span>
 *
 * @author jettcc in 2024/01/11
 * @version 1.0
 */
@NoArgsConstructor
public class SystemJsonResponse extends LinkedHashMap<String, Object> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Object NULL = null;
    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_MESSAGE = "message";
    private static final String RESPONSE_DATA = "data";

    private SystemJsonResponse(int code, String msg, Object data) {
        this.put(RESPONSE_CODE, code);
        Optional.ofNullable(msg).ifPresent(m -> this.put(RESPONSE_MESSAGE, m));
        Optional.ofNullable(data).ifPresent(d -> this.put(RESPONSE_DATA, d));
    }

    private SystemJsonResponse(int code, String msg) {
        this(code, msg, NULL);
    }


    /**
     * 成功信息返回, 无数据
     * {@link GlobalServiceStatusCode#SYSTEM_SUCCESS}
     *
     * @return 成功状态码
     */
    public static SystemJsonResponse SYSTEM_SUCCESS() {
        return new SystemJsonResponse(GlobalServiceStatusCode.SYSTEM_SUCCESS.getCode(),
                GlobalServiceStatusCode.SYSTEM_SUCCESS.getMessage());
    }

    /**
     * 成功信息返回
     * {@link GlobalServiceStatusCode#SYSTEM_SUCCESS}
     *
     * @param data 返回时带上的数据
     * @return 成功状态码以及数据
     */
    public static SystemJsonResponse SYSTEM_SUCCESS(Object data) {
        return new SystemJsonResponse(GlobalServiceStatusCode.SYSTEM_SUCCESS.getCode(),
                GlobalServiceStatusCode.SYSTEM_SUCCESS.getMessage(), data);
    }

    /**
     * 错误信息返回
     * {@link GlobalServiceStatusCode#SYSTEM_SERVICE_FAIL}
     *
     * @return SystemJsonResponse
     */
    public static SystemJsonResponse SYSTEM_FAIL() {
        return new SystemJsonResponse(GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL.getCode(),
                GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL.getMessage());
    }

    /**
     * 系统异常返回
     * {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @return SystemJsonResponse
     */
    public static SystemJsonResponse SERVICE_ERROR() {
        return new SystemJsonResponse(GlobalServiceStatusCode.SYSTEM_SERVICE_ERROR.getCode(),
                GlobalServiceStatusCode.SYSTEM_SERVICE_ERROR.getMessage());
    }

    /**
     * 系统异常返回, 自定义code
     * {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @param code 自定义状态码 {@link GlobalServiceStatusCode}
     * @return code对应的错误信息
     */
    public static SystemJsonResponse CUSTOMIZE_ERROR(GlobalServiceStatusCode code) {
        return new SystemJsonResponse(code.getCode(), code.getMessage());
    }

    /**
     * 系统异常返回, 自定义code
     * {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @param code 自定义状态码 {@link GlobalServiceStatusCode}
     * @param msg 自定义异常信息
     * @return code对应的错误信息
     */
    public static SystemJsonResponse CUSTOMIZE_MSG_ERROR(GlobalServiceStatusCode code, String msg) {
        return new SystemJsonResponse(code.getCode(), Optional.ofNullable(msg).orElse(code.getMessage()));
    }
}
