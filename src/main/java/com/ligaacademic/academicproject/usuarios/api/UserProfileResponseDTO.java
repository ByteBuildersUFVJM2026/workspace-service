package com.ligaacademic.academicproject.usuarios.api;

import com.ligaacademic.academicproject.usuarios.domain.UsersRoles;

import java.time.LocalDateTime;

public record UserProfileResponseDTO(String email, UsersRoles role, LocalDateTime createdAt) {}
