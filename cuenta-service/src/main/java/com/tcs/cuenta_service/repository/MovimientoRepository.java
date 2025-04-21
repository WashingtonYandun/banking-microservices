package com.tcs.cuenta_service.repository;


import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaAndFechaBetween(Cuenta cuenta,
                                                 LocalDateTime desde, LocalDateTime hasta);
}