# DevSu - Prueba Técnica Microservicios

Solución de 2 microservicios Spring Boot para administración de clientes, cuentas y movimientos bancarios.

## Arquitectura

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| `cliente-service` | 8081 | Persona, Cliente, CRUD `/api/clientes` |
| `cuenta-service` | 8082 | Cuenta, Movimiento, Reportes |

**Comunicación asíncrona:** RabbitMQ (`cliente.exchange` → `cliente.queue`). Al crear/actualizar/eliminar un cliente, `cuenta-service` sincroniza la tabla `cliente_replica`.

## Requisitos

- Java 21
- Maven 3.9+
- Docker y Docker Compose

## Despliegue con Docker (F7)

```bash
docker-compose up --build
```

Servicios disponibles:

- Cliente API: http://localhost:8081/api/clientes
- Cuenta API: http://localhost:8082/api/cuentas
- Movimientos: http://localhost:8082/api/movimientos
- Reportes: http://localhost:8082/api/reportes
- RabbitMQ Management: http://localhost:15672 (guest/guest)

## Ejecución local (sin Docker)

1. Levantar PostgreSQL en puertos 5433 (cliente_db) y 5434 (cuenta_db)
2. Levantar RabbitMQ en puerto 5672
3. Ejecutar scripts de `docker/postgres-cliente/init.sql` y `docker/postgres-cuenta/init.sql`
4. Compilar y ejecutar:

```bash
mvn clean package -DskipTests
java -jar cliente-service/target/cliente-service-1.0.0.jar
java -jar cuenta-service/target/cuenta-service-1.0.0.jar
```

## Pruebas

```bash
# Todas las pruebas unitarias e integración
mvn test

# Solo cliente-service (F5)
mvn test -pl cliente-service

# Solo cuenta-service (F6)
mvn test -pl cuenta-service

# Karate (requiere cuenta-service corriendo en 8082)
mvn test -pl cuenta-service -Dtest=MovimientoKarateTest
```

## Endpoints principales

### Clientes (CRUD)
- `GET/POST /api/clientes`
- `GET/PUT/PATCH/DELETE /api/clientes/{id}`

### Cuentas (CRU)
- `GET/POST /api/cuentas`
- `GET/PUT/PATCH /api/cuentas/{id}`

### Movimientos (CRU)
- `GET/POST /api/movimientos`
- `GET/PUT/PATCH /api/movimientos/{id}`

### Reportes (F4)
- `GET /api/reportes?cliente={nombre}&fechaInicio={yyyy-MM-dd}&fechaFin={yyyy-MM-dd}`

## Reglas de negocio

- **F2:** Al registrar un movimiento se actualiza `saldoDisponible` y se guarda el historial.
- **F3:** Si el saldo queda negativo, retorna HTTP 409 con mensaje `"Saldo no disponible"`.
- **F4:** Reporte JSON con cuentas del cliente y movimientos en el rango de fechas.

## Entregables

- `BaseDatos.sql` - Esquema y datos de casos de uso 1-5
- `postman/DevSu-API.postman_collection.json` - Validación de endpoints
- Prueba unitaria F5: `ClienteTest`
- Prueba integración F6: `MovimientoIntegrationTest`
- Karate: `cuenta-service/src/test/java/karate/movimientos.feature`

## Consideraciones de escalabilidad y resiliencia

- **Escalabilidad:** Cada microservicio es stateless y puede replicarse horizontalmente detrás de un load balancer.
- **Resiliencia:** RabbitMQ permite reintentos y DLQ para eventos de cliente; se recomienda circuit breaker en llamadas futuras entre servicios.
- **Rendimiento:** Bases de datos separadas por dominio evitan cuellos de botella; índices en `numero_cuenta`, `cliente_id` y `fecha` de movimientos.
