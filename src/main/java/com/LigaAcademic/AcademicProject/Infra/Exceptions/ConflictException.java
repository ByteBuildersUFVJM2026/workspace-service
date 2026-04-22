package com.LigaAcademic.AcademicProject.Infra.Exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}