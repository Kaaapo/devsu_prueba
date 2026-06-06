package com.devsu.cuenta.infrastructure.persistence;

import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.port.CuentaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CuentaRepositoryAdapter implements CuentaRepositoryPort {

    private final CuentaJpaRepository repository;

    @Override
    public Cuenta save(Cuenta cuenta) {
        return repository.save(cuenta);
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return repository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public List<Cuenta> findByClienteId(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Override
    public List<Cuenta> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return repository.existsByNumeroCuenta(numeroCuenta);
    }
}
