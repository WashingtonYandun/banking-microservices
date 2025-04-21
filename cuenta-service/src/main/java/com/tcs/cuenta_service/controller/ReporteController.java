package com.tcs.cuenta_service.controller;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.service.ICuentaService;
import com.tcs.cuenta_service.service.IMovimientoService;
import com.tcs.cuenta_service.util.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ICuentaService cuentaSvc;
    private final IMovimientoService movSvc;

    @GetMapping
    public ResponseEntity<?> estadoCuenta(
            @RequestParam String clienteId,
            @RequestParam Instant desde,
            @RequestParam Instant hasta
    ) {
        Either<ServiceError, List<Cuenta>> cuentasE = cuentaSvc.listarPorCliente(clienteId);
        if (cuentasE.isLeft()) {
            ServiceError err = cuentasE.getLeft();
            return ResponseEntity
                    .status(err.getStatus())
                    .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
        }
        List<Cuenta> cuentas = cuentasE.getRight();

        // rango
        List<CuentaReporteDto> cuentasDto = new ArrayList<>();
        for (Cuenta c : cuentas) {
            Either<ServiceError, List<Movimiento>> movsE =
                    movSvc.movimientosPorCuentaYFecha(c.getNumeroCuenta(), desde, hasta);
            if (movsE.isLeft()) {
                ServiceError err = movsE.getLeft();
                return ResponseEntity
                        .status(err.getStatus())
                        .body(ErrorResponse.of(err.getStatus(), err.getMessage()));
            }
            cuentasDto.add(new CuentaReporteDto(
                    c.getNumeroCuenta(),
                    c.getSaldo(),
                    movsE.getRight()
            ));
        }

        ReporteDto reporte = new ReporteDto(clienteId, desde, hasta, cuentasDto);
        return ResponseEntity.ok(reporte);
    }

    public record ReporteDto(
            String clienteId,
            Instant desde,
            Instant hasta,
            List<CuentaReporteDto> cuentas
    ) {}

    public record CuentaReporteDto(
            String numeroCuenta,
            Double saldoActual,
            List<Movimiento> movimientos
    ) {}
}
