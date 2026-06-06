package com.devsu.cuenta.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReporteEstadoCuentaDto {

    private String cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ReporteCuentaDto> cuentas;
}
