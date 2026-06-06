package com.devsu.cuenta.domain.port;

import com.devsu.cuenta.domain.model.Movimiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepositoryPort {

    Movimiento save(Movimiento movimiento);

    Optional<Movimiento> findById(Long id);

    List<Movimiento> findAll();

    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDate inicio, LocalDate fin);
}
