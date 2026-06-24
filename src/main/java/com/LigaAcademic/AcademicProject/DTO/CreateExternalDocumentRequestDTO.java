package com.ligaacademic.academicproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateExternalDocumentRequestDTO(

        @NotBlank
        @Size(max = 150)
        String title,

        @Size(max = 500)
        String description,

        @NotBlank
        @Size(max = 1000)
        @URL(protocol = "https")
        String externalUrl
) {
}
