package com.LigaAcademic.AcademicProject;

import com.LigaAcademic.AcademicProject.Infra.Exceptions.ConflictException;
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

    @Nested
    class registrarMembro {

        @Test
        void verificarSeMembroComAMatriculaRegistradaJaExiste() {
            Membro membroNovo = new Membro();
            membroNovo.setMatricula("12345678901");
            membroNovo.setNome("Arthur");

            when(membroRepository.existsByMatricula("12345678901")).thenReturn(false);
            when(membroRepository.save(membroNovo)).thenReturn(membroNovo);

            Membro resultado = membroService.registrarMembro(membroNovo);

            assertEquals(membroNovo, resultado);
            verify(membroRepository, times(1)).save(membroNovo);
        }

        @Test
        void verificarSeMembroComAMatriculaRegistradaJaExisteELancarException() {

            Membro membroNovo = new Membro();
            membroNovo.setMatricula("12345678901");

            when(membroRepository.existsByMatricula("12345678901")).thenReturn(true);

            assertThrows(ConflictException.class,
                    () -> membroService.registrarMembro(membroNovo));

            verify(membroRepository, never()).save(any());
        }
    }
}