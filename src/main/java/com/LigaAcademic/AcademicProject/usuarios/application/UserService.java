package com.ligaacademic.academicproject.usuarios.application;

import com.ligaacademic.academicproject.usuarios.api.CreateUserRequestDTO;
import com.ligaacademic.academicproject.usuarios.api.CreateUserResponseDTO;
import com.ligaacademic.academicproject.shared.exceptions.ConflictException;
import com.ligaacademic.academicproject.shared.auditoria.AuditarAcao;
import com.ligaacademic.academicproject.usuarios.domain.User;
import com.ligaacademic.academicproject.usuarios.domain.UsersRoles;
import com.ligaacademic.academicproject.usuarios.infra.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public CreateUserResponseDTO createUser(CreateUserRequestDTO dto) {
        if (usersRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Email já cadastrado.");
        }
        User newUser = new User(dto.email(), passwordEncoder.encode(dto.password()), UsersRoles.ROLE_USER);
        User saved = usersRepository.save(newUser);
        return new CreateUserResponseDTO(saved.getId(), saved.getEmail(), saved.getRole());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @AuditarAcao(acao = "Promoveu alguém para admin")
    @Transactional
    public void promoteToAdmin(String email) {


        if (!usersRepository.existsByEmail(email)) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        usersRepository.updateRoleByEmail(email, UsersRoles.ROLE_ADMIN);
    }


    @Transactional
    public void deleteUser(String email) {
        if (!usersRepository.existsByEmail(email)) {
            throw new EntityNotFoundException("Usuário para remover não encontrado");
        }
        usersRepository.deleteByEmail(email);
    }
}
