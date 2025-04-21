package com.tcs.cuenta_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cuentas", uniqueConstraints = {
        @UniqueConstraint(columnNames = "numeroCuenta")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cuenta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCuenta;

    @Column(nullable = false)
    private String tipoCuenta;

    @Column(nullable = false)
    private Double saldo;

    @Column(nullable = false)
    private Boolean estado;

    @Column(nullable = false)
    private String clienteId;
}
