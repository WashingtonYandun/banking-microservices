package com.tcs.cliente_service.event;

import com.tcs.cliente_service.domain.Cliente;

import java.util.HashMap;
import java.util.Map;

public class ClienteEventFactory {
    public static Map<String,Object> buildClienteCreatedEvent(Cliente c) {
        Map<String,Object> evt = new HashMap<>();
        evt.put("tipo", "CLIENTE_CREADO");
        evt.put("clienteId", c.getClienteId());
        evt.put("nombre", c.getNombre());
        evt.put("identificacion", c.getIdentificacion());
        evt.put("timestamp", System.currentTimeMillis());
        return evt;
    }
}

