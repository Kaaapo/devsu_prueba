package com.devsu.cuenta.interfaces.rest.dto;

import com.devsu.cuenta.domain.model.Movimiento;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class MovimientoResponse {

    private Long movimientoId;
    private LocalDate fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Long cuentaId;

    public static MovimientoResponse from(Movimiento movimiento) {
        return MovimientoResponse.builder()
                .movimientoId(movimiento.getMovimientoId())
                .fecha(movimiento.getFecha())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .valor(movimiento.getValor())
                .saldo(movimiento.getSaldo())
                .cuentaId(movimiento.getCuentaId())
                .build();
    }
}
