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
     */
    String upload(Long userId, String originalName, byte[] bytes);

    String upload(Long userId, MultipartFile file);

    /**
     * 预览图片
     *
     */
    String getObjectUrl(String fileName, Boolean hidden);

    /**
     * 加载图片
     *
     */
    byte[] load(String fileName);

    /**
     * 预览图片
     *
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
     */
    void remove(String fileName);

    void blockUser(Long userId, Long blockDDL);
}
