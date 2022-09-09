package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;

import java.util.List;
import java.util.Optional;

public interface ExamenService {

    Optional<Examen> findExamenPorNombre(String nombre);

    Examen findExamenByIdWithQuestions(Long id);

    Examen guardar(Examen examen);

    List<Examen> findAllExams();
}