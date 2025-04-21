package com.tcs.cuenta_service.controller;

import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.service.IMovimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final IMovimientoService svc;

    public MovimientoController(IMovimientoService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<Movimiento> registrar(
            @RequestParam String numeroCuenta,
            @RequestBody Movimiento m) {
        Movimiento mov = svc.registrar(numeroCuenta, m);
        return ResponseEntity.status(CREATED).body(mov);
    }
}