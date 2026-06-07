package com.devsu.cuenta.domain.exception;

public class ClienteInactivoException extends RuntimeException {

    public ClienteInactivoException(String nombreCliente) {
        super("El cliente " + nombreCliente + " no está activo");
    }
}
