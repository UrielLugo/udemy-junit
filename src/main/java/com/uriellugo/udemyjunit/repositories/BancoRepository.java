package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long> {
}