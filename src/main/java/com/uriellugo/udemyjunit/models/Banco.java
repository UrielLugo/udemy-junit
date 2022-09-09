package com.uriellugo.udemyjunit.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco implements Cloneable, Serializable {

    private Long id;
    private String nombre;
    private List<Cuenta> cuentas;
    private Integer totalTransferencia;

    public Banco() {
        cuentas = new ArrayList<>();
    }

    public Banco(Long id, String nombre, Integer totalTransferencia) {
        this.id = id;
        this.nombre = nombre;
        this.totalTransferencia = totalTransferencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        for (Cuenta c: cuentas) {
            c.setBanco(this);
        }
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        cuenta.setBanco(this);
        this.cuentas.add(cuenta);
    }

    public void addCuenta(List<Cuenta> cuentas) {
        for (Cuenta c: cuentas) {
            c.setBanco(this);
            this.cuentas.add(c);
        }
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
        origen.debito(monto);
        destino.credito(monto);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotalTransferencia() {
        return totalTransferencia;
    }

    public void setTotalTransferencia(Integer totalTransferencia) {
        this.totalTransferencia = totalTransferencia;
    }

    @Override
    public Banco clone() throws CloneNotSupportedException {
        return (Banco)super.clone();
    }
}
