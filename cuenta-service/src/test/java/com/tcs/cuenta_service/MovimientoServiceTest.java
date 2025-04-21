package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.exception.ServiceError;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.repository.MovimientoRepository;
import com.tcs.cuenta_service.util.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movRepo;

    @Mock
    private CuentaRepository cuentaRepo;

    @InjectMocks
    private MovimientoService svc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Stub para que save devuelva el mismo objeto
        when(cuentaRepo.save(any(Cuenta.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(movRepo.save(any(Movimiento.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void crearMovimiento_exito() {
        // dado
        String numero = "ACC123";
        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta(numero)
                .saldo(100.0)
                .build();
        when(cuentaRepo.findByNumeroCuenta(numero)).thenReturn(cuenta);

        Movimiento m = Movimiento.builder()
                .tipoMovimiento("DEPOSITO")
                .valor(50.0)
                .build();

        // cuando
        Either<ServiceError, Movimiento> res = svc.crear(numero, m);

        // entonces
        assertThat(res.isRight()).isTrue();
        Movimiento saved = res.getRight();
        assertThat(saved.getSaldoDisponible()).isEqualTo(150.0);
        assertThat(saved.getCuenta()).isSameAs(cuenta);
        verify(cuentaRepo).save(cuenta);
        verify(movRepo).save(m);
    }

    @Test
    void crearMovimiento_cuentaNoEncontrada() {
        when(cuentaRepo.findByNumeroCuenta("X")).thenReturn(null);
        Movimiento m = new Movimiento();

        Either<ServiceError, Movimiento> res = svc.crear("X", m);

        assertThat(res.isLeft()).isTrue();
        ServiceError err = res.getLeft();
        assertThat(err.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(err.getMessage()).isEqualTo("Cuenta no encontrada: X");
        verifyNoInteractions(movRepo);
    }

    @Test
    void crearMovimiento_saldoInsuficiente() {
        String numero = "ACC";
        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta(numero)
                .saldo(20.0)
                .build();
        when(cuentaRepo.findByNumeroCuenta(numero)).thenReturn(cuenta);

        Movimiento m = Movimiento.builder()
                .tipoMovimiento("RETIRO")
                .valor(-50.0)
                .build();

        Either<ServiceError, Movimiento> res = svc.crear(numero, m);

        assertThat(res.isLeft()).isTrue();
        ServiceError err = res.getLeft();
        assertThat(err.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(err.getMessage()).isEqualTo("Saldo no disponible");
        verify(cuentaRepo, never()).save(any());
        verify(movRepo, never()).save(any());
    }

    @Test
    void listarPorFecha_rangoInvalido() {
        Either<ServiceError, List<Movimiento>> res =
                svc.movimientosPorCuentaYFecha("ACC",
                        Instant.now().plusSeconds(10),
                        Instant.now());

        assertThat(res.isLeft()).isTrue();
        ServiceError err = res.getLeft();
        assertThat(err.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(err.getMessage()).contains("debe ser anterior");
    }

    @Test
    void listarPorFecha_exito() {
        String numero = "ACC";
        Instant desde = Instant.parse("2025-01-01T00:00:00Z");
        Instant hasta = Instant.parse("2025-01-31T23:59:59Z");

        Movimiento a = new Movimiento();
        Movimiento b = new Movimiento();
        when(movRepo.findByCuenta_NumeroCuentaAndFechaBetween(numero, desde, hasta))
                .thenReturn(List.of(a, b));

        Either<ServiceError, List<Movimiento>> res =
                svc.movimientosPorCuentaYFecha(numero, desde, hasta);

        assertThat(res.isRight()).isTrue();
        List<Movimiento> list = res.getRight();
        assertThat(list).containsExactly(a, b);
    }
}