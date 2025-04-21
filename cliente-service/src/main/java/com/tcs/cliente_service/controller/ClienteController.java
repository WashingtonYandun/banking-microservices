package com.tcs.cliente_service.controller;

import com.tcs.cliente_service.domain.Cliente;
import com.tcs.cliente_service.exception.ServiceError;
import com.tcs.cliente_service.service.IClienteService;
import com.tcs.cliente_service.util.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final IClienteService svc;

    public ClienteController(IClienteService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cliente c) {
        Either<ServiceError, Cliente> res = svc.crear(c);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity.status(err.getStatus()).body(
                    ErrorResponse.of(err.getStatus(), err.getMessage())
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(res.getRight());
    }

    @GetMapping
    public List<Cliente> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Either<ServiceError, Cliente> res = svc.obtenerPorId(id);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity.status(err.getStatus()).body(
                    ErrorResponse.of(err.getStatus(), err.getMessage())
            );
        }
        return ResponseEntity.ok(res.getRight());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Cliente c) {
        Either<ServiceError, Cliente> res = svc.actualizar(id, c);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity.status(err.getStatus()).body(
                    ErrorResponse.of(err.getStatus(), err.getMessage())
            );
        }
        return ResponseEntity.ok(res.getRight());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Either<ServiceError, Void> res = svc.eliminar(id);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity.status(err.getStatus()).body(
                    ErrorResponse.of(err.getStatus(), err.getMessage())
            );
        }
        return ResponseEntity.noContent().build();
    }
}