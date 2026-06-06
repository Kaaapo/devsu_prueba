package com.devsu.cuenta.interfaces.rest.mapper;

import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.interfaces.rest.dto.CuentaRequest;

public final class CuentaMapper {

    private CuentaMapper() {
    }

    public static Cuenta toEntity(CuentaRequest request) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.getNumeroCuenta());
        cuenta.setTipo(request.getTipo());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setSaldoDisponible(request.getSaldoDisponible());
        cuenta.setEstado(request.getEstado());
        cuenta.setClienteId(request.getClienteId());
        return cuenta;
    }
}
