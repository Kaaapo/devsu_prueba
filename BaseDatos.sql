
-- BaseDatos.sql - DevSu Prueba Técnica Microservicios
-- Esquema y datos iniciales para cliente_db y cuenta_db


--  BASE DE DATOS: cliente_db 

CREATE TABLE IF NOT EXISTS clientes (
    cliente_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Caso de uso 1: Creación de usuarios
INSERT INTO clientes (cliente_id, nombre, direccion, telefono, identificacion, contrasena, estado) VALUES
    (1, 'Jose Lema', 'Otavalo sn y principal', '098254785', 'ID001', '1234', TRUE),
    (2, 'Marianela Montalvo', 'Amazonas y NNUU', '097548965', 'ID002', '5678', TRUE),
    (3, 'Juan Osorio', '13 junio y Equinoccial', '098874587', 'ID003', '1245', TRUE);

--  BASE DE DATOS: cuenta_db 

CREATE TABLE IF NOT EXISTS cliente_replica (
    cliente_id BIGINT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS cuentas (
    cuenta_id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo VARCHAR(20) NOT NULL,
    saldo_inicial NUMERIC(19, 2) NOT NULL DEFAULT 0,
    saldo_disponible NUMERIC(19, 2) NOT NULL DEFAULT 0,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    movimiento_id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL
);

-- Réplica de clientes sincronizada vía RabbitMQ
INSERT INTO cliente_replica (cliente_id, nombre, estado) VALUES
    (1, 'Jose Lema', TRUE),
    (2, 'Marianela Montalvo', TRUE),
    (3, 'Juan Osorio', TRUE);

-- Caso de uso 2 y 3: Cuentas iniciales + cuenta corriente Jose Lema
INSERT INTO cuentas (cuenta_id, numero_cuenta, tipo, saldo_inicial, saldo_disponible, estado, cliente_id) VALUES
    (1, '478758', 'Ahorros', 2000.00, 1425.00, TRUE, 1),
    (2, '225487', 'Corriente', 100.00, 700.00, TRUE, 2),
    (3, '495878', 'Ahorros', 0.00, 150.00, TRUE, 3),
    (4, '496825', 'Ahorros', 540.00, 0.00, TRUE, 2),
    (5, '585545', 'Corriente', 1000.00, 1000.00, TRUE, 1);

-- Caso de uso 4: Movimientos realizados
INSERT INTO movimientos (movimiento_id, fecha, tipo_movimiento, valor, saldo, cuenta_id) VALUES
    (1, '2022-02-01', 'Retiro', -575.00, 1425.00, 1),
    (2, '2022-02-10', 'Deposito', 600.00, 700.00, 2),
    (3, '2022-02-05', 'Deposito', 150.00, 150.00, 3),
    (4, '2022-02-08', 'Retiro', -540.00, 0.00, 4);
