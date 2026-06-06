package com.devsu.cliente.infrastructure.persistence;

import com.devsu.cliente.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<Cliente, Long> {

    boolean existsByIdentificacion(String identificacion);
}
