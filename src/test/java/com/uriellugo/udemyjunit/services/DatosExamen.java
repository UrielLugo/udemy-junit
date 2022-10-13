package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Esta clase la escribí cuando aún no tenía noción de como funcionaban las librerías de testing,
 * por lo que ahora sé que usar valores 'static final' no es la mejor práctica para datos de prueba,
 * ya que al ser aleatorios y sin guardar un estado sobre otro test, no debe conservar su estado como objeto,
 * sino que debe mutar para siempre empezar con los mismos datos iniciales, lo podría cambiar pero sinceramente,
 * prefiero usar ese tiempo en otras cosas, además de que quede como recordatorio de lo que no se debe de hacer
 */
public class DatosExamen {

    public static final Long MATH_ID = 5L;

    public static final List<Examen> LIST_OF_EXAMENES = Arrays.asList(new Examen(MATH_ID, "Matemáticas"),
                new Examen(7L, "Historia"));

    public static final Examen MATH_EXAMEN = LIST_OF_EXAMENES.get(0);

    public static final List<String> MATH_PREGUNTAS = Arrays.asList("Aritmetica", "Integrales", "Derivadas", "Trigonometria", "Geometría");

    public static final Examen EXAMEN = new Examen(8L, "Física");
}
