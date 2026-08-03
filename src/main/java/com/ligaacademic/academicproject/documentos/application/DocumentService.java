package com.ligaacademic.academicproject.documentos.application;

import com.ligaacademic.academicproject.documentos.api.CreateExternalDocumentRequestDTO;
import com.ligaacademic.academicproject.documentos.api.CreateInternalDocumentMetadataDTO;
import com.ligaacademic.academicproject.documentos.api.DocumentDownloadDTO;
import com.ligaacademic.academicproject.documentos.api.DocumentResponseDTO;
import com.ligaacademic.academicproject.shared.storage.MinIOProperties;
import com.ligaacademic.academicproject.shared.storage.StorageService;
import com.ligaacademic.academicproject.documentos.domain.Document;
import com.ligaacademic.academicproject.documentos.infra.DocumentRepository;
import com.ligaacademic.academicproject.usuarios.domain.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentService {

    private static final Map<String, String> ALLOWED_CONTENT_TYPES_BY_EXTENSION = Map.of(
            "pdf", "application/pdf",
            "doc", "application/msword",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private final DocumentRepository documentRepository;
    private final StorageService storageService;
    private final MinIOProperties minIOProperties;

    public DocumentService(DocumentRepository documentRepository,
                           StorageService storageService,
                           MinIOProperties minIOProperties) {
        this.documentRepository = documentRepository;
        this.storageService = storageService;
        this.minIOProperties = minIOProperties;
    }

    @Transactional
    public DocumentResponseDTO createInternalDocument(CreateInternalDocumentMetadataDTO metadata, MultipartFile file) {
        validateFile(file);

        String originalFileName = sanitizeOriginalFileName(file.getOriginalFilename());
        String extension = getAllowedExtension(originalFileName);
        String objectKey = "documents/" + UUID.randomUUID() + "." + extension;

        boolean uploaded = false;
        try {
            storageService.upload(objectKey, file.getInputStream(), file.getSize(), file.getContentType());
            uploaded = true;

            Document document = Document.internalFile(
                    metadata.title(),
                    metadata.description(),
                    originalFileName,
                    file.getContentType(),
                    file.getSize(),
                    storageService.bucketName(),
                    objectKey,
                    currentUserId()
            );

            return toResponse(documentRepository.saveAndFlush(document));
        } catch (RuntimeException e) {
            if (uploaded) {
                try {
                    storageService.delete(objectKey);
                } catch (RuntimeException deleteException) {
                    e.addSuppressed(deleteException);
                }
            }
            throw e;
        } catch (IOException e) {
            throw new IllegalArgumentException("Arquivo invalido ou inacessivel", e);
        }
    }

    @Transactional
    public DocumentResponseDTO createExternalDocument(CreateExternalDocumentRequestDTO dto) {
        Document document = Document.externalLink(
                dto.title(),
                dto.description(),
                dto.externalUrl(),
                currentUserId()
        );
        return toResponse(documentRepository.save(document));
    }

    public Page<DocumentResponseDTO> listActive(Pageable pageable) {
        return documentRepository.findAllByDeletedAtIsNull(pageable)
                .map(this::toResponse);
    }

    public DocumentResponseDTO findActiveById(UUID id) {
        return toResponse(findActiveDocument(id));
    }

    public DocumentDownloadDTO download(UUID id) {
        Document document = findActiveDocument(id);
        if (!document.isInternalFile()) {
            throw new IllegalArgumentException("Documento externo nao possui arquivo para download");
        }

        var storedObject = storageService.download(document.getObjectKey());
        return new DocumentDownloadDTO(
                new InputStreamResource(storedObject.content()),
                document.getOriginalFileName(),
                document.getContentType(),
                document.getFileSize()
        );
    }

    @Transactional
    public void softDelete(UUID id) {
        Document document = findActiveDocument(id);
        document.softDelete(currentUserId());
        documentRepository.save(document);
    }

    private Document findActiveDocument(UUID id) {
        return documentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento com id " + id + " nao encontrado"));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo obrigatorio");
        }

        long maxFileSize = minIOProperties.maxFileSize().toBytes();
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Arquivo excede o limite de 25 MB");
        }

        String originalFileName = sanitizeOriginalFileName(file.getOriginalFilename());
        String extension = getAllowedExtension(originalFileName);
        String expectedContentType = ALLOWED_CONTENT_TYPES_BY_EXTENSION.get(extension);

        if (!expectedContentType.equals(file.getContentType())) {
            throw new IllegalArgumentException("Tipo do arquivo nao corresponde a extensao informada");
        }
    }

    private String sanitizeOriginalFileName(String originalFileName) {
        String cleanFileName = StringUtils.cleanPath(originalFileName == null ? "" : originalFileName);
        cleanFileName = StringUtils.getFilename(cleanFileName);

        if (!StringUtils.hasText(cleanFileName) || cleanFileName.contains("..")) {
            throw new IllegalArgumentException("Nome do arquivo invalido");
        }

        return cleanFileName;
    }

    private String getAllowedExtension(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);
        if (!StringUtils.hasText(extension)) {
            throw new IllegalArgumentException("Arquivo sem extensao");
        }

        extension = extension.toLowerCase(Locale.ROOT);
        if (!ALLOWED_CONTENT_TYPES_BY_EXTENSION.containsKey(extension)) {
            throw new IllegalArgumentException("Extensao de arquivo nao permitida");
        }

        return extension;
    }

    private UUID currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new IllegalArgumentException("Usuario autenticado invalido");
        }
        return user.getId();
    }

    private DocumentResponseDTO toResponse(Document document) {
        return new DocumentResponseDTO(
                document.getId(),
                document.getTitle(),
                document.getDescription(),
                document.getSourceType(),
                document.getOriginalFileName(),
                document.getContentType(),
                document.getFileSize(),
                document.getExternalUrl(),
                document.getCreatedBy(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
