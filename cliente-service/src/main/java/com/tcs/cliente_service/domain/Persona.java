package com.tcs.cliente_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public abstract class Persona {
    @Column(nullable = false)
    private String nombre;
    private String genero;
    private Integer edad;
    @Column(unique = true, nullable = false)
    private String identificacion;
    private String direccion;
    private String telefono;
}