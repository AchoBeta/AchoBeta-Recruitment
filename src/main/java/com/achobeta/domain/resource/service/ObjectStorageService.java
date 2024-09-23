package com.achobeta.domain.resource.service;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 10:29
 */
public interface ObjectStorageService {

    /**
     * 文件上传
     *
     * @param file 文件
     * @return code
     */
    String upload(MultipartFile file);

    /**
     * 预览图片
     *
     * @param fileName
     * @return url
     */
    String preview(String fileName);

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     */
    void download(String fileName, HttpServletResponse res);

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    void remove(String fileName);

}
