package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{email}/promote")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable String email) {
        userService.promoteToAdmin(email);
        return ResponseEntity.noContent().build();
    }
}