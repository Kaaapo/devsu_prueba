package com.devsu.cuenta.interfaces.rest.mapper;

import com.devsu.cuenta.application.dto.MovimientoReporteDto;
import com.devsu.cuenta.application.dto.ReporteCuentaDto;
import com.devsu.cuenta.application.dto.ReporteEstadoCuentaDto;
import com.devsu.cuenta.interfaces.rest.dto.MovimientoReporteResponse;
import com.devsu.cuenta.interfaces.rest.dto.ReporteCuentaResponse;
import com.devsu.cuenta.interfaces.rest.dto.ReporteEstadoCuentaResponse;

public final class ReporteMapper {

    private ReporteMapper() {
    }

    public static ReporteEstadoCuentaResponse toResponse(ReporteEstadoCuentaDto dto) {
        return ReporteEstadoCuentaResponse.builder()
                .cliente(dto.getCliente())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .cuentas(dto.getCuentas().stream()
                        .map(ReporteMapper::toCuentaResponse)
                        .toList())
                .build();
    }

    private static ReporteCuentaResponse toCuentaResponse(ReporteCuentaDto dto) {
        return ReporteCuentaResponse.builder()
                .numeroCuenta(dto.getNumeroCuenta())
                .tipo(dto.getTipo())
                .saldoDisponible(dto.getSaldoDisponible())
                .movimientos(dto.getMovimientos().stream()
                        .map(ReporteMapper::toMovimientoResponse)
                        .toList())
                .build();
    }

    private static MovimientoReporteResponse toMovimientoResponse(MovimientoReporteDto dto) {
        return MovimientoReporteResponse.builder()
                .fecha(dto.getFecha())
                .tipoMovimiento(dto.getTipoMovimiento())
                .valor(dto.getValor())
                .saldo(dto.getSaldo())
                .build();
    }
}
