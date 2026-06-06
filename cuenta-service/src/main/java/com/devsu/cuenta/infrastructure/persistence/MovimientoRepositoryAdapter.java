package com.devsu.cuenta.infrastructure.persistence;

import com.devsu.cuenta.domain.model.Movimiento;
import com.devsu.cuenta.domain.port.MovimientoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {

    private final MovimientoJpaRepository repository;

    @Override
    public Movimiento save(Movimiento movimiento) {
        return repository.save(movimiento);
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Movimiento> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDate inicio, LocalDate fin) {
        return repository.findByCuentaIdAndFechaBetweenOrderByFechaAsc(cuentaId, inicio, fin);
    }
}
