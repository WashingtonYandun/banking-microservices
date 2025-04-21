package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.repository.MovimientoRepository;
import com.tcs.cuenta_service.util.Either;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService implements IMovimientoService {

    private final MovimientoRepository movRepo;
    private final CuentaRepository cuentaRepo;

    public MovimientoService(MovimientoRepository movRepo,
                             CuentaRepository cuentaRepo) {
        this.movRepo = movRepo;
        this.cuentaRepo = cuentaRepo;
    }

    @Override
    @Transactional
    public Either<ServiceError, Movimiento> crear(String numeroCuenta, Movimiento m) {
        Optional<Cuenta> optCuenta = Optional.ofNullable(cuentaRepo.findByNumeroCuenta(numeroCuenta));
        if (optCuenta.isEmpty()) {
            return Either.left(new ServiceError(
                    HttpStatus.NOT_FOUND,
                    "Cuenta no encontrada: " + numeroCuenta
            ));
        }
        Cuenta cuenta = optCuenta.get();

        if (m.getTipoMovimiento() == null || m.getTipoMovimiento().isBlank()) {
            return Either.left(new ServiceError(
                    HttpStatus.BAD_REQUEST,
                    "tipoMovimiento es obligatorio"
            ));
        }

        if (m.getValor() == null) {
            return Either.left(new ServiceError(
                    HttpStatus.BAD_REQUEST,
                    "El valor del movimiento es obligatorio"
            ));
        }
        double valor = m.getValor();

        double nuevoSaldo = cuenta.getSaldo() + valor;
        if (nuevoSaldo < 0) {
            return Either.left(new ServiceError(
                    HttpStatus.BAD_REQUEST,
                    "Saldo no disponible"
            ));
        }

        cuenta.setSaldo(nuevoSaldo);
        cuentaRepo.save(cuenta);

        m.setCuenta(cuenta);
        m.setFecha(Instant.now());
        m.setSaldoDisponible(nuevoSaldo);
        Movimiento saved = movRepo.save(m);

        return Either.right(saved);
    }

    @Override
    public Either<ServiceError, List<Movimiento>> movimientosPorCuentaYFecha(
            String numeroCuenta, Instant desde, Instant hasta
    ) {

        if (desde == null || hasta == null) {
            return Either.left(new ServiceError(
                    HttpStatus.BAD_REQUEST,
                    "Los parámetros 'desde' y 'hasta' son obligatorios"
            ));
        }
        if (desde.isAfter(hasta)) {
            return Either.left(new ServiceError(
                    HttpStatus.BAD_REQUEST,
                    "'desde' debe ser anterior o igual a 'hasta'"
            ));
        }

        // rango
        List<Movimiento> movimientos = movRepo
                .findByCuenta_NumeroCuentaAndFechaBetween(numeroCuenta, desde, hasta);

        return Either.right(movimientos);
    }
}
