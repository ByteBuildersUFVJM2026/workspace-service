package com.ligaacademic.academicproject.shared.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@ConfigurationProperties(prefix = "minio")
public record MinIOProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String bucket,
        DataSize maxFileSize,
        boolean createBucketOnStartup
) {
}
