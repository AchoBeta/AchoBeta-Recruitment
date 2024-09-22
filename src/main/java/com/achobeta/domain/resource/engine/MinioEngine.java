package com.achobeta.domain.resource.engine;

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
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioEngine {

    private final MinioConfig minioConfig;

    private final MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build();
            return minioClient.bucketExists(bucketExistsArgs);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(makeBucketArgs);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            RemoveBucketArgs removeBucketArgs = RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.removeBucket(removeBucketArgs);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }


    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            return fileName;
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        try {
            // 查看文件地址
            GetPresignedObjectUrlArgs objectUrlArgs = new GetPresignedObjectUrlArgs().builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .method(Method.GET)
                    .build();
            return minioClient.getPresignedObjectUrl(objectUrlArgs);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @return Boolean
     */
    public void download(String fileName) {
        HttpServletResponse res = HttpServletUtil.getResponse().orElseThrow(GlobalServiceException::new);
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
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {

        try {
            ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(minioConfig.getBucketName()).build();
            List<Item> items = new ArrayList<>();
            for (Result<Item> result : minioClient.listObjects(listObjectsArgs)) {
                items.add(result.get());
            }
            return items;
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
