package com.devsu.cliente.infrastructure.messaging;

import com.devsu.cliente.domain.model.Cliente;
import com.devsu.cliente.domain.port.ClienteEventPublisherPort;
import com.devsu.events.ClienteEvent;
import com.devsu.events.ClienteEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClienteEventPublisher implements ClienteEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.cliente}")
    private String clienteExchange;

    @Value("${rabbitmq.routing-key.cliente}")
    private String routingKey;

    @Override
    public void publish(Cliente cliente, ClienteEventType eventType) {
        ClienteEvent event = new ClienteEvent(
                eventType,
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getEstado()
        );
        rabbitTemplate.convertAndSend(clienteExchange, routingKey, event);
    }
}
