package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.models.Examen;
import com.uriellugo.udemyjunit.repositories.ExamenRepository;
import com.uriellugo.udemyjunit.repositories.PreguntasRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private final ExamenRepository examenRepository;
    private final PreguntasRepository preguntasRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenByIdWithQuestions(Long id) {
        Optional<Examen> examenOptional = examenRepository.findExamenById(id);

        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.get();
            List<String> preguntas = preguntasRepository.findQuestionsByExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }

        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {

        if(!examen.getPreguntas().isEmpty()) {
            preguntasRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }

    @Override
    public List<Examen> findAllExams() {
        return examenRepository.findAll();
    }
}
