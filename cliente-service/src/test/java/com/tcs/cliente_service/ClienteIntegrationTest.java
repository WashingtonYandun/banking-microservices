package com.tcs.cliente_service;

import com.tcs.cliente_service.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClienteIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @MockBean
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @Test
    void crearYObtenerCliente() {
        Cliente c = new Cliente();
        c.setClienteId("C200");
        c.setNombre("TestUser");
        c.setContraseña("pass");
        c.setEstado(true);
        c.setIdentificacion("ID200");

        ResponseEntity<Cliente> postResp =
                rest.postForEntity("/api/clientes", c, Cliente.class);

        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Cliente saved = postResp.getBody();
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();

        Cliente fetched =
                rest.getForObject("/api/clientes/" + saved.getId(), Cliente.class);
        assertThat(fetched.getNombre()).isEqualTo("TestUser");
    }
}
