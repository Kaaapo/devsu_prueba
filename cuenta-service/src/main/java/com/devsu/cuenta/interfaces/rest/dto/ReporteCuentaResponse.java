package com.devsu.cuenta.interfaces.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ReporteCuentaResponse {

    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoDisponible;
    private List<MovimientoReporteResponse> movimientos;
}
