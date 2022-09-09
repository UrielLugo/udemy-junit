package com.uriellugo.udemyjunit.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
public class DatosPersonales {

    private List<String> listas;

    private String calle;

    private String codigoPostal;

    private String avenida;

    private String delegacion;

    private String ciudad;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @Builder
    public DatosPersonales(@Singular List<String> listas, String calle, String codigoPostal, String avenida, String delegacion, String ciudad) {
        this.listas = listas;
        this.calle = calle;
        this.codigoPostal = codigoPostal;
        this.avenida = avenida;
        this.delegacion = delegacion;
        this.ciudad = ciudad;
    }

    @Builder(builderMethodName = "builderGeneric", builderClassName = "DatosGenericosBuilder")
    static DatosPersonales ciudadGenerica(String delegacion, String avenida, String codigoPostal) {
        return builder()
                .calle("Calle 1")
                .codigoPostal("11000")
                .avenida(delegacion)
                .delegacion(avenida)
                .ciudad("CDMX")
                .build();
    }

}
