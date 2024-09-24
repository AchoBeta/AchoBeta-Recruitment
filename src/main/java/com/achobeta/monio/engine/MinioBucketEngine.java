package com.achobeta.monio.engine;

import com.achobeta.monio.config.MinioConfig;
import com.achobeta.monio.template.DefaultPolicyTemplate;
import com.achobeta.template.engine.TextEngine;
import io.minio.*;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 14:49
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class MinioBucketEngine implements InitializingBean {

    private final static String DEFAULT_POLICY_TEMPLATE = "minio-bucket-policy.json";

    private final MinioConfig minioConfig;

    private final MinioClient minioClient;

    private final TextEngine textEngine;

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
     * 尝试创建存储bucket
     */
    public void tryMakeBucket(String bucketName) throws Exception {
        if(!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
    }

    public void setBucketPolicy(String bucketName, String policy) throws Exception {
        SetBucketPolicyArgs bucketPolicyArgs = SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy)
                .build();
        minioClient.setBucketPolicy(bucketPolicyArgs);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        String bucketName = minioConfig.getBucketName();
        tryMakeBucket(bucketName);
        // 设置规则：所有人都能读（否则就只能获取）
        DefaultPolicyTemplate policyTemplate = DefaultPolicyTemplate.builder()
                .bucketName(bucketName)
                .build();
        String policy = textEngine.builder()
                .append(DEFAULT_POLICY_TEMPLATE, policyTemplate)
                .build();
        setBucketPolicy(bucketName, policy);
    }
}
