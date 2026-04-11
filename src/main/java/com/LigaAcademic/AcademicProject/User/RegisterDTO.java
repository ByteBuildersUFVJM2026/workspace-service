package com.LigaAcademic.AcademicProject.User;

import com.fasterxml.jackson.annotation.JsonProperty;


public record RegisterDTO(String email, String password, UsersRoles role) {

}