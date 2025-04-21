-- Para cliente-service
CREATE TABLE clientes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  cliente_id VARCHAR(255) UNIQUE NOT NULL,
  contraseña VARCHAR(255) NOT NULL,
  estado BOOLEAN,
  nombre VARCHAR(255),
  genero VARCHAR(10),
  edad INT,
  identificacion VARCHAR(50),
  direccion VARCHAR(255),
  telefono VARCHAR(20)
);

-- Para cuenta-service
CREATE TABLE cuentas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  numero_cuenta VARCHAR(255) UNIQUE NOT NULL,
  tipo_cuenta VARCHAR(50),
  saldo DECIMAL(19,2),
  estado BOOLEAN,
  cliente_id VARCHAR(255)
);
CREATE TABLE movimientos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  fecha TIMESTAMP,
  tipo_movimiento VARCHAR(50),
  valor DECIMAL(19,2),
  saldo_disponible DECIMAL(19,2),
  cuenta_id BIGINT,
  FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);
