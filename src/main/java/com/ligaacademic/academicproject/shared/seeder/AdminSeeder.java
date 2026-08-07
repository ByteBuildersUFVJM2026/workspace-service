package com.ligaacademic.academicproject.shared.seeder;

import com.ligaacademic.academicproject.usuarios.domain.User;
import com.ligaacademic.academicproject.usuarios.domain.UsersRoles;
import com.ligaacademic.academicproject.usuarios.infra.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public AdminSeeder(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_EMAIL:}") String adminEmail,
            @Value("${ADMIN_PASSWORD:}") String adminPassword) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (usersRepository.existsByRole(UsersRoles.ROLE_ADMIN)) {
            return;
        }


        if (adminEmail.isBlank()) {
            throw new IllegalStateException("ERRO CRÍTICO DE SEGURANÇA: A variável ADMIN_EMAIL não foi configurada. A aplicação não pode iniciar.");
        }
        if (adminPassword.isBlank()) {
            throw new IllegalStateException("ERRO CRÍTICO DE SEGURANÇA: A variável de ambiente ADMIN_PASSWORD não foi configurada no servidor. A aplicação não pode iniciar.");
        }

        User admin = new User(adminEmail, passwordEncoder.encode(adminPassword), UsersRoles.ROLE_ADMIN);
        usersRepository.save(admin);

        System.out.println("Usuário Admin criado com sucesso a partir de variáveis de ambiente.");
    }
}
