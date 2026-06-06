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

INSERT INTO cliente_replica (cliente_id, nombre, estado) VALUES
    (1, 'Jose Lema', TRUE),
    (2, 'Marianela Montalvo', TRUE),
    (3, 'Juan Osorio', TRUE)
ON CONFLICT (cliente_id) DO NOTHING;

INSERT INTO cuentas (cuenta_id, numero_cuenta, tipo, saldo_inicial, saldo_disponible, estado, cliente_id) VALUES
    (1, '478758', 'Ahorros', 2000.00, 1425.00, TRUE, 1),
    (2, '225487', 'Corriente', 100.00, 700.00, TRUE, 2),
    (3, '495878', 'Ahorros', 0.00, 150.00, TRUE, 3),
    (4, '496825', 'Ahorros', 540.00, 0.00, TRUE, 2),
    (5, '585545', 'Corriente', 1000.00, 1000.00, TRUE, 1)
ON CONFLICT (cuenta_id) DO NOTHING;

INSERT INTO movimientos (movimiento_id, fecha, tipo_movimiento, valor, saldo, cuenta_id) VALUES
    (1, '2022-02-01', 'Retiro', -575.00, 1425.00, 1),
    (2, '2022-02-10', 'Deposito', 600.00, 700.00, 2),
    (3, '2022-02-05', 'Deposito', 150.00, 150.00, 3),
    (4, '2022-02-08', 'Retiro', -540.00, 0.00, 4)
ON CONFLICT (movimiento_id) DO NOTHING;

SELECT setval('cuentas_cuenta_id_seq', (SELECT MAX(cuenta_id) FROM cuentas));
SELECT setval('movimientos_movimiento_id_seq', (SELECT MAX(movimiento_id) FROM movimientos));
