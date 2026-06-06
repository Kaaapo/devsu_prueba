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

INSERT INTO clientes (cliente_id, nombre, direccion, telefono, identificacion, contrasena, estado)
VALUES
    (1, 'Jose Lema', 'Otavalo sn y principal', '098254785', 'ID001', '1234', TRUE),
    (2, 'Marianela Montalvo', 'Amazonas y NNUU', '097548965', 'ID002', '5678', TRUE),
    (3, 'Juan Osorio', '13 junio y Equinoccial', '098874587', 'ID003', '1245', TRUE)
ON CONFLICT (cliente_id) DO NOTHING;

SELECT setval('clientes_cliente_id_seq', (SELECT MAX(cliente_id) FROM clientes));
