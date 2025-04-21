package com.tcs.cuenta_service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.domain.Movimiento;
import com.tcs.cuenta_service.dto.ReporteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReporteControllerIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void generarReporte() {
        // 1) Crear cuenta
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("ABC123");
        cuenta.setTipoCuenta("AHORRO");
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setEstado(true);
        cuenta.setClienteId("C100");
        ResponseEntity<Cuenta> cResp = rest.postForEntity("/api/cuentas", cuenta, Cuenta.class);
        assertThat(cResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 2) Registrar movimiento
        Movimiento m = new Movimiento();
        m.setTipoMovimiento("DEPOSITO");
        m.setValor(new BigDecimal("200"));
        HttpEntity<Movimiento> request = new HttpEntity<>(m);
        ResponseEntity<Movimiento> mResp = rest.exchange(
                "/api/movimientos?numeroCuenta=ABC123",
                HttpMethod.POST, request, Movimiento.class);
        assertThat(mResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 3) Llamar reporte
        String url = "/api/reportes?clienteId=C100" +
                "&desde=" + LocalDate.now().minusDays(1) +
                "&hasta=" + LocalDate.now().plusDays(1);
        ResponseEntity<ReporteDTO> rResp = rest.getForEntity(url, ReporteDTO.class);
        assertThat(rResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        ReporteDTO reporte = rResp.getBody();
        assertThat(reporte.getCuentas()).hasSize(1);
        assertThat(reporte.getCuentas().get(0).getMovimientos()).hasSize(1);
    }
}