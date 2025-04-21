package com.tcs.cuenta_service.service;


import com.tcs.cuenta_service.domain.Movimiento;

public interface IMovimientoService {
    Movimiento registrar(String numeroCuenta, Movimiento m);
}