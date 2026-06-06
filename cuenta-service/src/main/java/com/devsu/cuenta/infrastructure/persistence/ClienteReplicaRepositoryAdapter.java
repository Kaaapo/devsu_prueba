package com.devsu.cuenta.infrastructure.persistence;

import com.devsu.cuenta.domain.model.ClienteReplica;
import com.devsu.cuenta.domain.port.ClienteReplicaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClienteReplicaRepositoryAdapter implements ClienteReplicaRepositoryPort {

    private final ClienteReplicaJpaRepository repository;

    @Override
    public ClienteReplica save(ClienteReplica replica) {
        return repository.save(replica);
    }

    @Override
    public Optional<ClienteReplica> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ClienteReplica> findByNombreIgnoreCase(String nombre) {
        return repository.findByNombreIgnoreCase(nombre);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
