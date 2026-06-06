package com.devsu.cuenta.infrastructure.persistence;

import com.devsu.cuenta.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoJpaRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);

    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(Long cuentaId, LocalDate inicio, LocalDate fin);
}
