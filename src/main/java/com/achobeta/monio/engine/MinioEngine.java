package com.achobeta.monio.engine;

import com.achobeta.monio.config.MinioConfig;
import com.achobeta.util.HttpRequestUtil;
import com.achobeta.util.HttpServletUtil;
import com.achobeta.util.MediaUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MinioEngine {

    private final MinioConfig minioConfig;

    private final MinioClient minioClient;

    /**
     * 文件上传
     */
    public void upload(String fileName, byte[] bytes) throws Exception {
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(MediaUtil.getInputStream(bytes), bytes.length, -1) // 不分块
                .contentType(MediaUtil.getContentType(bytes))
                .build();
        //文件名称相同会覆盖
        minioClient.putObject(objectArgs);
    }

    /**
     * 获取 url
     */
    public String getObjectUrl(String fileName) throws Exception {
        // 查看文件地址
        GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .method(Method.GET) //这里必须显式声明请求方法
                .build();
        return minioClient.getPresignedObjectUrl(objectUrlArgs);
    }

    /**
     * 如果是基本的 url，不一定能够公网访问，需要设置 bucket 的权限
     * 如果不隐藏，那就是携带 queryString 的公网链接，是一定能够访问的
     * 隐藏链接 quertString 的访问签名（如果是 true，返回的链接可能会因为对象存储服务器的权限没打开而不多能访问）
     */
    public String getObjectUrl(String fileName, boolean hidden) throws Exception {
        // 查看文件地址
        String objectUrl = getObjectUrl(fileName);
        // 判断是否隐藏
        return Boolean.TRUE.equals(hidden) ? HttpRequestUtil.hiddenQueryString(objectUrl) : objectUrl;
    }

    /**
     * 文件加载
     */
    public byte[] load(String fileName) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build();
        try(GetObjectResponse objectResponse = minioClient.getObject(objectArgs)) {
            return MediaUtil.getBytes(objectResponse);
        }
    }

    /**
     * 文件预览
     */
    public void preview(String fileName, HttpServletResponse response) throws Exception {
        byte[] bytes = load(fileName);
        HttpServletUtil.returnBytes(bytes, response);
    }

    /**
     * 文件下载
     */
    public void download(String downloadName, String fileName, HttpServletResponse response) throws Exception {
        byte[] bytes = load(fileName);
        HttpServletUtil.returnBytes(downloadName, bytes, response);
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() throws Exception {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(minioConfig.getBucketName()).build();
        List<Item> items = new ArrayList<>();
        for (Result<Item> result : minioClient.listObjects(listObjectsArgs)) {
            items.add(result.get());
        }
        return items;
    }

    /**
     * 删除
     */
    public void remove(String fileName) throws Exception {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build();
        minioClient.removeObject(removeObjectArgs);
    }

}
