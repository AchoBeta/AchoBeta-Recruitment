package com.achobeta.monio.engine;

import com.achobeta.domain.resource.util.ResourceUtil;
import com.achobeta.monio.config.MinioConfig;
import com.achobeta.util.MediaUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MinioEngine {

    private final MinioConfig minioConfig;

    private final MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean 是否存在
     */
    public boolean bucketExists(String bucketName) throws Exception {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        return minioClient.bucketExists(bucketExistsArgs);
    }

    /**
     * 创建存储bucket
     */
    public void makeBucket(String bucketName) throws Exception {
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.makeBucket(makeBucketArgs);
    }

    /**
     * 删除存储bucket
     */
    public void removeBucket(String bucketName) throws Exception {
        RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.removeBucket(removeBucketArgs);
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }


    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) throws Exception {
        String originalFilename = ResourceUtil.getOriginalName(file);
        String suffix = ResourceUtil.getFileNameSuffix(originalFilename);
        String uniqueFileName = ResourceUtil.getUniqueFileName(suffix);
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(uniqueFileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        //文件名称相同会覆盖
        minioClient.putObject(objectArgs);
        return uniqueFileName;
    }

    /**
     * 获取 url
     *
     * @param fileName
     * @return
     */
    public String getObjectUrl(String fileName) throws Exception {
        // 查看文件地址
        GetPresignedObjectUrlArgs objectUrlArgs = new GetPresignedObjectUrlArgs().builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
        return minioClient.getPresignedObjectUrl(objectUrlArgs);
    }

    /**
     * 文件预览
     *
     * @param fileName 文件名称
     */
    public void preview(String fileName, HttpServletResponse response) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build();
        try (GetObjectResponse objectResponse = minioClient.getObject(objectArgs);
             ServletOutputStream stream = response.getOutputStream()) {
            byte[] data = MediaUtil.getBytes(objectResponse);
            // 设置响应内容类型
            String suffix = ResourceUtil.getFileNameSuffix(fileName);
            response.setContentType(ResourceUtil.getContentType(objectResponse, suffix));
            // 指定字符集
            response.setCharacterEncoding("utf-8");
            // 指定下载的文件名
            stream.write(data);
            stream.flush();
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     */
    public void download(String downloadName, String fileName, HttpServletResponse response) throws Exception {
        // 在设置内容类型之前设置下载的文件名称
        response.addHeader("Content-Disposition", "attachment;fileName=" + downloadName);
        preview(fileName, response);
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
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public void remove(String fileName) throws Exception {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build();
        minioClient.removeObject(removeObjectArgs);
    }

}
