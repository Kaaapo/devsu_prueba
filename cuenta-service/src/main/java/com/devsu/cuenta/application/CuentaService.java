package com.devsu.cuenta.application;

import com.devsu.cuenta.domain.model.ClienteReplica;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.port.ClienteReplicaRepositoryPort;
import com.devsu.cuenta.domain.port.CuentaRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepositoryPort cuentaRepository;
    private final ClienteReplicaRepositoryPort clienteReplicaRepository;

    @Transactional
    public Cuenta crear(Cuenta cuenta) {
        validarClienteExiste(cuenta.getClienteId());
        if (cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
            throw new IllegalArgumentException("Ya existe una cuenta con el número: " + cuenta.getNumeroCuenta());
        }
        if (cuenta.getSaldoInicial() == null) {
            cuenta.setSaldoInicial(BigDecimal.ZERO);
        }
        cuenta.setSaldoDisponible(cuenta.getSaldoInicial());
        if (cuenta.getEstado() == null) {
            cuenta.setEstado(true);
        }
        return cuentaRepository.save(cuenta);
    }

    @Transactional(readOnly = true)
    public Cuenta obtenerPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta con ID " + id + " no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Cuenta> listar() {
        return cuentaRepository.findAll();
    }

    @Transactional
    public Cuenta actualizarCompleto(Long id, Cuenta datos) {
        Cuenta existente = obtenerPorId(id);
        validarClienteExiste(datos.getClienteId());
        existente.setNumeroCuenta(datos.getNumeroCuenta());
        existente.setTipo(datos.getTipo());
        existente.setSaldoInicial(datos.getSaldoInicial());
        existente.setSaldoDisponible(datos.getSaldoDisponible());
        existente.setEstado(datos.getEstado());
        existente.setClienteId(datos.getClienteId());
        return cuentaRepository.save(existente);
    }

    @Transactional
    public Cuenta actualizarParcial(Long id, Cuenta datos) {
        Cuenta existente = obtenerPorId(id);
        if (datos.getNumeroCuenta() != null) existente.setNumeroCuenta(datos.getNumeroCuenta());
        if (datos.getTipo() != null) existente.setTipo(datos.getTipo());
        if (datos.getSaldoInicial() != null) existente.setSaldoInicial(datos.getSaldoInicial());
        if (datos.getSaldoDisponible() != null) existente.setSaldoDisponible(datos.getSaldoDisponible());
        if (datos.getEstado() != null) existente.setEstado(datos.getEstado());
        if (datos.getClienteId() != null) {
            validarClienteExiste(datos.getClienteId());
            existente.setClienteId(datos.getClienteId());
        }
        return cuentaRepository.save(existente);
    }

    private void validarClienteExiste(Long clienteId) {
        ClienteReplica cliente = clienteReplicaRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con ID " + clienteId + " no encontrado"));
        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new IllegalArgumentException("El cliente " + cliente.getNombre() + " no está activo");
        }
    }
}
