package com.ligaacademic.academicproject;

import com.ligaacademic.academicproject.usuarios.api.CreateUserRequestDTO;
import com.ligaacademic.academicproject.usuarios.api.CreateUserResponseDTO;
import com.ligaacademic.academicproject.usuarios.api.UserProfileResponseDTO;
import com.ligaacademic.academicproject.shared.exceptions.ConflictException;
import com.ligaacademic.academicproject.usuarios.domain.User;
import com.ligaacademic.academicproject.usuarios.domain.UsersRoles;
import com.ligaacademic.academicproject.usuarios.infra.UsersRepository;
import com.ligaacademic.academicproject.usuarios.infra.UserMapper;
import com.ligaacademic.academicproject.usuarios.application.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Nested
    class listarTodos {

        @Test
        void deveRetornarUsuariosPaginadosMapeadosSemSenha() {
            var pageable = PageRequest.of(0, 20);
            var criadoEm = LocalDateTime.of(2026, 8, 6, 10, 0);
            var user = new User("usuario@gmail.com", "senha-hasheada", UsersRoles.ROLE_USER);
            var response = new UserProfileResponseDTO(user.getEmail(), user.getRole(), criadoEm);

            when(usersRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user), pageable, 1));
            when(userMapper.paraResponseDTO(user)).thenReturn(response);

            Page<UserProfileResponseDTO> resultado = userService.listarTodos(pageable);

            assertEquals(List.of(response), resultado.getContent());
            verify(userMapper).paraResponseDTO(user);
        }
    }

    @Nested
    class createUser {

        @Test
        void deveAtribuirRoleUserAoNovoUsuarioCriado() {

            CreateUserRequestDTO dtoUser = new CreateUserRequestDTO(
                    "emailteste@gmail.com",
                    "senhaSegura123@"
            );

            UUID fakeId = UUID.randomUUID();
            User usuarioSimulado = new User("emailteste@gmail.com", "senhaHasheadaFalsa", UsersRoles.ROLE_USER);
            ReflectionTestUtils.setField(usuarioSimulado, "id", fakeId);

            when(usersRepository.existsByEmail(dtoUser.email())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("senhaHasheadaFalsa");
            when(usersRepository.save(any(User.class))).thenReturn(usuarioSimulado);

            CreateUserResponseDTO usuarioCriado = userService.createUser(dtoUser);

            assertEquals(UsersRoles.ROLE_USER, usuarioCriado.role());
        }

        @Test
        void verificaSeOEmailJaEstaCadastradoERetornaUmaException(){

            CreateUserRequestDTO dtoTest = new CreateUserRequestDTO(
                    "fodasetest@gmail.com",
                    "senhaHasheadaFalsa"
            );

            when(usersRepository.existsByEmail(dtoTest.email())).thenReturn(true);

            assertThrows(ConflictException.class,
                    () ->userService.createUser(dtoTest));
        }
    }


    @Nested
    class promoteUser {

        @Test
        void deveAtualizarRoleQuandoUsuarioExistir() {

            String email = "emailteste@gmail.com";

            when(usersRepository.existsByEmail(email)).thenReturn(true);

            userService.promoteToAdmin(email);

            verify(usersRepository).updateRoleByEmail(email, UsersRoles.ROLE_ADMIN);
        }

        @Test
        void deveLancarExcecaoQuandoUsuarioNaoExistir() {
            String email = "naoexiste@gmail.com";

            when(usersRepository.existsByEmail(email)).thenReturn(false);

            assertThrows(EntityNotFoundException.class,
                    () -> userService.promoteToAdmin(email));
        }
    }

    @Nested
    class deleteUser {

        @Test
        void deveDeletarSeMembroExistir() {

            String emailTest = "naoexiste@gmail.com";

            when(usersRepository.existsByEmail(emailTest)).thenReturn(true);

            userService.deleteUser(emailTest);

            verify(usersRepository).deleteByEmail(emailTest);
        }

        @Test
        void deveLancarExceptionSeMembroPraDeletarNaoExistir() {

            String email = "naoexiste@gmail.com";

            when(usersRepository.existsByEmail(email)).thenReturn(false);

            assertThrows(EntityNotFoundException.class,
                    () -> userService.deleteUser(email));

        }
    }
}
