package com.tcs.cuenta_service.dto;

import com.tcs.cuenta_service.domain.Movimiento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class CuentaReportDTO {
    private String numeroCuenta;
    private BigDecimal saldoActual;
    private List<Movimiento> movimientos;
}
