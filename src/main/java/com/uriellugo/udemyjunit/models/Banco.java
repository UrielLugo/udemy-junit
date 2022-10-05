package com.uriellugo.udemyjunit.models;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
@Entity
@Table(name = "bancos")
public class Banco implements Cloneable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private transient List<Cuenta> cuentas;

    @Column(name = "total_transferencias")
    private int totalTransferencia;

    public Banco() {
        cuentas = new ArrayList<>();
    }

    public Banco(Long id, String nombre, List<Cuenta> cuentas, int totalTransferencia) {
        this.id = id;
        this.nombre = nombre;
        this.cuentas = cuentas;
        this.totalTransferencia = totalTransferencia;
    }

    private Banco(String... hola) {
        Arrays.stream(hola).map(s -> s + " ").forEach(System.out::print);
    }

    private Banco(int i) {
        System.out.println("Valor en constructor: " + i);
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
