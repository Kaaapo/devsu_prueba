package com.devsu.cuenta.interfaces.rest;

import com.devsu.cuenta.application.ReporteService;
import com.devsu.cuenta.interfaces.rest.dto.ReporteEstadoCuentaResponse;
import com.devsu.cuenta.interfaces.rest.mapper.ReporteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ReporteEstadoCuentaResponse> estadoCuenta(
            @RequestParam String cliente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(
                ReporteMapper.toResponse(reporteService.generarEstadoCuenta(cliente, fechaInicio, fechaFin)));
    }
}
