package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Cuenta;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CuentaRepository {

    List<Cuenta> findAll();

    Cuenta findById(Long id);

    void update(Cuenta cuenta);
}
