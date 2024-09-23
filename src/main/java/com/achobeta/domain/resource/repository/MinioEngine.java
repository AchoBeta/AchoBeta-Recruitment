package com.achobeta.domain.resource.repository;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.config.MinioConfig;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.HttpServletUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        PutObjectArgs objectArgs = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        //文件名称相同会覆盖
        minioClient.putObject(objectArgs);
        return fileName;
    }

    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) throws Exception {
        // 查看文件地址
        GetPresignedObjectUrlArgs objectUrlArgs = new GetPresignedObjectUrlArgs().builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
        return minioClient.getPresignedObjectUrl(objectUrlArgs);
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        }
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
