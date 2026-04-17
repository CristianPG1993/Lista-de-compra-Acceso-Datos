-- ========================================
-- BASE DE DATOS: listaCompra
-- ========================================



-- ========================================
-- TABLA: usuarios
-- Tabla que almacena los usuarios del sistema
-- ========================================
CREATE TABLE IF NOT EXISTS usuarios(
id SERIAL PRIMARY KEY,
nombre VARCHAR(50) NOT NULL,
apellido VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL
);

-- ========================================
-- TABLA: listasCompra
-- Tabla que almacena las listas de compra generadas
-- ========================================

CREATE TABLE IF NOT EXISTS listasCompra (
idLista SERIAL PRIMARY KEY,
idUsuario INT NOT NULL,
nombreCompra VARCHAR(50) NOT NULL,
fechaCreacion DATE,
FOREIGN KEY (idUsuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- ========================================
-- TABLA: productos
-- Tabla que almacena los productos que se añaden en itemsLista
-- ========================================

CREATE TABLE IF NOT EXISTS productos (
idProducto SERIAL PRIMARY KEY,
nombre VARCHAR(50) NOT NULL,
precio DECIMAL(10,2) NOT NULL,
categoria VARCHAR(50)
);

-- ========================================
-- TABLA: itemsLista
-- Tabla que almacena todos los productos de una lista
-- ========================================

CREATE TABLE IF NOT EXISTS itemsLista (
idItem SERIAL PRIMARY KEY,
idProducto INT NOT NULL,
idLista INT NOT NULL,
cantidad INT NOT NULL,
precioUnitario DECIMAL(10,2) NOT NULL,
comprado BOOLEAN DEFAULT FALSE,
FOREIGN KEY(idProducto) REFERENCES productos(idProducto),
FOREIGN KEY (idLista) REFERENCES listasCompra(idLista) ON DELETE CASCADE,
UNIQUE(idLista,idProducto)
);


