package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.DTO.AutheticationDTO;
import com.LigaAcademic.AcademicProject.DTO.UserProfileResponseDTO;
import com.LigaAcademic.AcademicProject.DTO.LoginResponseDTO;
import com.LigaAcademic.AcademicProject.Infra.security.TokenService;
import com.LigaAcademic.AcademicProject.User.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated AutheticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> me(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(new UserProfileResponseDTO(user.getEmail(), user.getRole(), user.getCreatedAt()));
    }
}