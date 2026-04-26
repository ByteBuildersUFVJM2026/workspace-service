package com.LigaAcademic.AcademicProject.User;

import jakarta.persistence.Enumerated;


public enum UsersRoles {

    ROLE_ADMIN("ROLE_ADMIN"),

    ROLE_DIRETOR("ROLE_DIRETOR"),

    ROLE_USER("ROLE_USER");

    private String role;

   UsersRoles(String role){
        this.role = role;
    }

    public String getRole(){
       return role;
    }
}
