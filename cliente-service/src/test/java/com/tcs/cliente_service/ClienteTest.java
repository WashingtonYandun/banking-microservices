package com.tcs.cliente_service;

import com.tcs.cliente_service.domain.Cliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testGettersSetters() {
        Cliente c = new Cliente();
        c.setNombre("Ana");
        c.setClienteId("C100");
        assertEquals("Ana", c.getNombre());
        assertEquals("C100", c.getClienteId());
    }
}
