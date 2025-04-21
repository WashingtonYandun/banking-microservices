package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;

import java.util.List;

public interface ICuentaService {
    Cuenta crear(Cuenta cuenta);
    List<Cuenta> listar();
    Cuenta obtenerPorId(Long id);
    void eliminar(Long id);
}