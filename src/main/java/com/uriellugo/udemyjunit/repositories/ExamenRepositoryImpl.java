package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Examen;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Clase de prueba para demostrar que ante diversos escenarios de prueba, el cambio de la implementación afecta al desarrollo y clean code
 */
public class ExamenRepositoryImpl implements ExamenRepository {

    @Override
    public Examen guardar(Examen examen) {
        return null;
    }

    @Override
    public List<Examen> findAll() {

        Examen examen1 = Examen.builder().id(1L).nombre("Antropología")
                .preguntas(
                        Arrays.asList("¿Qué es el antropocentrismo?", "¿Qué es un ritual?", "¿Qué representa la guadalupana en la idiosincracia mexicana?")
                ).build();
        Examen examen2 = Examen.builder()
                .id(2L).nombre("Programación")
                .preguntas(
                        Arrays.asList("¿Cuál fue el primer lenguaje de programación?", "¿Qué año fue creada la máquina enigma?", "Qué es un patrón de diseño?")
                ).build();
        Examen examen3 = Examen.builder()
                .id(3L).nombre("Español")
                .preguntas(
                        Arrays.asList("¿Qué es una sílaba?", "¿Qué es una entrevista?, ¿Cómo se compone una obra de teatro?", "¿Qué es un diptongo")
                ).build();
        Examen examen4 = Examen.builder()
                .id(4L).nombre("Geografía")
                .preguntas(
                        Arrays.asList("¿Como se puede interpretar la demografía de un país?", "¿Cuáles son los recursos más importantes de una zona", "¿Cuánto es la producción de leche al año mundialmente?", "¿De qué habla la geológia", "¿Cuantas capas de la atmosfera hay?")
                ).build();
        Examen examen5 = Examen.builder()
                .id(5L).nombre("Matemáticas")
                .preguntas(
                        Arrays.asList("¿Qué es un teorema?", "¿Qué es una ecuación?", "¿Cuantas aristas tiene un cubo?", "¿Cuál es la demostración de la derivada")
                ).build();
        Examen examen6 = Examen.builder()
                .id(6L).nombre("Física")
                .preguntas(null)
                .build();
        Examen examen7 = Examen.builder()
                .id(7L).nombre("Historia")
                .preguntas(
                        Arrays.asList("¿En que año inicio la WW2?", "¿Cuando fue la revolución francesa?")
                ).build();

        return Arrays.asList(examen1, examen2, examen3, examen4, examen5, examen6, examen7);

        /* Por cada escenario de pruebas no es eficiente alterar el metodo de la implementación, de ahí el uso de mocks para usar en tests
        return Collections.emptyList();
         */
    }

    @Override
    public Optional<Examen> findExamenById(Long id) {
        return Optional.of(new Examen(2L, "Computación"));
    }
}
