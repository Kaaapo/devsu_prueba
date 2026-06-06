package com.devsu.cuenta.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String tipo;

    @NotNull(message = "El saldo inicial es obligatorio")
    private BigDecimal saldoInicial;

    private BigDecimal saldoDisponible;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;
}
