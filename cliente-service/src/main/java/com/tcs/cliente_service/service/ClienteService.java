package com.tcs.cliente_service.service;


import com.tcs.cliente_service.domain.Cliente;
import com.tcs.cliente_service.event.ClienteEventFactory;
import com.tcs.cliente_service.exception.ServiceError;
import com.tcs.cliente_service.repository.ClienteRepository;
import com.tcs.cliente_service.util.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ClienteService implements IClienteService {
    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository repo;
    private final RabbitTemplate rabbit;
    @Value("${clientes.exchange}") private String exchange;

    public ClienteService(ClienteRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    @Transactional
    public Either<ServiceError, Cliente> crear(Cliente c) {
        if (c.getClienteId() == null || c.getClienteId().isBlank())
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST, "clienteId es obligatorio"));
        if (c.getNombre() == null || c.getNombre().isBlank())
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST, "nombre es obligatorio"));
        if (c.getContraseña() == null || c.getContraseña().isBlank())
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST, "contraseña es obligatoria"));
        if (c.getIdentificacion() == null || c.getIdentificacion().isBlank())
            return Either.left(new ServiceError(HttpStatus.BAD_REQUEST, "identificacion es obligatoria"));

        Cliente saved;

        try {
            saved = repo.save(c);
        } catch (DataIntegrityViolationException ex) {
            return Either.left(new ServiceError(HttpStatus.CONFLICT, "clienteId o identificacion ya existe"));
        }

        try {
            var evt = ClienteEventFactory.buildClienteCreatedEvent(saved);
            rabbit.convertAndSend(exchange, "clientes.creado", evt);
        } catch (AmqpException ex) {
            log.warn("Fallo pub evento: {}", ex.getMessage());
        }

        return Either.right(saved);
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public Either<ServiceError, Cliente> obtenerPorId(Long id) {
        return repo.findById(id)
                .map(Either::<ServiceError, Cliente>right)
                .orElseGet(() -> Either.left(
                        new ServiceError(HttpStatus.NOT_FOUND, "Cliente no encontrado: " + id)
                ));
    }

    @Transactional
    public Either<ServiceError, Cliente> actualizar(Long id, Cliente datos) {
        Either<ServiceError, Cliente> found = obtenerPorId(id);
        if (found.isLeft()) return Either.left(found.getLeft());
        Cliente ex = found.getRight();
        ex.setNombre(datos.getNombre());
        ex.setGenero(datos.getGenero());
        ex.setEdad(datos.getEdad());
        ex.setIdentificacion(datos.getIdentificacion());
        ex.setDireccion(datos.getDireccion());
        ex.setTelefono(datos.getTelefono());
        ex.setContraseña(datos.getContraseña());
        ex.setEstado(datos.getEstado());
        try {
            Cliente updated = repo.save(ex);
            return Either.right(updated);
        } catch (DataIntegrityViolationException exx) {
            return Either.left(new ServiceError(HttpStatus.CONFLICT, "clienteId o identificacion duplicados"));
        }
    }

    @Transactional
    public Either<ServiceError, Void> eliminar(Long id) {
        if (!repo.existsById(id)) {
            return Either.left(new ServiceError(HttpStatus.NOT_FOUND, "Cliente no existe: " + id));
        }
        repo.deleteById(id);
        return Either.right(null);
    }
}