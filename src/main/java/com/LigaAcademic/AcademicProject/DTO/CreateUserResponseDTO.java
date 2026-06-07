package com.ligaacademic.academicproject.dto;

import com.ligaacademic.academicproject.user.UsersRoles;

import java.util.UUID;

public record CreateUserResponseDTO(UUID id, String email, UsersRoles role) {}
