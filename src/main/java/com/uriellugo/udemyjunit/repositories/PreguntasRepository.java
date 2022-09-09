package com.uriellugo.udemyjunit.repositories;

import java.util.List;

public interface PreguntasRepository {
    List<String> findQuestionsByExamenId(Long id);

    void guardarVarias(List<String> preguntas);
}
