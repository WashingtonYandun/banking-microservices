package com.tcs.cuenta_service.controller;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.dto.CuentaReportDTO;
import com.tcs.cuenta_service.dto.ReporteDTO;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.repository.MovimientoRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    public ReporteController(CuentaRepository cuentaRepo,
                             MovimientoRepository movRepo) {
        this.cuentaRepo = cuentaRepo;
        this.movRepo = movRepo;
    }

    @GetMapping
    public ReporteDTO estadoDeCuenta(
            @RequestParam String clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<Cuenta> cuentas = cuentaRepo.findByClienteId(clienteId);

        List<CuentaReportDTO> detalles = cuentas.stream().map(c -> {
            var movimientos = movRepo.findByCuentaAndFechaBetween(
                    c,
                    desde.atStartOfDay(),
                    hasta.atTime(23,59,59)
            );
            return new CuentaReportDTO(c.getNumeroCuenta(), c.getSaldo(), movimientos);
        }).collect(Collectors.toList());

        return new ReporteDTO(clienteId, desde, hasta, detalles);
    }
}
