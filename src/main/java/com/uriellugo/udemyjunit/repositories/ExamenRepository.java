package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Examen;

import java.util.List;
import java.util.Optional;

public interface ExamenRepository {

    Examen guardar(Examen examen);

    List<Examen> findAll();

    Optional<Examen> findExamenById(Long id);
}