package com.LigaAcademic.AcademicProject.DTO;

import com.LigaAcademic.AcademicProject.User.UsersRoles;

import java.time.LocalDateTime;

public record UserProfileResponseDTO(String email, UsersRoles role, LocalDateTime createdAt) {}