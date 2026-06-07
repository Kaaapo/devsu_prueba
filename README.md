# DevSu - Prueba Técnica Microservicios

Solución de 2 microservicios Spring Boot para administración de clientes, cuentas y movimientos bancarios.

## Arquitectura

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| `cliente-service` | 8081 | Persona, Cliente, CRUD `/api/clientes` |
| `cuenta-service` | 8082 | Cuenta, Movimiento, Reportes |

**Comunicación asíncrona:** RabbitMQ (`cliente.exchange` → `cliente.queue`). Al crear/actualizar/eliminar un cliente, `cuenta-service` sincroniza la tabla `cliente_replica`.

```
Postman ──► cliente-service:8081 ──► postgres-cliente:5433
                │
                └── RabbitMQ:5672 ──► cuenta-service:8082 ──► postgres-cuenta:5434
```

---

## Guía de despliegue con Docker

### 1. Requisitos previos

| Herramienta | Versión mínima | Verificar |
|---|---|---|
| Docker Desktop | 4.x+ | `docker --version` |
| Docker Compose | v2+ | `docker compose version` |
| Java (solo para tests locales) | 21 | `java -version` |
| Maven (solo para tests locales) | 3.9+ | `mvn -version` |
| Postman | v9.13+ | Importar colección incluida |

> **Importante:** Docker Desktop debe estar **abierto y en ejecución** antes de continuar.

### 2. Clonar o abrir el proyecto

```bash
cd devsu_prueba
```

### 3. Levantar todos los servicios

```bash
docker-compose up --build -d
```

Este comando construye y levanta 5 contenedores:

| Contenedor | Imagen | Puerto host |
|---|---|---|
| `devsu-postgres-cliente` | postgres:16-alpine | 5433 |
| `devsu-postgres-cuenta` | postgres:16-alpine | 5434 |
| `devsu-rabbitmq` | rabbitmq:3.13-management | 5672, 15672 |
| `devsu-cliente-service` | build local | 8081 |
| `devsu-cuenta-service` | build local | 8082 |

La primera ejecución puede tardar **3–5 minutos** (descarga de imágenes + compilación Maven).

### 4. Verificar que todo esté corriendo

```bash
docker-compose ps
```

Todos los servicios deben mostrar estado `Up`. Las bases de datos deben mostrar `(healthy)`.

Esperar ~30 segundos adicionales para que Spring Boot termine de arrancar, luego probar:

```bash
# PowerShell
Invoke-RestMethod http://localhost:8081/api/clientes
Invoke-RestMethod http://localhost:8082/api/cuentas
```

**Respuesta esperada:**
- Clientes: 3 registros (Jose Lema, Marianela Montalvo, Juan Osorio)
- Cuentas: 5 registros

### 5. URLs de los servicios

| Servicio | URL base |
|---|---|
| API Clientes | http://localhost:8081/api/clientes |
| API Cuentas | http://localhost:8082/api/cuentas |
| API Movimientos | http://localhost:8082/api/movimientos |
| API Reportes | http://localhost:8082/api/reportes |
| RabbitMQ Management | http://localhost:15672 (usuario: `guest`, contraseña: `guest`) |

### 6. Datos iniciales (casos de uso 1–5)

Los datos se cargan automáticamente al crear los contenedores PostgreSQL mediante:
- `docker/postgres-cliente/init.sql` → base `cliente_db`
- `docker/postgres-cuenta/init.sql` → base `cuenta_db`

El archivo `BaseDatos.sql` en la raíz contiene el mismo esquema y datos en un solo script (entregable de la prueba).

**Clientes precargados:**

| ID | Nombre | Teléfono |
|---|---|---|
| 1 | Jose Lema | 098254785 |
| 2 | Marianela Montalvo | 097548965 |
| 3 | Juan Osorio | 098874587 |

**Cuentas precargadas:**

| Número | Tipo | Saldo disponible | Cliente |
|---|---|---|---|
| 478758 | Ahorros | 1425 | Jose Lema |
| 225487 | Corriente | 700 | Marianela Montalvo |
| 495878 | Ahorros | 150 | Juan Osorio |
| 496825 | Ahorros | 0 | Marianela Montalvo |
| 585545 | Corriente | 1000 | Jose Lema |

### 7. Probar con Postman

#### Importar la colección

1. Abrir Postman
2. **Import** → seleccionar `postman/DevSu-API.postman_collection.json`
3. Verificar variables de la colección:
   - `cliente_host` = `http://localhost:8081`
   - `cuenta_host` = `http://localhost:8082`
   - `cliente_id` = se llena automáticamente al ejecutar **Crear cliente (RabbitMQ)**

#### Flujo de prueba recomendado

**Paso 1 — F1: Listar clientes**
```
GET http://localhost:8081/api/clientes
```
Esperado: HTTP 200, array con 3 clientes.

**Paso 2 — F1: Listar cuentas**
```
GET http://localhost:8082/api/cuentas
```
Esperado: HTTP 200, array con 5 cuentas.

**Paso 3 — F2: Registrar depósito**
```
POST http://localhost:8082/api/movimientos
Content-Type: application/json

{
  "numeroCuenta": "225487",
  "tipoMovimiento": "Deposito",
  "valor": 100,
  "fecha": "2022-02-15"
}
```
Esperado: HTTP 201, saldo actualizado.

**Paso 4 — F3: Retiro sin saldo (debe fallar)**
```
POST http://localhost:8082/api/movimientos
Content-Type: application/json

{
  "numeroCuenta": "496825",
  "tipoMovimiento": "Retiro",
  "valor": 100
}
```
Esperado: HTTP 409
```json
{
  "message": "Saldo no disponible"
}
```

**Paso 5 — F4: Reporte estado de cuenta**
```
GET http://localhost:8082/api/reportes?cliente=Marianela Montalvo&fechaInicio=2022-02-01&fechaFin=2022-02-28
```
Esperado: HTTP 200, JSON con cuentas y movimientos de Marianela en febrero 2022.

**Paso 6 — Comunicación asíncrona (RabbitMQ)**
```
POST http://localhost:8081/api/clientes
Content-Type: application/json

{
  "nombre": "Cliente Prueba",
  "genero": "M",
  "edad": 30,
  "identificacion": "ID999",
  "direccion": "Calle Test",
  "telefono": "099999999",
  "contrasena": "test123",
  "estado": true
}
```
Esperar 2–3 segundos, luego crear una cuenta con el `clienteId` retornado:
```
POST http://localhost:8082/api/cuentas
Content-Type: application/json

{
  "numeroCuenta": "999001",
  "tipo": "Ahorros",
  "saldoInicial": 500,
  "estado": true,
  "clienteId": <clienteId del paso anterior>
}
```
Esperado: HTTP 201. Si funciona, RabbitMQ sincronizó el cliente correctamente.

### 8. Ejecutar pruebas automatizadas

```bash
mvn test
```

| Prueba | Archivo | Requisito |
|---|---|---|
| F5 — Unitario dominio Cliente | `cliente-service/.../ClienteTest.java` | 4 tests |
| F6 — Integración movimientos y cuentas | `cuenta-service/.../MovimientoIntegrationTest.java` | 3 tests |
| Karate DSL | `cuenta-service/src/test/resources/karate/movimientos.feature` | 2 escenarios (F2 + F3) |

Reporte HTML de Karate: `cuenta-service/target/karate-reports/karate-summary.html`

### 9. Ver logs (si algo falla)

```bash
# Todos los servicios
docker-compose logs -f

# Solo un microservicio
docker-compose logs -f cliente-service
docker-compose logs -f cuenta-service
```

### 10. Detener y limpiar

```bash
# Detener contenedores (conserva datos)
docker-compose down

# Detener y eliminar volúmenes (datos frescos en próximo up)
docker-compose down -v

# Volver a levantar con datos iniciales limpios
docker-compose up --build -d
```

### 11. Solución de problemas

| Problema | Solución |
|---|---|
| `port is already allocated` | Otro proceso usa 8081/8082/5433/5434. Detenerlo o cambiar puertos en `docker-compose.yml` |
| API retorna error de conexión | Esperar 30–60 s tras `docker-compose up`. Revisar logs con `docker-compose logs cliente-service` |
| `Connection refused` en Postman | Verificar que Docker Desktop esté abierto y `docker-compose ps` muestre todo `Up` |
| Cliente creado pero cuenta falla con 404 | Esperar 2–3 s para que RabbitMQ sincronice. Reintentar POST de cuenta |
| Datos incorrectos o vacíos | Ejecutar `docker-compose down -v` y volver a levantar para recargar `init.sql` |
| Build falla en Docker | Verificar conexión a internet (Maven descarga dependencias durante el build) |

---

## Ejecución local (sin Docker)

1. Levantar PostgreSQL en puertos 5433 (`cliente_db`) y 5434 (`cuenta_db`)
2. Levantar RabbitMQ en puerto 5672
3. Ejecutar `docker/postgres-cliente/init.sql` en `cliente_db` y `docker/postgres-cuenta/init.sql` en `cuenta_db`
4. Compilar y ejecutar:

```bash
mvn clean package -DskipTests
java -jar cliente-service/target/cliente-service-1.0.0.jar
java -jar cuenta-service/target/cuenta-service-1.0.0.jar
```

---

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
- **Cuentas:** No se puede crear ni reasignar una cuenta a un cliente inactivo; retorna HTTP 400 con mensaje `"El cliente {nombre} no está activo"`.

## Entregables

| Archivo | Descripción |
|---|---|
| `BaseDatos.sql` | Esquema y datos de casos de uso 1–5 |
| `postman/DevSu-API.postman_collection.json` | Colección Postman para validar endpoints |
| `docker/postgres-*/init.sql` | Scripts de carga automática en Docker |
| `ClienteTest.java` | F5 — Prueba unitaria dominio Cliente |
| `MovimientoIntegrationTest.java` | F6 — Pruebas de integración (movimientos y cliente inactivo) |
| `movimientos.feature` | Karate DSL — pruebas HTTP F2 y F3 (`src/test/resources/karate/`) |

## Consideraciones de escalabilidad, resiliencia y rendimiento

La prueba pedía contemplar estos factores aunque no haya sido necesario implementarlos todos. Esto es lo que tuve en cuenta al diseñar la solución:

### Escalabilidad

Cada microservicio no guarda estado en memoria: la información vive en PostgreSQL o en RabbitMQ. Eso significa que si mañana hay más tráfico, se pueden levantar varias instancias del mismo servicio (por ejemplo, 2 o 3 `cuenta-service`) y poner un load balancer delante sin cambiar código.

En este proyecto cada MS tiene su propia base de datos, así que también se podrían escalar por separado según donde esté el cuello de botella. Si hay muchos movimientos pero pocos cambios de clientes, tendría más sentido escalar solo `cuenta-service`.

### Resiliencia

Para la comunicación entre microservicios elegí RabbitMQ en lugar de llamadas HTTP directas. La razón es simple: si `cuenta-service` está reiniciando o caído un momento, el evento del cliente no se pierde, queda en la cola hasta que el servicio vuelva a estar disponible.

En un entorno productivo se podría configurar reintentos automáticos y una DLQ (cola de mensajes fallidos) para revisar manualmente los eventos que no se procesaron. También dejé documentado el uso de circuit breaker para futuras integraciones síncronas, aunque en esta versión no fue necesario porque la comunicación principal es asíncrona.

### Rendimiento

Separar `cliente_db` y `cuenta_db` evita que las consultas de movimientos compitan con las de clientes en la misma base. Cada dominio crece y se optimiza por su lado.

Para las consultas más frecuentes dejé campos que conviene indexar en producción: `numero_cuenta` (al registrar movimientos), `cliente_id` (al listar cuentas y armar reportes) y `fecha` en movimientos (para el reporte F4 por rango de fechas). JPA crea las tablas con `ddl-auto: update` en desarrollo; en producción usaría scripts SQL con índices explícitos.

:D