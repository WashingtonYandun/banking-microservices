package com.tcs.cuenta_service.controller;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.service.ICuentaService;
import com.tcs.cuenta_service.util.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final ICuentaService svc;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cuenta c) {
        Either<ServiceError, Cuenta> res = svc.crear(c);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity
                    .status(err.getStatus())
                    .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res.getRight());
    }

    @GetMapping
    public ResponseEntity<?> listarPorCliente(@RequestParam String clienteId) {
        Either<ServiceError, List<Cuenta>> res = svc.listarPorCliente(clienteId);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity
                    .status(err.getStatus())
                    .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
        }
        return ResponseEntity.ok(res.getRight());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Cuenta c
    ) {
        Either<ServiceError, Cuenta> res = svc.actualizar(id, c);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity
                    .status(err.getStatus())
                    .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
        }
        return ResponseEntity.ok(res.getRight());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Either<ServiceError, Void> res = svc.eliminar(id);
        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity
                    .status(err.getStatus())
                    .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
        }
        return ResponseEntity.noContent().build();
    }
}