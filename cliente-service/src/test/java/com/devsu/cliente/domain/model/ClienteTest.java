package com.devsu.cliente.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteTest {

    @Test
    void debeEstarActivoPorDefecto() {
        Cliente cliente = new Cliente();
        cliente.setEstado(true);

        assertTrue(cliente.estaActivo());
    }

    @Test
    void debeDesactivarCliente() {
        Cliente cliente = new Cliente();
        cliente.setEstado(true);

        cliente.desactivar();

        assertFalse(cliente.estaActivo());
    }

    @Test
    void debeActivarCliente() {
        Cliente cliente = new Cliente();
        cliente.setEstado(false);

        cliente.activar();

        assertTrue(cliente.estaActivo());
    }

    @Test
    void debeRechazarEdadNegativa() {
        Cliente cliente = new Cliente();
        cliente.setEdad(-1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, cliente::validarEdad);

        assertTrue(exception.getMessage().contains("edad"));
    }
}
