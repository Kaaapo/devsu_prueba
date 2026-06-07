package com.devsu.cuenta;

import com.devsu.cuenta.domain.model.ClienteReplica;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.infrastructure.persistence.ClienteReplicaJpaRepository;
import com.devsu.cuenta.infrastructure.persistence.CuentaJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovimientoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CuentaJpaRepository cuentaRepository;

    @Autowired
    private ClienteReplicaJpaRepository clienteReplicaRepository;

    @BeforeEach
    void setUp() {
        cuentaRepository.deleteAll();
        clienteReplicaRepository.deleteAll();

        ClienteReplica cliente = new ClienteReplica();
        cliente.setClienteId(1L);
        cliente.setNombre("Marianela Montalvo");
        cliente.setEstado(true);
        clienteReplicaRepository.save(cliente);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("225487");
        cuenta.setTipo("Corriente");
        cuenta.setSaldoInicial(new BigDecimal("100"));
        cuenta.setSaldoDisponible(new BigDecimal("100"));
        cuenta.setEstado(true);
        cuenta.setClienteId(1L);
        cuentaRepository.save(cuenta);
    }

    @Test
    void debeRechazarRetiroSinSaldo() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", "225487");
        body.put("tipoMovimiento", "Retiro");
        body.put("valor", 575);

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void debeRechazarCuentaConClienteInactivo() throws Exception {
        ClienteReplica clienteInactivo = new ClienteReplica();
        clienteInactivo.setClienteId(2L);
        clienteInactivo.setNombre("Cliente Inactivo");
        clienteInactivo.setEstado(false);
        clienteReplicaRepository.save(clienteInactivo);

        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", "999888");
        body.put("tipo", "Ahorros");
        body.put("saldoInicial", 100);
        body.put("estado", true);
        body.put("clienteId", 2);

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El cliente Cliente Inactivo no está activo"));
    }

    @Test
    void debeRegistrarDepositoYActualizarSaldo() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", "225487");
        body.put("tipoMovimiento", "Deposito");
        body.put("valor", 600);

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldo").value(700));
    }
}
