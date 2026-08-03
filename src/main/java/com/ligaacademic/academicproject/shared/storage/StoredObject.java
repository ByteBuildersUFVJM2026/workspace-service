package com.ligaacademic.academicproject.shared.storage;

import java.io.InputStream;

public record StoredObject(
        InputStream content,
        String contentType
) {
}
