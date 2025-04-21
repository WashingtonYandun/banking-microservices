package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.util.Either;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService implements ICuentaService {

    private final CuentaRepository repo;

    public CuentaService(CuentaRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Either<ServiceError, Cuenta> crear(Cuenta c) {
        if (c.getNumeroCuenta() == null || c.getNumeroCuenta().isBlank()) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "numero de cuenta es obligatorio"));
        }
        if (c.getTipoCuenta() == null || c.getTipoCuenta().isBlank()) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "tipo de cuenta es obligatorio"));
        }
        if (c.getSaldo() == null) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "saldo inicial es obligatorio"));
        }
        if (Double.compare(c.getSaldo(), 0.0) < 0) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "saldo inicial no puede ser negativo"));
        }
        if (c.getEstado() == null) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "estado es obligatorio"));
        }
        if (c.getClienteId() == null || c.getClienteId().isBlank()) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "clienteId es obligatorio"));
        }

        if (repo.existsByNumeroCuenta(c.getNumeroCuenta())) {
            return Either.left(new ServiceError(HttpStatus.CONFLICT,
                    "numeroCuenta ya existe"));
        }

        try {
            Cuenta saved = repo.save(c);
            return Either.right(saved);
        } catch (DataIntegrityViolationException ex) {
            return Either.left(new ServiceError(HttpStatus.CONFLICT,
                    "Violación de integridad: " + ex.getMessage()));
        }
    }

    @Override
    public Either<ServiceError, List<Cuenta>> listarPorCliente(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "clienteId es obligatorio"));
        }
        List<Cuenta> cuentas = repo.findByClienteId(clienteId);
        return Either.right(cuentas);
    }

    @Override
    @Transactional
    public Either<ServiceError, Cuenta> actualizar(Long id, Cuenta datos) {
        Optional<Cuenta> opt = repo.findById(id);
        if (opt.isEmpty()) {
            return Either.left(new ServiceError(HttpStatus.NOT_FOUND,
                    "Cuenta no encontrada: " + id));
        }
        Cuenta existing = opt.get();

        if (datos.getTipoCuenta() == null || datos.getTipoCuenta().isBlank()) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "tipoCuenta es obligatorio"));
        }
        existing.setTipoCuenta(datos.getTipoCuenta());

        if (datos.getSaldo() == null) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "saldo es obligatorio"));
        }
        if (Double.compare(datos.getSaldo(), 0.0) < 0) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "saldo no puede ser negativo"));
        }
        existing.setSaldo(datos.getSaldo());

        if (datos.getEstado() == null) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "estado es obligatorio"));
        }
        existing.setEstado(datos.getEstado());

        if (datos.getClienteId() == null || datos.getClienteId().isBlank()) {
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST,
                    "clienteId es obligatorio"));
        }
        existing.setClienteId(datos.getClienteId());

        try {
            Cuenta saved = repo.save(existing);
            return Either.right(saved);
        } catch (DataIntegrityViolationException ex) {
            return Either.left(new ServiceError(HttpStatus.CONFLICT,
                    "Violación de integridad: " + ex.getMessage()));
        }
    }

    @Override
    @Transactional
    public Either<ServiceError, Void> eliminar(Long id) {
        if (!repo.existsById(id)) {
            return Either.left(new ServiceError(HttpStatus.NOT_FOUND,
                    "Cuenta no encontrada: " + id));
        }
        repo.deleteById(id);
        return Either.right(null);
    }
}
