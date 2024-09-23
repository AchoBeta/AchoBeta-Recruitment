package com.achobeta.domain.resource.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

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
     * @return 资源 url
     */
    String getObjectUrl(String fileName);

    /**
     * 预览图片
     *
     * @param fileName
     * @param response
     */
    void preview(String fileName, HttpServletResponse response);

    /**
     * 文件下载
     *
     * @param downloadName 下载的文件名称
     * @param fileName 文件名称
     */
    void download(String downloadName, String fileName, HttpServletResponse response);

    /**
     * 删除
     *
     * @param fileName
     */
    void remove(String fileName);

}
