package com.uriellugo.udemyjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uriellugo.udemyjunit.models.Cuenta;
import com.uriellugo.udemyjunit.models.TransaccionDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integracion_wc")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerWebClientTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    ObjectMapper objectMapper;

    TransaccionDto transaccionDto;
    Map<String, Object> response = new HashMap<>();
    Cuenta cuenta;

    @BeforeEach
    void setUp() {
        transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigen(1L);
        transaccionDto.setCuentaDestino(2L);
        transaccionDto.setBancoId(1L);
        transaccionDto.setMonto(new BigDecimal(100));

        response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", transaccionDto);
    }

    @Test
    @Order(1)
    void test_transferir() throws JsonProcessingException {
        // When
        client.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDto)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(respuesta -> {
                    try {
                        JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                        System.out.println("\nJSON ----------------------");
                        System.out.println(json.toPrettyString());
                        assertEquals("Transferencia realizada con éxito", json.path("mensaje").asText());
                        assertEquals(1L, json.path("transaccion").path("cuentaOrigen").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaccion").path("monto").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.mensaje").isNotEmpty()
                // Tres maneras de comparar el mensaje
                .jsonPath("$.mensaje").value(Matchers.is("Transferencia realizada con éxito"))
                .jsonPath("$.mensaje").value(valor -> assertEquals("Transferencia realizada con éxito", valor))
                .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con éxito")
                .jsonPath("$.transaccion.cuentaOrigen").isEqualTo(transaccionDto.getCuentaOrigen())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void test_detalle() throws JsonProcessingException {

        cuenta = new Cuenta(1L, "Andres", new BigDecimal("900"));
        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Andres")
                .jsonPath("$.saldo").isEqualTo(900)
                .json(objectMapper.writeValueAsString(cuenta));
    }

    @Test
    @Order(3)
    void test_detalle2() {

        client.get().uri("/api/cuentas/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    cuenta = response.getResponseBody();
                    assertNotNull(cuenta);
                    assertEquals("John", cuenta.getPersona());
                    assertEquals(2100, cuenta.getSaldo().doubleValue());
                });
    }

    @Test
    @Order(4)
    void test_listar() {
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(response -> {
                    try {
                        byte[] responseBody = response.getResponseBody();
                        JsonNode jsonNode = objectMapper.readTree(responseBody);
                        System.out.println("\nJSON ----------------------");
                        System.out.println(jsonNode.toPrettyString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].persona").isEqualTo("Andres")
                .jsonPath("$[0].saldo").isEqualTo(900)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].persona").isEqualTo("John")
                .jsonPath("$[1].saldo").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(Matchers.hasSize(2));
    }

    @Test
    @Order(5)
    void test_listar2() {
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertNotNull(cuentas);
                    assertEquals(1L, cuentas.get(0).getId());
                    assertEquals("Andres", cuentas.get(0).getPersona());
                    assertEquals(900.0, cuentas.get(0).getSaldo().doubleValue());
                    assertEquals(2L, cuentas.get(1).getId());
                    assertEquals("John", cuentas.get(1).getPersona());
                    assertEquals(2100.00, cuentas.get(1).getSaldo().doubleValue());
                    assertEquals(2, cuentas.size());
                    try {
                        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cuentas);
                        System.out.println("\nJSON ----------------------");
                        System.out.println(json);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                })
                .value(Matchers.hasSize(2))
                .hasSize(2);
    }

    @Test
    @Order(6)
    void test_guardar() {
        // Given
        cuenta = new Cuenta(3L, "Pepe", new BigDecimal(3000));
        // When
        client.post().uri("api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                // Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("3")
                .jsonPath("$.persona").isEqualTo("Pepe")
                .jsonPath("$.persona").value(Matchers.is("Pepe"))
                .jsonPath("$.saldo").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void test_guardar2() {
        // Given
        cuenta = new Cuenta(4L, "Alejandra", new BigDecimal(3500));
        // When
        client.post().uri("api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                // Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta c = response.getResponseBody();
                    assertNotNull(c);
                    assertEquals(4L, c.getId());
                    assertEquals("Alejandra", c.getPersona());
                    assertEquals(3500.0, c.getSaldo().doubleValue());
                    try {
                        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(c);
                        System.out.println("\nJSON ----------------------");
                        System.out.println(json);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                })
                .isEqualTo(cuenta);
    }

    @Test
    @Order(8)
    void test_eliminar() {
        client.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    System.out.println("Accounts before elimination ----------------------");
                    System.out.println(cuentas);
                })
                .hasSize(4);

        client.delete().uri("api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();

        client.get().uri("/api/cuentas")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    System.out.println("Accounts after elimination ----------------------");
                    System.out.println(cuentas);
                })
                .hasSize(3);

        client.get().uri("api/cuentas/3")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}