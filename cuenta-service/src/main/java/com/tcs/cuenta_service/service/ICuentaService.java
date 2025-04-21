package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.util.Either;

import java.util.List;

public interface ICuentaService {
    Either<ServiceError, Cuenta> crear(Cuenta c);
    Either<ServiceError, List<Cuenta>> listarPorCliente(String clienteId);
    Either<ServiceError, Cuenta> actualizar(Long id, Cuenta c);
    Either<ServiceError, Void> eliminar(Long id);
}