package com.devsu.cliente.application;

import com.devsu.cliente.domain.model.Cliente;
import com.devsu.cliente.domain.port.ClienteEventPublisherPort;
import com.devsu.cliente.domain.port.ClienteRepositoryPort;
import com.devsu.events.ClienteEventType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepositoryPort clienteRepository;
    private final ClienteEventPublisherPort eventPublisher;

    @Transactional
    public Cliente crear(Cliente cliente) {
        if (clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe un cliente con la identificación: " + cliente.getIdentificacion());
        }
        cliente.validarEdad();
        Cliente guardado = clienteRepository.save(cliente);
        eventPublisher.publish(guardado, ClienteEventType.CREADO);
        return guardado;
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente con ID " + id + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente actualizarCompleto(Long id, Cliente datos) {
        Cliente existente = obtenerPorId(id);
        aplicarDatos(existente, datos);
        existente.validarEdad();
        Cliente actualizado = clienteRepository.save(existente);
        eventPublisher.publish(actualizado, ClienteEventType.ACTUALIZADO);
        return actualizado;
    }

    @Transactional
    public Cliente actualizarParcial(Long id, Cliente datos) {
        Cliente existente = obtenerPorId(id);
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        if (datos.getGenero() != null) existente.setGenero(datos.getGenero());
        if (datos.getEdad() != null) existente.setEdad(datos.getEdad());
        if (datos.getIdentificacion() != null) existente.setIdentificacion(datos.getIdentificacion());
        if (datos.getDireccion() != null) existente.setDireccion(datos.getDireccion());
        if (datos.getTelefono() != null) existente.setTelefono(datos.getTelefono());
        if (datos.getContrasena() != null) existente.setContrasena(datos.getContrasena());
        if (datos.getEstado() != null) existente.setEstado(datos.getEstado());
        existente.validarEdad();
        Cliente actualizado = clienteRepository.save(existente);
        eventPublisher.publish(actualizado, ClienteEventType.ACTUALIZADO);
        return actualizado;
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        clienteRepository.deleteById(id);
        eventPublisher.publish(cliente, ClienteEventType.ELIMINADO);
    }

    private void aplicarDatos(Cliente destino, Cliente origen) {
        destino.setNombre(origen.getNombre());
        destino.setGenero(origen.getGenero());
        destino.setEdad(origen.getEdad());
        destino.setIdentificacion(origen.getIdentificacion());
        destino.setDireccion(origen.getDireccion());
        destino.setTelefono(origen.getTelefono());
        destino.setContrasena(origen.getContrasena());
        destino.setEstado(origen.getEstado());
    }
}
