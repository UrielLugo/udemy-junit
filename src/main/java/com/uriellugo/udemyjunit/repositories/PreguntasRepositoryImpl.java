package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.services.DatosExamen;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

@Log4j2
public class PreguntasRepositoryImpl implements PreguntasRepository {
    @Override
    public List<String> findQuestionsByExamenId(Long id) {
        log.debug("PreguntasRepositoryImpl.findQuestionsByExamenId");
        return DatosExamen.getMathPreguntas();
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        log.debug("PreguntasRepositoryImpl.guardarVarias");
    }
}
