package com.ligaacademic.academicproject.documentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 30)
    private DocumentSourceType sourceType;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "content_type", length = 150)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "bucket_name", length = 100)
    private String bucketName;

    @Column(name = "object_key", length = 500)
    private String objectKey;

    @Column(name = "external_url", length = 1000)
    private String externalUrl;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected Document() {
    }

    public static Document internalFile(
            String title,
            String description,
            String originalFileName,
            String contentType,
            Long fileSize,
            String bucketName,
            String objectKey,
            UUID createdBy
    ) {
        Document document = new Document();
        document.id = UUID.randomUUID();
        document.title = title;
        document.description = description;
        document.sourceType = DocumentSourceType.INTERNAL_FILE;
        document.originalFileName = originalFileName;
        document.contentType = contentType;
        document.fileSize = fileSize;
        document.bucketName = bucketName;
        document.objectKey = objectKey;
        document.createdBy = createdBy;
        return document;
    }

    public static Document externalLink(
            String title,
            String description,
            String externalUrl,
            UUID createdBy
    ) {
        Document document = new Document();
        document.id = UUID.randomUUID();
        document.title = title;
        document.description = description;
        document.sourceType = DocumentSourceType.EXTERNAL_LINK;
        document.externalUrl = externalUrl;
        document.createdBy = createdBy;
        return document;
    }

    public void softDelete(UUID deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isInternalFile() {
        return DocumentSourceType.INTERNAL_FILE.equals(this.sourceType);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public DocumentSourceType getSourceType() {
        return sourceType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
