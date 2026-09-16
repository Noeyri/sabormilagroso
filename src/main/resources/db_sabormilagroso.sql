-- 1. ROLES
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL -- Ej: 'ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_MESERO'
);

-- 2. USUARIOS
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol_id INT NOT NULL,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- 3. CATEGORÍAS
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

-- 4. PRODUCTOS (Platillos)
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    descripcion_larga TEXT,
    precio NUMERIC(10, 2) NOT NULL,
    precio_anterior NUMERIC(10, 2),
    disponible BOOLEAN DEFAULT TRUE,
    popular BOOLEAN DEFAULT FALSE,
    recomendado BOOLEAN DEFAULT FALSE,
    calificacion NUMERIC(3, 2) DEFAULT 0,
    tiempo_preparacion_min INT,
    imagen_url VARCHAR(500),
    stock INT DEFAULT 0,
    categoria_id INT NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- 5. IMÁGENES DE PRODUCTOS
CREATE TABLE imagenes_producto (
    id SERIAL PRIMARY KEY,
    imagen_url VARCHAR(255) NOT NULL,
    es_principal BOOLEAN DEFAULT FALSE,
    producto_id INT NOT NULL,
    CONSTRAINT fk_imagen_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- 6. CARRITOS (Cada usuario cliente tiene un carrito activo)
CREATE TABLE carritos (
    id SERIAL PRIMARY KEY,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT UNIQUE NOT NULL, -- 1 a 1 con el cliente
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- 7. ITEMS DEL CARRITO
CREATE TABLE items_carrito (
    id SERIAL PRIMARY KEY,
    cantidad INT NOT NULL DEFAULT 1,
    observaciones TEXT, -- Ej: "Sin sal", "Salsa aparte"
    carrito_id INT NOT NULL,
    producto_id INT NOT NULL,
    CONSTRAINT fk_item_carrito FOREIGN KEY (carrito_id) REFERENCES carritos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- 8. PEDIDOS (Órdenes confirmadas)
CREATE TABLE pedidos (
    id SERIAL PRIMARY KEY,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) DEFAULT 'PENDIENTE', -- PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO
    tipo_envio VARCHAR(30) NOT NULL,        -- 'LOCAL', 'LLEVAR', 'DELIVERY'
    direccion_envio TEXT,
    total NUMERIC(10, 2) NOT NULL,
    usuario_id INT NOT NULL,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- 9. ITEMS DEL PEDIDO (La foto fija de lo que compró en ese pedido)
CREATE TABLE items_pedido (
    id SERIAL PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL,
    subtotal NUMERIC(10, 2) NOT NULL,
    observaciones TEXT,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_pedido_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- 10. PAGOS
CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    monto NUMERIC(10, 2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL, -- 'TARJETA', 'EFECTIVO', 'YAPE', 'PLIN'
    estado VARCHAR(20) DEFAULT 'COMPLETADO',
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pedido_id INT UNIQUE NOT NULL,
    CONSTRAINT fk_pago_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
);

-- 11. DIRECCIONES (Direcciones guardadas del cliente)
CREATE TABLE direcciones (
    id SERIAL PRIMARY KEY,
    etiqueta VARCHAR(50) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    distrito VARCHAR(100),
    referencia VARCHAR(255),
    predeterminada BOOLEAN DEFAULT FALSE,
    usuario_id INT NOT NULL,
    CONSTRAINT fk_direccion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);