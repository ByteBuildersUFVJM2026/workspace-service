package com.LigaAcademic.AcademicProject.DTO;

import com.LigaAcademic.AcademicProject.User.UsersRoles;


public record RegisterDTO(String email, String password, UsersRoles role) {

}