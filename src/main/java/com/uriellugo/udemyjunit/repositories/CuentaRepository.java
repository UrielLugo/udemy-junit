package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.persona=?1") // Forma imperativa
    Optional<Cuenta> findByPersona(String persona); // Forma declarativa si va sin @Query
}