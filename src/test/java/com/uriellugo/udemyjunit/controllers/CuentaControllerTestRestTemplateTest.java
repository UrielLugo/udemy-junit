package com.uriellugo.udemyjunit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uriellugo.udemyjunit.models.Cuenta;
import com.uriellugo.udemyjunit.models.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integracion_rt")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerTestRestTemplateTest {

    private final TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private Integer port;

    @Autowired
    CuentaControllerTestRestTemplateTest(TestRestTemplate client) {
        this.client = client;
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        System.out.println("Requesting to port: " + port + " at " + LocalTime.now().toString());
    }

    @Test
    @Order(1)
    void test_transaccion() throws JsonProcessingException {
        TransaccionDto dto = new TransaccionDto();
        dto.setMonto(new BigDecimal(100));
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setBancoId(1L);

        ResponseEntity<String> responseEntity = client.postForEntity("/api/cuentas/transferir", dto, String.class);
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        String json = responseEntity.getBody();
        System.out.println(json);
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito"));

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con éxito", jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigen").asLong());

        Map<String, Object> response2 = new HashMap<>();
        response2.put("date", LocalDate.now().toString());
        response2.put("status", "OK");
        response2.put("mensaje", "Transferencia realizada con éxito");
        response2.put("transaccion", dto);

        assertEquals(objectMapper.writeValueAsString(response2), json);
    }

    @Test
    @Order(2)
    void test_detalle() {
        ResponseEntity<Cuenta> responseEntity = client.getForEntity("/api/cuentas/1", Cuenta.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        Cuenta cuenta = responseEntity.getBody();
        assertNotNull(cuenta);
        System.out.println(cuenta);

        assertEquals("Andres", cuenta.getPersona());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
        assertEquals(new Cuenta(1L, "Andres", new BigDecimal("900.00")), cuenta);
    }

    @Test
    @Order(3)
    void test_listar() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> responseEntity = client.getForEntity("/api/cuentas", Cuenta[].class);
        assertNotNull(responseEntity.getBody());
        List<Cuenta> cuentas = Arrays.asList(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        assertEquals(2, cuentas.size());
        assertEquals(1L, cuentas.get(0).getId());
        assertEquals("Andres", cuentas.get(0).getPersona());
        assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());
        assertEquals(2L, cuentas.get(1).getId());
        assertEquals("John", cuentas.get(1).getPersona());
        assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cuentas);
        System.out.println(json);
        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals(1L, jsonNode.get(0).path("id").asLong());
        assertEquals("Andres", jsonNode.get(0).path("persona").asText());
        assertEquals("900.0", jsonNode.get(0).path("saldo").toString());
        assertEquals(2L, jsonNode.get(1).path("id").asLong());
        assertEquals("John", jsonNode.get(1).path("persona").asText());
        assertEquals("2100.0", jsonNode.get(1).path("saldo").toString());
    }

    @Test
    @Order(4)
    void test_guardar() {
        Cuenta cuenta = new Cuenta(null, "Pepa", new BigDecimal(4000));

        ResponseEntity<Cuenta> responseEntity = client.postForEntity("/api/cuentas", cuenta, Cuenta.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Cuenta cuentaCreada = responseEntity.getBody();
        assertNotNull(cuentaCreada);
        assertEquals(3L, cuentaCreada.getId());
        assertEquals("Pepa", cuentaCreada.getPersona());
        assertEquals("4000", cuentaCreada.getSaldo().toPlainString());
    }

    @Test
    @Order(5)
    void test_eliminar() {

        ResponseEntity<Cuenta[]> responseEntity = client.getForEntity("/api/cuentas", Cuenta[].class);
        assertNotNull(responseEntity.getBody());
        List<Cuenta> cuentas = Arrays.asList(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(3, cuentas.size());

        //client.delete("/api/cuentas/3", Cuenta[].class);
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> deleteResponse = client.exchange("/api/cuentas/3", HttpMethod.DELETE, null, Void.class, pathVariables);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertFalse(deleteResponse.hasBody());

        responseEntity = client.getForEntity("/api/cuentas", Cuenta[].class);
        assertNotNull(responseEntity.getBody());
        List<Cuenta> cuentas2 = Arrays.asList(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(2, cuentas2.size());

        ResponseEntity<Cuenta> detailResponse = client.getForEntity("/api/cuentas/3", Cuenta.class);
        assertEquals(HttpStatus.NOT_FOUND, detailResponse.getStatusCode());
        assertFalse(detailResponse.hasBody());
    }
}