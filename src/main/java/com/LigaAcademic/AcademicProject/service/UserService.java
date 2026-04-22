package com.LigaAcademic.AcademicProject.service;

import com.LigaAcademic.AcademicProject.User.UsersRoles;
import com.LigaAcademic.AcademicProject.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void promoteToAdmin(String email) {
        if (!usersRepository.existsByEmail(email)) {
            throw new EntityNotFoundException("Usuário com email " + email + " não encontrado");
        }
        usersRepository.updateRoleByEmail(email, UsersRoles.ROLE_ADMIN);
    }
}