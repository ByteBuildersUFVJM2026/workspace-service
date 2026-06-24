CREATE TABLE documents (
    id UUID PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    source_type VARCHAR(30) NOT NULL,
    original_file_name VARCHAR(255),
    content_type VARCHAR(150),
    file_size BIGINT,
    bucket_name VARCHAR(100),
    object_key VARCHAR(500),
    external_url VARCHAR(1000),
    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_by UUID,
    deleted_at TIMESTAMP,

    CONSTRAINT documents_source_type_check
        CHECK (source_type IN ('INTERNAL_FILE', 'EXTERNAL_LINK')),

    CONSTRAINT documents_internal_file_check
        CHECK (
            source_type <> 'INTERNAL_FILE'
            OR (
                original_file_name IS NOT NULL
                AND content_type IS NOT NULL
                AND file_size IS NOT NULL
                AND bucket_name IS NOT NULL
                AND object_key IS NOT NULL
                AND external_url IS NULL
            )
        ),

    CONSTRAINT documents_external_link_check
        CHECK (
            source_type <> 'EXTERNAL_LINK'
            OR (
                external_url IS NOT NULL
                AND original_file_name IS NULL
                AND content_type IS NULL
                AND file_size IS NULL
                AND bucket_name IS NULL
                AND object_key IS NULL
            )
        ),

    CONSTRAINT documents_file_size_positive_check
        CHECK (file_size IS NULL OR file_size > 0)
);

CREATE INDEX idx_documents_deleted_at ON documents (deleted_at);
CREATE INDEX idx_documents_created_at ON documents (created_at);
