package com.LigaAcademic.AcademicProject.controller;

import com.LigaAcademic.AcademicProject.Infra.security.TokenService;
import com.LigaAcademic.AcademicProject.User.LoginResponseDTO;
import com.LigaAcademic.AcademicProject.User.RegisterDTO;
import com.LigaAcademic.AcademicProject.User.RegisterDTO;
import com.LigaAcademic.AcademicProject.User.AutheticationDTO;
import com.LigaAcademic.AcademicProject.User.User;
import com.LigaAcademic.AcademicProject.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {



    private TokenService tokenService;
    private AuthenticationManager authenticationManager;
    private UsersRepository usersRepository;

    public AuthenticationController(TokenService tokenService, AuthenticationManager authenticationManager, UsersRepository usersRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AutheticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User)auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDTO data){
        System.out.println("Dados recebidos" + data.email());
        if(this.usersRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.email(),encryptedPassword,data.role());

        this.usersRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

}
