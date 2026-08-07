package com.ligaacademic.academicproject.shared.seeder;

import com.ligaacademic.academicproject.usuarios.domain.User;
import com.ligaacademic.academicproject.usuarios.domain.UsersRoles;
import com.ligaacademic.academicproject.usuarios.infra.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usersRepository.existsByRole(UsersRoles.ROLE_ADMIN)) {
            return;
        }


        String email = System.getenv("ADMIN_EMAIL");
        String rawPassword = System.getenv("ADMIN_PASSWORD");

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalStateException("ERRO CRÍTICO DE SEGURANÇA: A variável de ambiente ADMIN_PASSWORD não foi configurada no servidor. A aplicação não pode iniciar.");
        }


        User admin = new User(email, passwordEncoder.encode(rawPassword), UsersRoles.ROLE_ADMIN);
        usersRepository.save(admin);

        System.out.println("Usuário Admin criado com sucesso a partir de variáveis de ambiente.");
    }
}
