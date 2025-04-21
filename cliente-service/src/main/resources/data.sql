CREATE TABLE clientes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  cliente_id VARCHAR(255) UNIQUE NOT NULL,
  contraseña VARCHAR(255) NOT NULL,
  estado BOOLEAN,
  nombre VARCHAR(255) NOT NULL,
  genero VARCHAR(10),
  edad INT,
  identificacion VARCHAR(50) UNIQUE NOT NULL,
  direccion VARCHAR(255),
  telefono VARCHAR(20)
);