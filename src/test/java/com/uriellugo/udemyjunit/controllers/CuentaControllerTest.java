package com.uriellugo.udemyjunit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uriellugo.udemyjunit.Datos;
import com.uriellugo.udemyjunit.models.Cuenta;
import com.uriellugo.udemyjunit.models.TransaccionDto;
import com.uriellugo.udemyjunit.services.CuentaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService cuentaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_detalle() throws Exception {
        // Given
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta01());

        // When
        mvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Andres"))
                .andExpect(jsonPath("$.saldo").value("1000"));

        verify(cuentaService).findById(1L);
    }

    @Test
    void test_transferir() throws Exception {

        // Given
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setBancoId(1L);
        transaccionDto.setCuentaOrigen(1L);
        transaccionDto.setCuentaDestino(2L);
        transaccionDto.setMonto(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", transaccionDto);

        // When
        mvc.perform(post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDto)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con éxito"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigen").value(transaccionDto.getCuentaOrigen()))
                .andExpect(jsonPath("$.transaccion.cuentaDestino").value(transaccionDto.getCuentaDestino()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void test_listar() throws Exception {
        // Given
        List<Cuenta> cuentas = Arrays.asList(Datos.crearCuenta01(), Datos.crearCuenta02());
        when(cuentaService.findAll()).thenReturn(cuentas);

        // When
        mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].persona").value("Andres"))
                .andExpect(jsonPath("$.[1].persona").value("Paula"))
                .andExpect(jsonPath("$.[0].saldo").value("1000"))
                .andExpect(jsonPath("$.[1].saldo").value("2000"))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));

    }

    @Test
    void test_guardar() throws Exception {
        // Given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal(3000));
        when(cuentaService.save(any())).thenAnswer(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // When
        mvc.perform(post("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.persona", Matchers.is("Pepe")))
                .andExpect(jsonPath("$.saldo", Matchers.is(3000)));

        verify(cuentaService).save(any());
    }
}