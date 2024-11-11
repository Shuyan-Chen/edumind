package com.shuyan.edumind.service.impl;


import com.shuyan.edumind.configuration.property.SystemConfig;
import com.shuyan.edumind.service.FileUpload;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileUploadImpl implements FileUpload {
    private final Logger logger = LoggerFactory.getLogger(FileUpload.class);
    private final SystemConfig systemConfig;

    @Autowired
    public FileUploadImpl(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    @Override
    public String uploadFile(InputStream inputStream, long size, String extName) {
        String bucketName = systemConfig.getMinioConfig().getBucket();
        String objectName = System.currentTimeMillis() + "." + extName;
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .contentType("application/octet-stream")
                    .build();
            systemConfig.getMinioConfig().minioClient().putObject(args);
            return systemConfig.getMinioConfig().getUrl() + "/" + objectName;
        } catch (Exception e) {
            logger.error("Failed to upload file: {}", e.getMessage(), e);
        }
        return null;
    }


}


