package com.tcs.cuenta_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.repository.CuentaRepository;
import com.tcs.cuenta_service.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReporteControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CuentaRepository cuentaRepo;
    @Autowired private MovimientoRepository movRepo;

    @BeforeEach
    void setUp() {
        movRepo.deleteAll();
        cuentaRepo.deleteAll();
    }

    @Test
    void estadoCuenta_happyPath() throws Exception {
        String clienteId = "C1";
        Cuenta c1 = cuentaRepo.save(Cuenta.builder()
                .numeroCuenta("ACC1")
                .tipoCuenta("AHORRO")
                .saldo(100.0)
                .estado(true)
                .clienteId(clienteId)
                .build());

        Instant now = Instant.now();
        // movimiento dentro del rango
        movRepo.save(Movimiento.builder()
                .cuenta(c1)
                .tipoMovimiento("DEPOSITO")
                .valor(50.0)
                .fecha(now)
                .saldoDisponible(150.0)
                .build());
        // movimiento fuera del rango
        movRepo.save(Movimiento.builder()
                .cuenta(c1)
                .tipoMovimiento("RETIRO")
                .valor(-20.0)
                .fecha(now.minusSeconds(10_000))
                .saldoDisponible(80.0)
                .build());

        mockMvc.perform(get("/api/reportes")
                        .param("clienteId", clienteId)
                        .param("desde", now.minusSeconds(1_000).toString())
                        .param("hasta", now.plusSeconds(1_000).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId))
                .andExpect(jsonPath("$.cuentas[0].numeroCuenta").value("ACC1"))
                .andExpect(jsonPath("$.cuentas[0].movimientos.length()").value(1))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].tipoMovimiento")
                        .value("DEPOSITO"));
    }

    @Test
    void estadoCuenta_fechaInvalida() throws Exception {
        Instant desde = Instant.now();
        Instant hasta = desde.minusSeconds(3600);

        mockMvc.perform(get("/api/reportes")
                        .param("clienteId", "C1")
                        .param("desde", desde.toString())
                        .param("hasta", hasta.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("'desde' debe ser anterior o igual a 'hasta'"));
    }
}