package com.tcs.cuenta_service.controller;

import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.service.IMovimientoService;
import com.tcs.cuenta_service.util.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {
    private final IMovimientoService svc;

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestParam String numeroCuenta,
            @RequestBody Movimiento m
    ) {
        Either<ServiceError, Movimiento> res = svc.crear(numeroCuenta, m);
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
    public ResponseEntity<?> listarPorFecha(
            @RequestParam String numeroCuenta,
            @RequestParam Instant desde,
            @RequestParam Instant hasta
    ) {
        Either<ServiceError, List<Movimiento>> res =
                svc.movimientosPorCuentaYFecha(numeroCuenta, desde, hasta);

        if (res.isLeft()) {
            ServiceError err = res.getLeft();
            return ResponseEntity
                    .status(err.getStatus())
                    .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
        }
        return ResponseEntity.ok(res.getRight());
    }
}