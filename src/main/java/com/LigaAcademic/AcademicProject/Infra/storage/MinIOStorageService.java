package com.ligaacademic.academicproject.infra.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class MinIOStorageService implements StorageService {

    private final MinioClient minioClient;
    private final MinIOProperties properties;

    public MinIOStorageService(MinioClient minioClient, MinIOProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
    }

    @PostConstruct
    public void ensureBucketExists() {
        if (!properties.createBucketOnStartup()) {
            return;
        }

        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(properties.bucket())
                    .build());

            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.bucket())
                        .build());
            }
        } catch (Exception e) {
            throw new DocumentStorageException("Erro ao preparar bucket de documentos", e);
        }
    }

    @Override
    public void upload(String objectKey, InputStream content, long size, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectKey)
                    .stream(content, size, -1L)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            throw new DocumentStorageException("Erro ao salvar arquivo no storage", e);
        }
    }

    @Override
    public StoredObject download(String objectKey) {
        try {
            InputStream content = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectKey)
                    .build());
            return new StoredObject(content, null);
        } catch (Exception e) {
            throw new DocumentStorageException("Erro ao buscar arquivo no storage", e);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.bucket())
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            throw new DocumentStorageException("Erro ao remover arquivo do storage", e);
        }
    }

    @Override
    public String bucketName() {
        return properties.bucket();
    }
}
