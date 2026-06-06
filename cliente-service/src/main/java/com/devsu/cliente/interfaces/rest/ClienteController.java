package com.devsu.cliente.interfaces.rest;

import com.devsu.cliente.application.ClienteService;
import com.devsu.cliente.domain.model.Cliente;
import com.devsu.cliente.interfaces.rest.dto.ClienteRequest;
import com.devsu.cliente.interfaces.rest.dto.ClienteResponse;
import com.devsu.cliente.interfaces.rest.mapper.ClienteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = clienteService.crear(ClienteMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponse.from(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteResponse.from(clienteService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<ClienteResponse> clientes = clienteService.listar().stream()
                .map(ClienteResponse::from)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        Cliente cliente = clienteService.actualizarCompleto(id, ClienteMapper.toEntity(request));
        return ResponseEntity.ok(ClienteResponse.from(cliente));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody ClienteRequest request) {
        Cliente cliente = clienteService.actualizarParcial(id, ClienteMapper.toEntity(request));
        return ResponseEntity.ok(ClienteResponse.from(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
