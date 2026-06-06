package com.devsu.cuenta.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ReporteCuentaDto {

    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoDisponible;
    private List<MovimientoReporteDto> movimientos;
}
