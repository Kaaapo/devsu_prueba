package com.devsu.events;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ClienteEvent {

    private ClienteEventType eventType;
    private Long clienteId;
    private String nombre;
    private Boolean estado;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ClienteEvent() {
    }

    public ClienteEvent(ClienteEventType eventType, Long clienteId, String nombre, Boolean estado) {
        this.eventType = eventType;
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.estado = estado;
        this.timestamp = LocalDateTime.now();
    }

    public ClienteEventType getEventType() {
        return eventType;
    }

    public void setEventType(ClienteEventType eventType) {
        this.eventType = eventType;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
