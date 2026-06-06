package com.devsu.cuenta.interfaces.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReporteEstadoCuentaResponse {

    private String cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ReporteCuentaResponse> cuentas;
}
