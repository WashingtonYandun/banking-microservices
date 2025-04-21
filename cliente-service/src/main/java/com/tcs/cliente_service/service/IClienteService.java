package com.tcs.cliente_service.service;

import com.tcs.cliente_service.domain.Cliente;
import com.tcs.cliente_service.exception.ServiceError;
import com.tcs.cliente_service.util.Either;

import java.util.List;

public interface IClienteService {
    Either<ServiceError, Cliente> crear(Cliente cliente);
    List<Cliente> listar();
    Either<ServiceError, Cliente> obtenerPorId(Long id);
    Either<ServiceError, Cliente> actualizar(Long id, Cliente cliente);
    Either<ServiceError, Void> eliminar(Long id);
}

