package com.ligaacademic.academicproject.controller;

import com.ligaacademic.academicproject.dto.CreateUserRequestDTO;
import com.ligaacademic.academicproject.dto.CreateUserResponseDTO;
import com.ligaacademic.academicproject.infra.auditoria.AuditarAcao;
import com.ligaacademic.academicproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @AuditarAcao(acao = "Usuário foi criado.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> criarUsuario(@RequestBody @Validated CreateUserRequestDTO dto) {
        CreateUserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AuditarAcao(acao = "Membro promovido para cargo admin")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{email}/promote")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable String email) {
        userService.promoteToAdmin(email);
        return ResponseEntity.noContent().build();
    }

    @AuditarAcao(acao = "Usuário foi deletado.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email){

        userService.deleteUser(email);

        return ResponseEntity.noContent().build();
    }
}
