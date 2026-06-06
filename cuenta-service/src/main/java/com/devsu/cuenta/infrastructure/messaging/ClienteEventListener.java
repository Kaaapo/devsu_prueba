package com.devsu.cuenta.infrastructure.messaging;

import com.devsu.cuenta.domain.model.ClienteReplica;
import com.devsu.cuenta.domain.port.ClienteReplicaRepositoryPort;
import com.devsu.events.ClienteEvent;
import com.devsu.events.ClienteEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ClienteEventListener {

    private final ClienteReplicaRepositoryPort clienteReplicaRepository;

    @RabbitListener(queues = "${rabbitmq.queue.cliente}")
    @Transactional
    public void handleClienteEvent(ClienteEvent event) {
        if (event.getEventType() == ClienteEventType.ELIMINADO) {
            clienteReplicaRepository.deleteById(event.getClienteId());
            return;
        }

        ClienteReplica replica = clienteReplicaRepository.findById(event.getClienteId())
                .orElse(new ClienteReplica());

        replica.setClienteId(event.getClienteId());
        replica.setNombre(event.getNombre());
        replica.setEstado(event.getEstado());
        clienteReplicaRepository.save(replica);
    }
}
