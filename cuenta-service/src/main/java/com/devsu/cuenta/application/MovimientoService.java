package com.devsu.cuenta.application;

import com.devsu.cuenta.domain.exception.SaldoNoDisponibleException;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.model.Movimiento;
import com.devsu.cuenta.domain.port.CuentaRepositoryPort;
import com.devsu.cuenta.domain.port.MovimientoRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepositoryPort movimientoRepository;
    private final CuentaRepositoryPort cuentaRepository;

    @Transactional
    public Movimiento registrar(String numeroCuenta, String tipoMovimiento, BigDecimal valor, LocalDate fecha) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta con número " + numeroCuenta + " no encontrada"));

        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("La cuenta " + numeroCuenta + " no está activa");
        }

        BigDecimal valorMovimiento = normalizarValor(tipoMovimiento, valor);
        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(valorMovimiento);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException();
        }

        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(fecha != null ? fecha : LocalDate.now());
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuentaId(cuenta.getCuentaId());

        return movimientoRepository.save(movimiento);
    }

    @Transactional(readOnly = true)
    public Movimiento obtenerPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento con ID " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Movimiento> listar() {
        return movimientoRepository.findAll();
    }

    @Transactional
    public Movimiento actualizarCompleto(Long id, Movimiento datos) {
        Movimiento existente = obtenerPorId(id);
        existente.setFecha(datos.getFecha());
        existente.setTipoMovimiento(datos.getTipoMovimiento());
        existente.setValor(datos.getValor());
        existente.setSaldo(datos.getSaldo());
        existente.setCuentaId(datos.getCuentaId());
        return movimientoRepository.save(existente);
    }

    @Transactional
    public Movimiento actualizarParcial(Long id, Movimiento datos) {
        Movimiento existente = obtenerPorId(id);
        if (datos.getFecha() != null) existente.setFecha(datos.getFecha());
        if (datos.getTipoMovimiento() != null) existente.setTipoMovimiento(datos.getTipoMovimiento());
        if (datos.getValor() != null) existente.setValor(datos.getValor());
        if (datos.getSaldo() != null) existente.setSaldo(datos.getSaldo());
        if (datos.getCuentaId() != null) existente.setCuentaId(datos.getCuentaId());
        return movimientoRepository.save(existente);
    }

    private BigDecimal normalizarValor(String tipoMovimiento, BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El valor del movimiento es obligatorio");
        }
        String tipo = tipoMovimiento != null ? tipoMovimiento.trim() : "";
        if ("Retiro".equalsIgnoreCase(tipo) && valor.compareTo(BigDecimal.ZERO) > 0) {
            return valor.negate();
        }
        if ("Deposito".equalsIgnoreCase(tipo) && valor.compareTo(BigDecimal.ZERO) < 0) {
            return valor.abs();
        }
        return valor;
    }
}
