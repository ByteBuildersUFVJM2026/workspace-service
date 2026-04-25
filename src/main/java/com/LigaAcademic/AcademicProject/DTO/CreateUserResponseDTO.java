package com.LigaAcademic.AcademicProject.DTO;

import com.LigaAcademic.AcademicProject.User.UsersRoles;

import java.util.UUID;

public record CreateUserResponseDTO(UUID id, String email, UsersRoles role) {}