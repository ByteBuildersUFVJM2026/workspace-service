package com.ligaacademic.academicproject.controller;

import com.ligaacademic.academicproject.dto.CreateExternalDocumentRequestDTO;
import com.ligaacademic.academicproject.dto.CreateInternalDocumentMetadataDTO;
import com.ligaacademic.academicproject.dto.DocumentResponseDTO;
import com.ligaacademic.academicproject.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
@PreAuthorize("hasRole('DIRETOR')")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponseDTO> upload(
            @Valid @RequestPart("metadata") CreateInternalDocumentMetadataDTO metadata,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.createInternalDocument(metadata, file));
    }

    @PostMapping("/link")
    public ResponseEntity<DocumentResponseDTO> createExternalLink(
            @Valid @RequestBody CreateExternalDocumentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.createExternalDocument(dto));
    }

    @GetMapping
    public ResponseEntity<Page<DocumentResponseDTO>> list(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(documentService.listActive(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.findActiveById(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable UUID id) {
        var download = documentService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.contentType()))
                .contentLength(download.fileSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(download.originalFileName(), StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(download.resource());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        documentService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
