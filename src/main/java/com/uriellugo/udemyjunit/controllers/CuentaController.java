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
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable long id) {
        return cuentaService.findById(id);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto transaccion) {
        cuentaService.transferir(transaccion.getCuentaOrigen(), transaccion.getCuentaDestino(),
                transaccion.getMonto(), transaccion.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", transaccion);

        return ResponseEntity.ok(response);
    }
}