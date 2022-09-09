package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;
import com.uriellugo.udemyjunit.repositories.ExamenRepository;
import com.uriellugo.udemyjunit.repositories.PreguntasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    ExamenRepository examenRepository;
    PreguntasRepository preguntasRepository;
    ExamenService service;

    @BeforeEach
    void setUp() {
        // Mockito, usa los contratos dados por la interfaz solamente, no utiliza ninguna implementación en caso que la haya.
        // Dado que...
        this.examenRepository = mock(ExamenRepository.class);
        this.preguntasRepository = mock(PreguntasRepository.class);
        this.service = new ExamenServiceImpl(examenRepository, preguntasRepository);
    }

    @Test
    void test_findExamenPorNombre() {

        // Cuando...
        when(examenRepository.findAll()).thenReturn(DatosExamen.LIST_OF_EXAMENES); // Si es llamado el metodo findAll() devuelve la lista de datos simulados
        // Llamada a metodo
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        // Validaciones (Aserciones)
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.get().getId());
        assertEquals("Matemáticas", examen.get().getNombre());
    }

    @Test
    void test_findExamenByNombreEmptyList() {

        // Mockito, usa los contratos dados por la interfaz solamente, no utiliza ninguna implementación en caso que la haya.
        // Cuando...
        when(examenRepository.findAll()).thenReturn(Collections.emptyList()); // Si es llamado el metodo findAll() devuelve la lista de datos simulados

        // Llamada a metodo
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        // Entonces... - Validaciones (Aserciones)
        assertFalse(examen.isPresent());
    }

    @Test
    void test_findExamenByIdWithQuestions() {
        when(examenRepository.findExamenById(anyLong()))
                .thenReturn(Optional.of(DatosExamen.MATH_EXAMEN));
        when(preguntasRepository.findQuestionsByExamenId(anyLong()))
                .thenReturn(DatosExamen.MATH_PREGUNTAS);

        Examen examen = service.findExamenByIdWithQuestions(DatosExamen.MATH_ID);

        // Entonces... - Validaciones
        assertNotNull(examen);
        assertNotNull(examen.getPreguntas());
        assertFalse(examen.getPreguntas().isEmpty());
    }

    @Test
    void test_findMathExamenWithQuestions() {
        // Cuando...
        when(examenRepository.findExamenById(DatosExamen.MATH_ID))
                .thenReturn(Optional.of(DatosExamen.MATH_EXAMEN));
        when(preguntasRepository.findQuestionsByExamenId(DatosExamen.MATH_ID))
                .thenReturn(DatosExamen.MATH_PREGUNTAS);

        Examen examen = service.findExamenByIdWithQuestions(DatosExamen.MATH_ID);

        // Entonces... - Validaciones
        assertNotNull(examen.getPreguntas());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmetica"));
    }

    @Test
    void test_findMathExamenWithQuestionsVerify() {
        // Cuando...
        when(examenRepository.findExamenById(anyLong()))
                .thenReturn(Optional.of(DatosExamen.MATH_EXAMEN));
        when(preguntasRepository.findQuestionsByExamenId(anyLong()))
                .thenReturn(DatosExamen.MATH_PREGUNTAS);

        Examen examen = service.findExamenByIdWithQuestions(DatosExamen.MATH_ID);

        // Entonces... - Validaciones
        assertNotNull(examen.getPreguntas());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        // Verifica que algún método se ejecutó
        verify(examenRepository).findExamenById(DatosExamen.MATH_ID);
        verify(preguntasRepository).findQuestionsByExamenId(DatosExamen.MATH_ID);

    }

    @Test
    void test_examenNotFoundVerify() {

        // Cuando...
        when(examenRepository.findAll()).thenReturn(DatosExamen.LIST_OF_EXAMENES); // Si es llamado el metodo findAll() devuelve la lista de datos simulados

        // Llamada a metodo
        Optional<Examen> examenOptional = service.findExamenPorNombre("Antropología");

        // Entonces... - Validaciones (Aserciones)
        assertFalse(examenOptional.isPresent());

        // Verifica que algún método se ejecutó
        verify(examenRepository).findAll();
        verify(examenRepository, times(1)).findAll(); // Se verifica que solo se ejecute el metodo 'n' times()
    }

    @Test
    void test_guardar_examen() {

        Examen newExamen = DatosExamen.EXAMEN;
        newExamen.setPreguntas(DatosExamen.MATH_PREGUNTAS);

        // Cuando...
        when(examenRepository.guardar(any(Examen.class))).thenReturn(DatosExamen.EXAMEN);

        // LLamada a método
        Examen examen = service.guardar(newExamen);

        // Entonces... - Validaciones
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntasRepository).guardarVarias(anyList()); // Si no tiene preguntas, el metodo, service.guardar no ejecuta guardarVarias()
    }

    @Test
    void test_guardar_examen_id_incremental_clase_anonima() {
        Examen newExamen = DatosExamen.EXAMEN;
        newExamen.setPreguntas(DatosExamen.MATH_PREGUNTAS);

        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {

            Long sequency = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) {
                Examen examen = invocation.getArgument(0);
                examen.setId(sequency++);
                return examen;
            }
        });
    }

    @Test
    void test_guardar_examen_id_incremental_lambda() {
        Examen newExamen = DatosExamen.EXAMEN;
        newExamen.setPreguntas(DatosExamen.MATH_PREGUNTAS);

        //when(examenRepository.guardar(any(Examen.class))).then()
    }
}