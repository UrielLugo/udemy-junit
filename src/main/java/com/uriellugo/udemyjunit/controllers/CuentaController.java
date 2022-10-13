package com.uriellugo.udemyjunit.controllers;

import com.uriellugo.udemyjunit.models.Cuenta;
import com.uriellugo.udemyjunit.models.TransaccionDto;
import com.uriellugo.udemyjunit.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> listar() {
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> detalle(@PathVariable long id) {
        Cuenta cuenta;
        try {
            cuenta = cuentaService.findById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta) {
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<Map<String, Object>> transferir(@RequestBody TransaccionDto transaccion) {
        cuentaService.transferir(transaccion.getCuentaOrigen(), transaccion.getCuentaDestino(),
                transaccion.getMonto(), transaccion.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", transaccion);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cuentaService.deleteById(id);
    }
}