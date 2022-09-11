package com.uriellugo.udemyjunit.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransaccionDto {
    private Long cuentaOrigen;
    private Long cuentaDestino;
    private Long bancoId;
    private BigDecimal monto;
}
