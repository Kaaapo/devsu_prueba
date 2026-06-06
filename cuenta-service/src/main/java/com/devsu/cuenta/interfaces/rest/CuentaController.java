package com.devsu.cuenta.interfaces.rest;

import com.devsu.cuenta.application.CuentaService;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.interfaces.rest.dto.CuentaRequest;
import com.devsu.cuenta.interfaces.rest.dto.CuentaResponse;
import com.devsu.cuenta.interfaces.rest.mapper.CuentaMapper;
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
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CuentaRequest request) {
        Cuenta cuenta = cuentaService.crear(CuentaMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(CuentaResponse.from(cuenta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(CuentaResponse.from(cuentaService.obtenerPorId(id)));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listar() {
        List<CuentaResponse> cuentas = cuentaService.listar().stream()
                .map(CuentaResponse::from)
                .toList();
        return ResponseEntity.ok(cuentas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequest request) {
        Cuenta cuenta = cuentaService.actualizarCompleto(id, CuentaMapper.toEntity(request));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuentaResponse> actualizarParcial(
            @PathVariable Long id,
            @RequestBody CuentaRequest request) {
        Cuenta cuenta = cuentaService.actualizarParcial(id, CuentaMapper.toEntity(request));
        return ResponseEntity.ok(CuentaResponse.from(cuenta));
    }
}
