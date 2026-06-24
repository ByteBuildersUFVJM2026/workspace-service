package com.ligaacademic.academicproject.infra.storage;

import java.io.InputStream;

public record StoredObject(
        InputStream content,
        String contentType
) {
}
