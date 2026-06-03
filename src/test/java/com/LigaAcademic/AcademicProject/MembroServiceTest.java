package com.LigaAcademic.AcademicProject;

import com.LigaAcademic.AcademicProject.DTO.MembroRequestDTO;
import com.LigaAcademic.AcademicProject.DTO.MembroResponseDTO;
import com.LigaAcademic.AcademicProject.Infra.Exceptions.ConflictException;
import com.LigaAcademic.AcademicProject.Mapper.MembroMapper;
import com.LigaAcademic.AcademicProject.model.Membro;
import com.LigaAcademic.AcademicProject.repository.GuildasRepository;
import com.LigaAcademic.AcademicProject.repository.MembroRepository;
import com.LigaAcademic.AcademicProject.service.MembroService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembroServiceTest {

    @InjectMocks
    private MembroService membroService;

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private GuildasRepository guildasRepository;

    @Mock
    private MembroMapper membroMapper;

    @Nested
    class registrarMembro {

        @Test
        void deveSalvarMembroQuandoMatriculaNaoExiste() {
            MembroRequestDTO dto = new MembroRequestDTO("Arthur", "Membro", "12345678901", "arthur@email.com");

            Membro entidade = new Membro();
            entidade.setMatricula("12345678901");
            entidade.setNome("Arthur");

            MembroResponseDTO responseEsperado = new MembroResponseDTO("Arthur", "12345678901", "Membro", "arthur@email.com", 0f, List.of());

            when(membroRepository.existsByMatricula("12345678901")).thenReturn(false);
            when(membroMapper.paraEntidade(dto)).thenReturn(entidade);
            when(membroRepository.save(entidade)).thenReturn(entidade);
            when(membroMapper.paraResponseDTO(entidade)).thenReturn(responseEsperado);

            MembroResponseDTO resultado = membroService.registrarMembro(dto);

            assertEquals(responseEsperado, resultado);
            verify(membroRepository, times(1)).save(entidade);
        }

        @Test
        void deveLancarConflictExceptionQuandoMatriculaJaExiste() {
            MembroRequestDTO dto = new MembroRequestDTO("Arthur", "Membro", "12345678901", "arthur@email.com");

            when(membroRepository.existsByMatricula("12345678901")).thenReturn(true);

            assertThrows(ConflictException.class,
                    () -> membroService.registrarMembro(dto));

            verify(membroRepository, never()).save(any());
        }
    }
}