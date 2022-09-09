package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.Banco;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BancoRepository {

    List<Banco> findAll();

    Banco findById(Long id);

    void update(Banco banco);
}
