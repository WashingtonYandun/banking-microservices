package com.tcs.cuenta_service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.repository.MovimientoRepository;
import com.tcs.cuenta_service.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class MovimientoServiceTest {

    private MovimientoService svc;
    private CuentaRepository cuentaRepo = Mockito.mock(CuentaRepository.class);
    private MovimientoRepository movRepo = Mockito.mock(MovimientoRepository.class);

    @BeforeEach
    void setUp() {
        svc = new MovimientoService(cuentaRepo, movRepo);
    }

    @Test
    void depositoActualizaSaldo() {
        Cuenta c = new Cuenta(); c.setSaldo(new BigDecimal("100"));
        Mockito.when(cuentaRepo.findByNumeroCuenta("123")).thenReturn(Optional.of(c));
        Mockito.when(movRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Movimiento m = new Movimiento();
        m.setTipoMovimiento("DEPOSITO");
        m.setValor(new BigDecimal("50"));

        Movimiento result = svc.registrar("123", m);
        assertThat(result.getSaldoDisponible()).isEqualByComparingTo("150");
    }

    @Test
    void retiroSinSaldoLanzaError() {
        Cuenta c = new Cuenta(); c.setSaldo(new BigDecimal("30"));
        Mockito.when(cuentaRepo.findByNumeroCuenta("123")).thenReturn(Optional.of(c));

        Movimiento m = new Movimiento();
        m.setTipoMovimiento("RETIRO");
        m.setValor(new BigDecimal("50"));

        assertThatThrownBy(() -> svc.registrar("123", m))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Saldo no disponible");
    }
}