package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.util.Either;

import java.time.Instant;
import java.util.List;

public interface IMovimientoService {
    Either<ServiceError, Movimiento> crear(String numeroCuenta, Movimiento m);
    Either<ServiceError, List<Movimiento>> movimientosPorCuentaYFecha(
            String numeroCuenta, Instant desde, Instant hasta);
}