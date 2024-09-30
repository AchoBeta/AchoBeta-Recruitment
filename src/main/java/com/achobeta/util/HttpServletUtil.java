package com.achobeta.util;

import com.achobeta.exception.GlobalServiceException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 23:51
 */
public class HttpServletUtil {

    public static Optional<ServletRequestAttributes> getAttributes() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static Optional<HttpServletRequest> getRequest() {
        return getAttributes().map(ServletRequestAttributes::getRequest);
    }

    public static Optional<HttpServletResponse> getResponse() {
        return getAttributes().map(ServletRequestAttributes::getResponse);
    }

    public static void returnBytes(byte[] bytes, HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 写入数据
            if(Objects.nonNull(bytes)) {
                // 设置响应内容类型（用同一个 inputStream 会互相影响）
                response.setContentType(MediaUtil.getContentType(bytes));
                // 指定字符集
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static void returnBytes(String downloadName, byte[] bytes, HttpServletResponse response) {
        // 在设置内容类型之前设置下载的文件名称
        response.addHeader("Content-Disposition", "attachment; fileName=" + HttpRequestUtil.encodeString(downloadName));
        returnBytes(bytes, response);
    }

    public static String getHost(HttpServletRequest request) {
        return String.format("%s://%s", request.getScheme(), request.getHeader("host"));
    }

    public static String getBaseUrl(HttpServletRequest request, String... uris) {
        String uri = Arrays.stream(uris).filter(StringUtils::hasText).collect(Collectors.joining());
        return getHost(request) + uri;
    }

}
