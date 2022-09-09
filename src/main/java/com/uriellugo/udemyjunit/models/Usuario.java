package com.uriellugo.udemyjunit.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Builder
    public Usuario(String nombre, String apellido, String email, String numero, Byte[] pin, Integer edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.numero = numero;
        this.pin = pin;
        this.edad = edad;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String numero;

    private Byte[] pin = {12, 21};

    private Integer edad;

}
