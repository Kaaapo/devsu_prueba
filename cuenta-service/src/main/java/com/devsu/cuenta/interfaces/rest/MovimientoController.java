package com.devsu.cuenta.interfaces.rest;

import com.devsu.cuenta.application.MovimientoService;
import com.devsu.cuenta.domain.model.Movimiento;
import com.devsu.cuenta.interfaces.rest.dto.MovimientoRequest;
import com.devsu.cuenta.interfaces.rest.dto.MovimientoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        Movimiento movimiento = movimientoService.registrar(
                request.getNumeroCuenta(),
                request.getTipoMovimiento(),
                request.getValor(),
                request.getFecha()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(MovimientoResponse.from(movimiento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(MovimientoResponse.from(movimientoService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listar() {
        List<MovimientoResponse> movimientos = movimientoService.listar().stream()
                .map(MovimientoResponse::from)
                .toList();
        return ResponseEntity.ok(movimientos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody Movimiento movimiento) {
        Movimiento actualizado = movimientoService.actualizarCompleto(id, movimiento);
        return ResponseEntity.ok(MovimientoResponse.from(actualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovimientoResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Movimiento movimiento) {
        Movimiento actualizado = movimientoService.actualizarParcial(id, movimiento);
        return ResponseEntity.ok(MovimientoResponse.from(actualizado));
    }
}
