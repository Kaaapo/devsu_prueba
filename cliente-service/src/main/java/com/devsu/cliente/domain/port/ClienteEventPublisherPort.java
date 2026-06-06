package com.devsu.cliente.domain.port;

import com.devsu.cliente.domain.model.Cliente;
import com.devsu.events.ClienteEventType;

public interface ClienteEventPublisherPort {

    void publish(Cliente cliente, ClienteEventType eventType);
}
