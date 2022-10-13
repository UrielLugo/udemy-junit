package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatosExamen {

    public static final Long MATH_ID = 5L;

    public static List<Examen> getListOfExamenes() {
        return Arrays.asList(new Examen(MATH_ID, "Matemáticas"), new Examen(7L, "Historia"));
    }

    public static Examen getMathExamen() {
        return getListOfExamenes().get(0);
    }

    public static List<String> getMathPreguntas() {
        return Arrays.asList("Aritmetica", "Integrales", "Derivadas", "Trigonometria", "Geometría");
    }

    public static Examen getExamen() {
         return new Examen(8L, "Física");
    }
}
