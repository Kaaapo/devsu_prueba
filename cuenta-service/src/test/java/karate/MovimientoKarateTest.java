package karate;

import com.devsu.cuenta.CuentaServiceApplication;
import com.devsu.cuenta.domain.model.ClienteReplica;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.infrastructure.persistence.ClienteReplicaJpaRepository;
import com.devsu.cuenta.infrastructure.persistence.CuentaJpaRepository;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest(
        classes = CuentaServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class MovimientoKarateTest {

    @LocalServerPort
    private int port;

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

    @Karate.Test
    Karate testMovimientos() {
        return Karate.run("movimientos")
                .systemProperty("server.port", String.valueOf(port))
                .relativeTo(getClass());
    }
}
