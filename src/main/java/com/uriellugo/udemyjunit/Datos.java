package com.uriellugo.udemyjunit;

import com.uriellugo.udemyjunit.models.Banco;
import com.uriellugo.udemyjunit.models.Cuenta;

import java.math.BigDecimal;

public class Datos {

    // Instancias de Clase
    public static final Cuenta CUENTA_01 = new Cuenta(1L, "Andres", new BigDecimal("1000"));
    public static final Cuenta CUENTA_02 = new Cuenta(2L, "Paula", new BigDecimal("2000"));
    public static final Banco BANCO_01 = new Banco(1L, "BNMEX", 0);

    // Creación de Instancias de Objetos a traves de metodos

    public static Cuenta crearCuenta01() {
        return new Cuenta(1L, "Andres", new BigDecimal("1000"));
    }

    public static Cuenta crearCuenta02() {
        return new Cuenta(2L, "Paula", new BigDecimal("2000"));
    }

    public static Banco crearBanco01() {
        return new Banco(1L, "BNMEX", 0);
    }
}
