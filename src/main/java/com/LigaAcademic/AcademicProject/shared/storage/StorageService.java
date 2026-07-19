package com.ligaacademic.academicproject.shared.storage;

import java.io.InputStream;

public interface StorageService {

    void upload(String objectKey, InputStream content, long size, String contentType);

    StoredObject download(String objectKey);

    void delete(String objectKey);

    String bucketName();
}
