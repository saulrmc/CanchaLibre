USE canchaLibre;
GO

-- ============================================================================
-- 1. CUENTA_USUARIO (Central Credentials Layout)
-- ============================================================================
IF OBJECT_ID('dbo.CUENTA_USUARIO', 'U') IS NULL
BEGIN
CREATE TABLE dbo.CUENTA_USUARIO (
                                    id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                                    correo VARCHAR(100) NOT NULL,
                                    contrasena VARCHAR(255) NOT NULL,
                                    intentosFallidos INT NOT NULL DEFAULT 0,
                                    ultimaSesion DATETIME NULL,
                                    activo BIT NOT NULL DEFAULT 1
);
END
GO

-- ============================================================================
-- 2. PROFILE SCHEMAS (Referencing CUENTA_USUARIO)
-- ============================================================================

-- CLIENTE
IF OBJECT_ID('dbo.CLIENTE', 'U') IS NULL
BEGIN
CREATE TABLE dbo.CLIENTE (
                             id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                             idCuentaUsuario INT NOT NULL,
                             nombres VARCHAR(150) NOT NULL,
                             telefono VARCHAR(20) NOT NULL,
                             calificacion INT NOT NULL DEFAULT 5,
                             activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.CLIENTE
    ADD CONSTRAINT FK_CLIENTE_CUENTA_USUARIO
        FOREIGN KEY (idCuentaUsuario) REFERENCES dbo.CUENTA_USUARIO(id);
END
GO

-- PROPIETARIO
IF OBJECT_ID('dbo.PROPIETARIO', 'U') IS NULL
BEGIN
CREATE TABLE dbo.PROPIETARIO (
                                 id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                                 idCuentaUsuario INT NOT NULL,
                                 nombres VARCHAR(150) NOT NULL,
                                 telefono VARCHAR(20) NOT NULL,
                                 calificacion INT NOT NULL DEFAULT 5,
                                 activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.PROPIETARIO
    ADD CONSTRAINT FK_PROPIETARIO_CUENTA_USUARIO
        FOREIGN KEY (idCuentaUsuario) REFERENCES dbo.CUENTA_USUARIO(id);
END
GO

-- ADMINISTRADOR
IF OBJECT_ID('dbo.ADMINISTRADOR', 'U') IS NULL
BEGIN
CREATE TABLE dbo.ADMINISTRADOR (
                                   id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                                   idCuentaUsuario INT NOT NULL,
                                   nombres VARCHAR(150) NOT NULL,
                                   telefono VARCHAR(20) NOT NULL,
                                   activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.ADMINISTRADOR
    ADD CONSTRAINT FK_ADMINISTRADOR_CUENTA_USUARIO
        FOREIGN KEY (idCuentaUsuario) REFERENCES dbo.CUENTA_USUARIO(id);
END
GO

-- ============================================================================
-- 3. INFRASTRUCTURE & ENGAGEMENT SCHEMAS
-- ============================================================================

-- CANCHA
IF OBJECT_ID('dbo.CANCHA', 'U') IS NULL
BEGIN
CREATE TABLE dbo.CANCHA (
                            id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                            idPropietario INT NOT NULL,
                            nombre VARCHAR(100) NOT NULL,
                            descripcion VARCHAR(500) NULL,
                            deportes VARCHAR(100) NOT NULL, -- Managed as CSV list or string token from Java List
                            imagenUrl VARCHAR(255) NULL,
                            disponible BIT NOT NULL DEFAULT 1,
                            direccion VARCHAR(255) NOT NULL,
                            activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.CANCHA
    ADD CONSTRAINT FK_CANCHA_PROPIETARIO
        FOREIGN KEY (idPropietario) REFERENCES dbo.PROPIETARIO(id);
END
GO

-- RESENA
IF OBJECT_ID('dbo.RESENA', 'U') IS NULL
BEGIN
CREATE TABLE dbo.RESENA (
                            id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                            idCancha INT NOT NULL,
                            idCliente INT NOT NULL,
                            estrellas INT NOT NULL CHECK (estrellas BETWEEN 1 AND 5),
                            comentario VARCHAR(500) NULL,
                            fechaResena DATETIME NOT NULL,
                            activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.RESENA
    ADD CONSTRAINT FK_RESENA_CANCHA
        FOREIGN KEY (idCancha) REFERENCES dbo.CANCHA(id);

ALTER TABLE dbo.RESENA
    ADD CONSTRAINT FK_RESENA_CLIENTE
        FOREIGN KEY (idCliente) REFERENCES dbo.CLIENTE(id);
END
GO

-- ============================================================================
-- 4. BUSINESS & REVENUE TRANSACTIONS
-- ============================================================================

-- RESERVA
IF OBJECT_ID('dbo.RESERVA', 'U') IS NULL
BEGIN
CREATE TABLE dbo.RESERVA (
                             id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                             idCliente INT NOT NULL,
                             idCancha INT NOT NULL,
                             fechaHora DATETIME NOT NULL,
                             duracion TIME NOT NULL,
                             estado VARCHAR(50) NOT NULL DEFAULT 'ESPERA',
                             activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.RESERVA
    ADD CONSTRAINT FK_RESERVA_CLIENTE
        FOREIGN KEY (idCliente) REFERENCES dbo.CLIENTE(id);

ALTER TABLE dbo.RESERVA
    ADD CONSTRAINT FK_RESERVA_CANCHA
        FOREIGN KEY (idCancha) REFERENCES dbo.CANCHA(id);
END
GO

-- PAGO
IF OBJECT_ID('dbo.PAGO', 'U') IS NULL
BEGIN
CREATE TABLE dbo.PAGO (
                          id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                          idReserva INT NOT NULL,
                          metodoPago VARCHAR(50) NOT NULL,
                          monto DECIMAL(10,2) NOT NULL,
                          fechaPago DATETIME NOT NULL,
                          activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.PAGO
    ADD CONSTRAINT FK_PAGO_RESERVA
        FOREIGN KEY (idReserva) REFERENCES dbo.RESERVA(id);
END
GO

-- COMPROBANTE
IF OBJECT_ID('dbo.COMPROBANTE', 'U') IS NULL
BEGIN
CREATE TABLE dbo.COMPROBANTE (
                                 id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
                                 idReserva INT NOT NULL,
                                 igv DECIMAL(4,2) NOT NULL DEFAULT 0.18,
                                 fechaEmision DATETIME NOT NULL,
                                 total DECIMAL(10,2) NOT NULL,
                                 activo BIT NOT NULL DEFAULT 1
);

ALTER TABLE dbo.COMPROBANTE
    ADD CONSTRAINT FK_COMPROBANTE_RESERVA
        FOREIGN KEY (idReserva) REFERENCES dbo.RESERVA(id);
END
GO