package com.devsu.cuenta.infrastructure.persistence;

import com.devsu.cuenta.domain.model.ClienteReplica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteReplicaJpaRepository extends JpaRepository<ClienteReplica, Long> {

    Optional<ClienteReplica> findByNombreIgnoreCase(String nombre);
}
