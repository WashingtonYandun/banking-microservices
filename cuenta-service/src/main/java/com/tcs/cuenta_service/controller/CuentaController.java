package com.tcs.cuenta_service.controller;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.service.ICuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final ICuentaService svc;

    public CuentaController(ICuentaService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta c) {
        Cuenta nueva = svc.crear(c);
        return ResponseEntity.status(CREATED).body(nueva);
    }

    @GetMapping
    public List<Cuenta> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public Cuenta obtener(@PathVariable Long id) {
        return svc.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        svc.eliminar(id);
    }
}