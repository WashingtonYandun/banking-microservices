package com.tcs.cuenta_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReporteDTO {
    private String clienteId;
    private LocalDate desde;
    private LocalDate hasta;
    private List<CuentaReportDTO> cuentas;
}