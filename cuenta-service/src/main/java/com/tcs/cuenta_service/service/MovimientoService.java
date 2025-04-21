package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.repository.MovimientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MovimientoService implements IMovimientoService {

    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    public MovimientoService(CuentaRepository cuentaRepo,
                             MovimientoRepository movRepo) {
        this.cuentaRepo = cuentaRepo;
        this.movRepo = movRepo;
    }

    @Override
    @Transactional
    public Movimiento registrar(String numeroCuenta, Movimiento m) {
        Cuenta c = cuentaRepo.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cuenta no existe: " + numeroCuenta));

        BigDecimal nuevoSaldo = m.getTipoMovimiento().equalsIgnoreCase("RETIRO")
                ? c.getSaldo().subtract(m.getValor())
                : c.getSaldo().add(m.getValor());

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Saldo no disponible");
        }

        c.setSaldo(nuevoSaldo);
        cuentaRepo.save(c);

        m.setCuenta(c);
        m.setSaldoDisponible(nuevoSaldo);
        m.setFecha(LocalDateTime.now());
        return movRepo.save(m);
    }
}