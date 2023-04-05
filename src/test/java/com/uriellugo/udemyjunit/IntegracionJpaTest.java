package com.uriellugo.udemyjunit;

import com.uriellugo.udemyjunit.models.Banco;
import com.uriellugo.udemyjunit.models.Cuenta;
import com.uriellugo.udemyjunit.repositories.BancoRepository;
import com.uriellugo.udemyjunit.repositories.CuentaRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Tag("integracion_jpa")
@DataJpaTest
class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    BancoRepository bancoRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void test_findById() {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findById(1L);
        assertTrue(cuentaOpt.isPresent());

        Cuenta cuenta = cuentaOpt.get();
        assertEquals("Andres", cuenta.getPersona());
        assertEquals(1000.0, cuenta.getSaldo().doubleValue());
    }

    @Test
    void test_findByPersona() {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByPersona("Andres");
        assertTrue(cuentaOpt.isPresent());

        Cuenta cuenta = cuentaOpt.get();
        assertEquals(1, cuenta.getId());
        assertEquals("Andres", cuenta.getPersona());
        assertEquals(1000d, cuenta.getSaldo().doubleValue());
    }

    @Test
    void test_whenFindByPersona_thenThrowException() {
        Optional<Cuenta> cuentaOpt = cuentaRepository.findByPersona("PersonaNull");
        assertFalse(cuentaOpt.isPresent());
        assertThrows(NoSuchElementException.class, () -> cuentaOpt.orElseThrow(NoSuchElementException::new));
    }

    @Test
    void test_findAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void test_save() {

        // Given
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal(3000));
        cuentaPepe.setBanco(entityManager.find(Banco.class, 1L));
        cuentaRepository.save(cuentaPepe);

        // When
        Cuenta cuenta = cuentaRepository.findByPersona("Pepe").orElseThrow(NoSuchElementException::new);

        // Then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals(3000d, cuenta.getSaldo().doubleValue());
        assertEquals(3, cuenta.getId());
        assertNotNull(cuenta.getBanco());
    }

    @Test
    void test_saveBanco() {
        // Given
        Banco bancoSantaMex = new Banco(3L, "SantaMex", 0);
        bancoRepository.save(bancoSantaMex);

        // When
        Banco banco = bancoRepository.findByNombre("SantaMex").orElseThrow(NoSuchElementException::new);

        // Then
        assertEquals("SantaMex", banco.getNombre());
        assertEquals(0, banco.getTotalTransferencia());
    }

    @Test
    void test_updateCuenta() {
        // Given
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal(3000));
        cuentaPepe.setBanco(entityManager.find(Banco.class, 1L));
        Cuenta cuenta = cuentaRepository.save(cuentaPepe);

        assertEquals("Pepe", cuenta.getPersona());
        assertEquals(3000d, cuenta.getSaldo().doubleValue());
        assertNotNull(cuenta.getBanco());

        // When
        cuenta.setSaldo(new BigDecimal(3000));
        cuenta.setPersona("Ignacio");
        Cuenta cuentaUpdate = cuentaRepository.save(cuenta);

        // Then
        assertEquals("Ignacio", cuentaUpdate.getPersona());
        assertEquals(3000d, cuentaUpdate.getSaldo().doubleValue());
    }

    @Test
    void test_deleteCuenta() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow(NoSuchElementException::new);
        assertEquals("John", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> cuentaRepository.findById(2L));

        assertEquals(1, cuentaRepository.findAll().size());
    }
}
