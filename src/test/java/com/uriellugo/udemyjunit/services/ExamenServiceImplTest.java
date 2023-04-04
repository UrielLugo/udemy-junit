package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;
import com.uriellugo.udemyjunit.repositories.ExamenRepository;
import com.uriellugo.udemyjunit.repositories.ExamenRepositoryImpl;
import com.uriellugo.udemyjunit.repositories.PreguntasRepository;
import com.uriellugo.udemyjunit.repositories.PreguntasRepositoryImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl examenRepository;

    @Mock
    PreguntasRepositoryImpl preguntasRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    private List<Examen> examList;
    private Examen mathExam;
    private List<String> mathQuestions;
    private Examen exam;

    @BeforeEach
    void setUp() {
        this.examList = DatosExamen.getListOfExamenes();
        this.mathExam = DatosExamen.getMathExamen();
        this.mathQuestions = DatosExamen.getMathPreguntas();
        this.exam = DatosExamen.getExamen();
    }

    @Test
    void test_findExamenPorNombre() {

        // Cuando...
        when(examenRepository.findAll()).thenReturn(examList); // Si es llamado el metodo findAll() devuelve la lista de datos simulados
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
                .thenReturn(Optional.of(mathExam));
        when(preguntasRepository.findQuestionsByExamenId(anyLong()))
                .thenReturn(mathQuestions);

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
                .thenReturn(Optional.of(mathExam));
        when(preguntasRepository.findQuestionsByExamenId(DatosExamen.MATH_ID))
                .thenReturn(mathQuestions);

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
                .thenReturn(Optional.of(mathExam));
        when(preguntasRepository.findQuestionsByExamenId(anyLong()))
                .thenReturn(mathQuestions);

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
        when(examenRepository.findAll()).thenReturn(examList); // Si es llamado el metodo findAll() devuelve la lista de datos simulados

        // Llamada a metodo
        Optional<Examen> examenOptional = service.findExamenPorNombre("Antropología");

        // Entonces... - Validaciones (Aserciones)
        assertFalse(examenOptional.isPresent());

        // Verifica que algún método se ejecutó
        verify(examenRepository).findAll();
        verify(examenRepository, times(1)).findAll(); // Se verifica que solo se ejecute el metodo 'n' times()
    }

    @Test
    void test_guardarExamen() {

        Examen newExamen = exam;
        newExamen.setPreguntas(mathQuestions);

        // Cuando...
        when(examenRepository.guardar(any(Examen.class))).thenReturn(newExamen);

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
    void test_guardarExamenWithIncrementalId_anonymousClass() {
        Examen newExamen = new Examen(null, "Sistemas Embebidos");

        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {

            Long sequency = 10L;

            @Override
            public Examen answer(InvocationOnMock invocation) {
                Examen examen = invocation.getArgument(0);
                examen.setId(sequency++);
                return examen;
            }
        });

        // Then
        Examen examen = service.guardar(newExamen);
        assertNotNull(examen);
        assertNotNull(examen.getId());
        assertEquals(10L, examen.getId());
        assertEquals("Sistemas Embebidos", examen.getNombre());
    }

    @Test
    void test_guardarExamenWithIncrementalId_lambdaExpression() {
        Examen newExamen = exam;
        newExamen.setPreguntas(mathQuestions);

        when(examenRepository.guardar(any(Examen.class))).then(invocationOnMock -> {
            long sequency = 10L;
            Examen examen = invocationOnMock.getArgument(0);
            examen.setId(sequency);
            return examen;
        });

        // Then
        Examen examen = service.guardar(newExamen);
        assertNotNull(examen);
        assertNotNull(examen.getId());
        assertEquals(10L, examen.getId());
        assertEquals("Física", examen.getNombre());
    }

    @Test
    void test_examenWithException() {
        when(examenRepository.findExamenById(isNull())).thenReturn(Optional.of(new Examen(null, "Corrupted")));
        when(preguntasRepository.findQuestionsByExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(RuntimeException.class, () -> service.findExamenByIdWithQuestions(null));

        assertEquals(IllegalArgumentException.class, exception.getClass(),
                "No es una excepción de tipo: " + IllegalArgumentException.class.getSimpleName());

        verify(examenRepository).findExamenById(isNull());
        verify(preguntasRepository).findQuestionsByExamenId(isNull());
    }

    @Test
    void test_argumentMatchers1() {
        when(examenRepository.findExamenById(anyLong())).thenReturn(Optional.of(mathExam));
        when(preguntasRepository.findQuestionsByExamenId(anyLong())).thenReturn(mathQuestions);

        service.findExamenByIdWithQuestions(anyLong());

        verify(examenRepository).findExamenById(anyLong());
        verify(preguntasRepository).findQuestionsByExamenId(DatosExamen.MATH_ID);
        //verify(preguntasRepository).findQuestionsByExamenId(ArgumentMatchers.eq(5L)); // Métodos estáticos de ArgumentMatchers
        verify(preguntasRepository).findQuestionsByExamenId(Mockito.argThat(arg -> arg != null && arg.equals(DatosExamen.MATH_ID)));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Mensaje personalizado de error que imprime mockito en caso de que falle el test\n" +
                    "Debe ser un entero positivo: " + argument;
        }

    }

    @Test
    void test_argumentMatchers2_anonymousClass() {
        Examen examen = mathExam;
        //examen.setId(-2L); // Throw error from MiArgsMatchers
        when(examenRepository.findExamenById(anyLong())).thenReturn(Optional.of(examen));
        when(preguntasRepository.findQuestionsByExamenId(anyLong())).thenReturn(mathQuestions);

        service.findExamenByIdWithQuestions(anyLong());

        verify(examenRepository).findExamenById(anyLong());
        verify(preguntasRepository).findQuestionsByExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    void test_argumentMatchers3_lambda() {
        Examen examen = mathExam;
        //examen.setId(-2L); // Throw error from MiArgsMatchers
        when(examenRepository.findExamenById(anyLong())).thenReturn(Optional.of(examen));
        when(preguntasRepository.findQuestionsByExamenId(anyLong())).thenReturn(mathQuestions);

        service.findExamenByIdWithQuestions(anyLong());

        verify(examenRepository).findExamenById(anyLong());
        verify(preguntasRepository).findQuestionsByExamenId(argThat(argument -> argument != null && argument > 0));
    }

    @Test
    void test_argumentCaptor1() {
        when(examenRepository.findAll()).thenReturn(examList);

        service.findExamenPorNombre("Matemáticas");

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        verify(preguntasRepository).findQuestionsByExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void test_argumentCaptor2() {
        when(examenRepository.findAll()).thenReturn(examList);

        service.findExamenPorNombre("Matemáticas");

        verify(preguntasRepository).findQuestionsByExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void test_doThrow() {
        doThrow(IllegalArgumentException.class).when(preguntasRepository).guardarVarias(anyList());
        exam.setPreguntas(mathQuestions);
        assertThrows(IllegalArgumentException.class, () -> service.guardar(exam));
    }

    @Test
    void test_doAnswer() {
        when(examenRepository.findAll()).thenReturn(examList);

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? mathQuestions : Collections.emptyList();
        }).when(preguntasRepository).findQuestionsByExamenId(anyLong());

        Optional<Examen> examenOpt = service.findExamenPorNombre("Matemáticas");
        assertTrue(examenOpt.isPresent());
        Examen examen = examenOpt.get();
        System.out.println(examen);
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertFalse(examen.getPreguntas().isEmpty());

        verify(preguntasRepository).findQuestionsByExamenId(anyLong());
    }

    @Test
    void test_doAnswer2() {
        exam.setPreguntas(mathQuestions);

        doAnswer(invocationOnMock -> {
        long sequency = 10L;
        Examen examen = invocationOnMock.getArgument(0);
        examen.setId(sequency);
        return examen;
        }).when(examenRepository).guardar(any(Examen.class));

        // Then
        Examen examen = service.guardar(exam);
        assertNotNull(examen);
        assertNotNull(examen.getId());
        assertEquals(10L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(preguntasRepository).guardarVarias(anyList());
        verify(examenRepository, times(1)).guardar(any(Examen.class));
    }

    @Test
    void test_doCallRealMethod() {
        when(examenRepository.findAll()).thenReturn(examList);
        // Aquí llama a un método mock, esta vez llamaremos al método real
        //when(preguntasRepository.findQuestionsByExamenId(anyLong())).thenReturn(mathQuestions);

        // Este método se debería de user solamente para invocar métodos reales de terceros
        // Alguna clase, servicio o API de terceros
        doCallRealMethod().when(preguntasRepository).findQuestionsByExamenId(anyLong());

        Optional<Examen> examenOpt = service.findExamenPorNombre("Matemáticas");
        assertTrue(examenOpt.isPresent());
        Examen examen = examenOpt.get();
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    void test_spy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntasRepository preguntasRepository = spy(PreguntasRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntasRepository);

        // El método doReturn sirve para que el espía sustituya la implementación real con la del retorno
        doReturn(examList).when(examenRepository).findAll();

        List<Examen> allExams = examenService.findAllExams();
        assertFalse(allExams.isEmpty());
        Examen math = allExams.stream().filter(p -> p.getNombre().equals("Matemáticas")).findAny().orElse(null);
        assertNotNull(math);
        assertEquals("Matemáticas", math.getNombre());
        assertEquals(2, allExams.size());

        verify(examenRepository).findAll();
        verify(preguntasRepository, times(0)).findQuestionsByExamenId(anyLong());
    }

    @Test
    void test_ordenInvocaciones() {
        when(examenRepository.findAll()).thenReturn(examList);

        service.findExamenPorNombre("Matemáticas");
        service.findExamenPorNombre("Historia");

        InOrder inOrder = inOrder(preguntasRepository);
        inOrder.verify(preguntasRepository).findQuestionsByExamenId(5L);
        inOrder.verify(preguntasRepository).findQuestionsByExamenId(7L);
    }

    @Test
    void test_ordenInvocaciones2() {
        when(examenRepository.findAll()).thenReturn(examList);

        service.findExamenPorNombre("Matemáticas");
        service.findExamenPorNombre("Historia");

        InOrder inOrder = inOrder(examenRepository, preguntasRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).findQuestionsByExamenId(5L);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).findQuestionsByExamenId(7L);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void test_verify_numeroInvocaciones() {
        when(examenRepository.findAll()).thenReturn(examList);

        service.findExamenPorNombre("Matemáticas");

        verify(preguntasRepository).findQuestionsByExamenId(5L); // Por default es times(1)
        verify(preguntasRepository, times(1)).findQuestionsByExamenId(5L);
        verify(preguntasRepository, atLeast(1)).findQuestionsByExamenId(5L);
        verify(preguntasRepository, atLeastOnce()).findQuestionsByExamenId(5L);
        verify(preguntasRepository, atMost(1)).findQuestionsByExamenId(5L);
        verify(preguntasRepository, atMostOnce()).findQuestionsByExamenId(5L);
    }

    @Test
    void test_verify_numeroInvocaciones2() {
        when(examenRepository.findAll()).thenReturn(examList);

        service.findExamenPorNombre("Matemáticas");
        service.findExamenPorNombre("Historia");

        //verify(preguntasRepository).findQuestionsByExamenId(anyLong()); // Por default es times(1) FALLA
        verify(preguntasRepository, times(2)).findQuestionsByExamenId(anyLong());
        verify(preguntasRepository, atLeast(1)).findQuestionsByExamenId(anyLong());
        verify(preguntasRepository, atLeastOnce()).findQuestionsByExamenId(anyLong());
        verify(preguntasRepository, atMost(2)).findQuestionsByExamenId(anyLong());
        //verify(preguntasRepository, atMostOnce()).findQuestionsByExamenId(anyLong()); FALLA
    }

    @Test
    void test_verify_numeroInvocaciones3() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());

        service.findExamenPorNombre("Matemáticas");

        verify(preguntasRepository, never()).findQuestionsByExamenId(5L);
        verifyNoInteractions(preguntasRepository);

        verify(examenRepository).findAll();
        verify(examenRepository, times(1)).findAll();
        verify(examenRepository, atLeast(1)).findAll();
        verify(examenRepository, atLeastOnce()).findAll();
        verify(examenRepository, atMost(1)).findAll();
        verify(examenRepository, atMostOnce()).findAll();
    }
}