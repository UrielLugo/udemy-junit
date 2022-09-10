package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoRepository extends JpaRepository<Banco, Long> {

    Optional<Banco> findByNombre(String nombre);
}