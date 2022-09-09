package com.uriellugo.udemyjunit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Examen {

    private Long id;
    private String nombre;
    private List<String> preguntas;

    public Examen(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.preguntas = new ArrayList<>();
    }

}
