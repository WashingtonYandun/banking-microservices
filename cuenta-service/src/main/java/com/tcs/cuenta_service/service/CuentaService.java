package com.tcs.cuenta_service.service;

import com.tcs.cuenta_service.domain.Cuenta;
import com.tcs.cuenta_service.repository.CuentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaService implements ICuentaService {

    private final CuentaRepository repo;

    public CuentaService(CuentaRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Cuenta crear(Cuenta cuenta) {
        return repo.save(cuenta);
    }

    @Override
    public List<Cuenta> listar() {
        return repo.findAll();
    }

    @Override
    public Cuenta obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Cuenta no existe: " + id);
        }
        repo.deleteById(id);
    }
}