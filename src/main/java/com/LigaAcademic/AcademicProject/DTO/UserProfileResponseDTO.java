package com.ligaacademic.academicproject.dto;

import com.ligaacademic.academicproject.user.UsersRoles;

import java.time.LocalDateTime;

public record UserProfileResponseDTO(String email, UsersRoles role, LocalDateTime createdAt) {}
