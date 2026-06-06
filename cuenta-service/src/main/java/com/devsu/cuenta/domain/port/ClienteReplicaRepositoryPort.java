package com.devsu.cuenta.domain.port;

import com.devsu.cuenta.domain.model.ClienteReplica;

import java.util.Optional;

public interface ClienteReplicaRepositoryPort {

    ClienteReplica save(ClienteReplica replica);

    Optional<ClienteReplica> findById(Long id);

    Optional<ClienteReplica> findByNombreIgnoreCase(String nombre);

    void deleteById(Long id);
}
