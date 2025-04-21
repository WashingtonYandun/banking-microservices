package com.tcs.cuenta_service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClienteCreatedListener {
    private static final Logger log = LoggerFactory.getLogger(ClienteCreatedListener.class);

    @RabbitListener(queues = "clientes.queue")
    public void onClienteCreated(Map<String,Object> evt) {
        log.info("Evento recibido: CLIENTE_CREADO → {}", evt);
        // Aquí podrías enlazar lógica adicional (p.ej. validar cliente antes de crear cuenta)
    }
}