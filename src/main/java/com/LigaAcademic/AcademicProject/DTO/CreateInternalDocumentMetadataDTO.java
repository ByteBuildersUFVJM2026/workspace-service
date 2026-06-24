package com.ligaacademic.academicproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateInternalDocumentMetadataDTO(

        @NotBlank
        @Size(max = 150)
        String title,

        @Size(max = 500)
        String description
) {
}
