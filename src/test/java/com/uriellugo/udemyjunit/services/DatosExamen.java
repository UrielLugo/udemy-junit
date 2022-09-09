package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatosExamen {

    public static final Long MATH_ID = 5L;

    public static final List<Examen> LIST_OF_EXAMENES = Arrays.asList(new Examen(MATH_ID, "Matemáticas"),
                new Examen(7L, "Historia"));

    public static final Examen MATH_EXAMEN = LIST_OF_EXAMENES.get(0);

    public static final List<String> MATH_PREGUNTAS = Arrays.asList("Aritmetica", "Integrales", "Derivadas", "Trigonometria", "Geometría");

    public static final Examen EXAMEN = new Examen(8L, "Física");
}
