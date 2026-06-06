package com.devsu.cuenta.application;

import com.devsu.cuenta.application.dto.MovimientoReporteDto;
import com.devsu.cuenta.application.dto.ReporteCuentaDto;
import com.devsu.cuenta.application.dto.ReporteEstadoCuentaDto;
import com.devsu.cuenta.domain.model.ClienteReplica;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.model.Movimiento;
import com.devsu.cuenta.domain.port.ClienteReplicaRepositoryPort;
import com.devsu.cuenta.domain.port.CuentaRepositoryPort;
import com.devsu.cuenta.domain.port.MovimientoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ClienteReplicaRepositoryPort clienteReplicaRepository;
    private final CuentaRepositoryPort cuentaRepository;
    private final MovimientoRepositoryPort movimientoRepository;

    @Transactional(readOnly = true)
    public ReporteEstadoCuentaDto generarEstadoCuenta(String nombreCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        ClienteReplica cliente = clienteReplicaRepository.findByNombreIgnoreCase(nombreCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente " + nombreCliente + " no encontrado"));

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(cliente.getClienteId());

        List<ReporteCuentaDto> cuentasReporte = cuentas.stream()
                .map(cuenta -> {
                    List<Movimiento> movimientos = movimientoRepository
                            .findByCuentaIdAndFechaBetween(cuenta.getCuentaId(), fechaInicio, fechaFin);

                    List<MovimientoReporteDto> movimientosDto = movimientos.stream()
                            .map(m -> MovimientoReporteDto.builder()
                                    .fecha(m.getFecha())
                                    .tipoMovimiento(m.getTipoMovimiento())
                                    .valor(m.getValor())
                                    .saldo(m.getSaldo())
                                    .build())
                            .toList();

                    return ReporteCuentaDto.builder()
                            .numeroCuenta(cuenta.getNumeroCuenta())
                            .tipo(cuenta.getTipo())
                            .saldoDisponible(cuenta.getSaldoDisponible())
                            .movimientos(movimientosDto)
                            .build();
                })
                .toList();

        return ReporteEstadoCuentaDto.builder()
                .cliente(cliente.getNombre())
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .cuentas(cuentasReporte)
                .build();
    }
}
