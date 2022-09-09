package com.uriellugo.udemyjunit.services;

import com.uriellugo.udemyjunit.exceptions.DineroInsuficienteException;
import com.uriellugo.udemyjunit.models.Banco;
import com.uriellugo.udemyjunit.models.Cuenta;
import com.uriellugo.udemyjunit.repositories.BancoRepository;
import com.uriellugo.udemyjunit.repositories.CuentaRepository;
import org.apache.commons.lang3.SerializationUtils;
import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static com.uriellugo.udemyjunit.Datos.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CuentaServiceImplTest {

    //@Mock
    @MockBean
    CuentaRepository cuentaRepository;

    //@Mock
    @MockBean
    BancoRepository bancoRepository;

    //@InjectMocks
    //CuentaServiceImpl cuentaService;

    @Autowired
    CuentaService cuentaService;

    @Autowired
    ApplicationContext applicationContext;

    @BeforeEach
    void setUp() throws CloneNotSupportedException {
        //cuentaRepository = Mockito.mock(CuentaRepository.class);
        //bancoRepository = Mockito.mock(BancoRepository.class);
        //cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);

        Mockito.when(cuentaRepository.findById(1L)).thenReturn(crearCuenta01());
        Mockito.when(cuentaRepository.findById(2L)).thenReturn(crearCuenta02());
        // Implements Cloneable <-> DeepCopy without same reference
        //Banco bancoClone = Datos.crearBanco01(); // Opción 1 - Crear instancias nuevas a través de un método
        // Banco bancoClone = Datos.BANCO_01.clone(); // Opción 2 - Object.clone() e Interface Cloneable
        Banco bancoClone = SerializationUtils.clone(BANCO_01); // Opción 3 - Apache commons SerializationUtils e Interface Serializable
        Mockito.when(bancoRepository.findById(1L)).thenReturn(bancoClone);
    }

    @DisplayName("Bean names")
    @Test
    void test_applicationContextBeans() {
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        // Los beans de los mocks se ven asi si se usa la integración de Mockito con Spring (@MockBean)
        // com.uriellugo.udemyjunit.repositories.CuentaRepository#0
        // com.uriellugo.udemyjunit.repositories.BancoRepository#0
    }

    @DisplayName("Transferir saldo")
    @Test
    void test_transferirSaldo() {

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);
        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        cuentaService.transferir(1L, 2L, new BigDecimal("100"), 1L);

        saldoOrigen =  cuentaService.revisarSaldo(1L);
        saldoDestino = cuentaService.revisarSaldo(2L);
        assertEquals("900", saldoOrigen.toPlainString());
        assertEquals("2100", saldoDestino.toPlainString());

        int total = cuentaService.revisarTotalTransferencias(1L);
        assertEquals(1, total);

        Mockito.verify(cuentaRepository, times(3)).findById(1L);
        Mockito.verify(cuentaRepository, times(3)).findById(2L);
        Mockito.verify(cuentaRepository, times(2)).update(any(Cuenta.class));
        Mockito.verify(bancoRepository, times(1)).update(any(Banco.class));

        verify(cuentaRepository, times(6)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();
    }

    @DisplayName("Exception Dinero Insuficiente")
    @Test
    void test_transferirSaldo_whenCuentaOrigenNotFound_thenThrows_DineroInsuficienteException() {

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);
        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        assertThrows(DineroInsuficienteException.class, () ->
                cuentaService.transferir(1L, 2L, new BigDecimal("1200"), 1L));

        int total = cuentaService.revisarTotalTransferencias(1L);
        assertEquals(0, total);

        saldoOrigen =  cuentaService.revisarSaldo(1L);
        saldoDestino = cuentaService.revisarSaldo(2L);
        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(2)).findById(2L);
        verify(cuentaRepository, never()).update(any(Cuenta.class));
        verify(bancoRepository, never()).update(any(Banco.class));

        verify(cuentaRepository, never()).findAll();
    }

    @DisplayName("findById sameInstance")
    @Test
    void test_transferirSaldo_whenFindById_thenSameInstanceObject() {
        Cuenta cuenta1 = cuentaService.findById(1L);
        Cuenta cuenta2 = cuentaService.findById(1L);

        assertSame(cuenta1, cuenta2);

        verify(cuentaRepository, times(2)).findById(1L);
    }
}