package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.DTO.CreateUserRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.CreateUserResponseDTO;
import com.LigaAcademic.AcademicProject.Infra.Exceptions.ConflictException;
import com.LigaAcademic.AcademicProject.User.User;
import com.LigaAcademic.AcademicProject.User.UsersRoles;
import com.LigaAcademic.AcademicProject.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
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