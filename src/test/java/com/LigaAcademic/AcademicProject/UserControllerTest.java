package com.ligaacademic.academicproject;

import com.ligaacademic.academicproject.dto.CreateUserRequestDTO;
import com.ligaacademic.academicproject.dto.CreateUserResponseDTO;
import com.ligaacademic.academicproject.infra.security.SecurityConfigurations;
import com.ligaacademic.academicproject.infra.security.TokenService;
import com.ligaacademic.academicproject.infra.security.ratelimity.LoginRateLimitFilter;
import com.ligaacademic.academicproject.user.UsersRoles;
import com.ligaacademic.academicproject.controller.UserController;
import com.ligaacademic.academicproject.repository.UsersRepository;
import com.ligaacademic.academicproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfigurations.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    // Dependências dos filtros de segurança — necessários para o contexto subir
    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private LoginRateLimitFilter loginRateLimitFilter;

    @BeforeEach
    void configurarFiltroMock() throws Exception {
        doAnswer(inv -> {
            ServletRequest req = inv.getArgument(0);
            ServletResponse res = inv.getArgument(1);
            FilterChain chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(loginRateLimitFilter).doFilter(any(), any(), any());
    }

    @Nested
    class criarUsuario {

        @Test
        @WithMockUser(roles = "ADMIN")
        void deveRetornar201ComBodyCorretoQuandoAdminCriarUsuario() throws Exception {
            CreateUserRequestDTO request = new CreateUserRequestDTO(
                    "novo@gmail.com", "senha123@"
            );
            CreateUserResponseDTO response = new CreateUserResponseDTO(
                    UUID.randomUUID(), "novo@gmail.com", UsersRoles.ROLE_USER
            );

            when(userService.createUser(any())).thenReturn(response);

            mockMvc.perform(post("/admin/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("novo@gmail.com"))
                    .andExpect(jsonPath("$.role").value("ROLE_USER"));
        }

        @Test
        @WithMockUser(roles = "USER")
        void deveRetornar403QuandoNaoForAdmin() throws Exception {
            CreateUserRequestDTO request = new CreateUserRequestDTO(
                    "novo@gmail.com", "senha123@"
            );

            mockMvc.perform(post("/admin/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void deveRetornar400QuandoEmailForInvalido() throws Exception {
            CreateUserRequestDTO request = new CreateUserRequestDTO(
                    "emailinvalido", "senha123@"
            );

            mockMvc.perform(post("/admin/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    class promoverUsuario {

        @Test
        @WithMockUser(roles = "ADMIN")
        void deveRetornar204QuandoPromoverUsuario() throws Exception {
            mockMvc.perform(patch("/admin/users/{email}/promote", "usuario@gmail.com"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "USER")
        void deveRetornar403QuandoNaoForAdmin() throws Exception {
            mockMvc.perform(patch("/admin/users/{email}/promote", "usuario@gmail.com"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class deletarUsuario {

        @Test
        @WithMockUser(roles = "ADMIN")
        void deveRetornar204QuandoDeletarUsuario() throws Exception {
            mockMvc.perform(delete("/admin/users/{email}", "usuario@gmail.com"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "USER")
        void deveRetornar403QuandoNaoForAdmin() throws Exception {
            mockMvc.perform(delete("/admin/users/{email}", "usuario@gmail.com"))
                    .andExpect(status().isForbidden());
        }
    }
}
