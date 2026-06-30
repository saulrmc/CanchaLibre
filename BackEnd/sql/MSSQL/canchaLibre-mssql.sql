USE [CanchaLibre];
GO

IF OBJECT_ID('dbo.CUENTA_USUARIO', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CUENTA_USUARIO (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        userName VARCHAR(50) NOT NULL UNIQUE,
        [password] VARCHAR(50) NOT NULL,
        rol VARCHAR(15) NOT NULL,
        intentosFallidos INT NOT NULL DEFAULT 0,
        ultimaSesion DATETIME2 NULL,
        fechaBloqueo DATETIME2 NULL,
        activo BIT NOT NULL DEFAULT 0
    );
END;
GO

IF OBJECT_ID('dbo.CLIENTE', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CLIENTE (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCuentaUsuario INT NULL,
        nombres VARCHAR(150) NOT NULL,
        correo VARCHAR(50) NOT NULL,
        telefono VARCHAR(15),
        calificacion DECIMAL(2,1) NOT NULL DEFAULT 0.0,
        activo BIT NOT NULL DEFAULT 1,

        CONSTRAINT FK_CLIENTE_CUENTA_USUARIO
            FOREIGN KEY (idCuentaUsuario) REFERENCES dbo.CUENTA_USUARIO(id)
    );
END;
GO

IF OBJECT_ID('dbo.PROPIETARIO', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.PROPIETARIO (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCuentaUsuario INT NULL,
        nombres VARCHAR(150) NOT NULL,
        correo VARCHAR(50) NOT NULL,
        telefono VARCHAR(15),
        calificacion DECIMAL(2,1) NOT NULL DEFAULT 0.0,
        ruc CHAR(11) NOT NULL UNIQUE,
        saldo DECIMAL(10,2) NOT NULL DEFAULT 0.0,
        activo BIT NOT NULL DEFAULT 1,

        CONSTRAINT FK_PROPIETARIO_CUENTA_USUARIO
            FOREIGN KEY (idCuentaUsuario) REFERENCES dbo.CUENTA_USUARIO(id)
    );
END;
GO

IF OBJECT_ID('dbo.ADMINISTRADOR', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.ADMINISTRADOR (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCuentaUsuario INT NULL,
        nombres VARCHAR(150) NOT NULL,
        correo VARCHAR(50) NOT NULL,
        telefono VARCHAR(15),
        activo BIT NOT NULL DEFAULT 1,

        CONSTRAINT FK_ADMINISTRADOR_CUENTA_USUARIO
            FOREIGN KEY (idCuentaUsuario) REFERENCES dbo.CUENTA_USUARIO(id)
    );
END;
GO

IF OBJECT_ID('dbo.CANCHA', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CANCHA (
        id INT IDENTITY(1,1) PRIMARY KEY,
        nombre VARCHAR(150) NOT NULL,
        descripcion VARCHAR(MAX) NULL,
        direccion VARCHAR(255) NOT NULL,
        imagenUrl VARCHAR(MAX) NULL,
        idPropietario INT NOT NULL,
        precioBase DECIMAL(10,2) NOT NULL,
        promedioCalificacion DECIMAL(3,2) NOT NULL DEFAULT 0.00,
        activo BIT NOT NULL DEFAULT 1,

        CONSTRAINT FK_CANCHA_PROPIETARIO
            FOREIGN KEY (idPropietario) REFERENCES dbo.PROPIETARIO(id)
    );
END;
GO

IF OBJECT_ID('dbo.CANCHA_DEPORTE', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CANCHA_DEPORTE (
        idCancha INT NOT NULL,
        deporte VARCHAR(50) NOT NULL,

        CONSTRAINT PK_CANCHA_DEPORTE
            PRIMARY KEY (idCancha, deporte),

        CONSTRAINT FK_CANCHA_DEPORTE_CANCHA
            FOREIGN KEY (idCancha) REFERENCES dbo.CANCHA(id)
            ON DELETE CASCADE
    );
END;
GO

IF OBJECT_ID('dbo.CANCHA_ETIQUETA', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CANCHA_ETIQUETA (
        idCancha INT NOT NULL,
        etiqueta VARCHAR(50) NOT NULL,

        CONSTRAINT PK_CANCHA_ETIQUETA
            PRIMARY KEY (idCancha, etiqueta),

        CONSTRAINT FK_CANCHA_ETIQUETA_CANCHA
            FOREIGN KEY (idCancha) REFERENCES dbo.CANCHA(id)
            ON DELETE CASCADE
    );
END;
GO

IF OBJECT_ID('dbo.BLOQUE_HORARIO', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.BLOQUE_HORARIO (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCancha INT NOT NULL,
        dia INT NOT NULL,
        horaInicio TIME NOT NULL,
        horaFin TIME NOT NULL,
        precio DECIMAL(10,2) NOT NULL,
        estado VARCHAR(50) NOT NULL,
        activo BIT NOT NULL DEFAULT 1,

        CONSTRAINT FK_BLOQUE_HORARIO_CANCHA
            FOREIGN KEY (idCancha) REFERENCES dbo.CANCHA(id)
    );
END;
GO

IF OBJECT_ID('dbo.RESERVA', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.RESERVA (
        id INT IDENTITY(1,1) PRIMARY KEY,
        idCliente INT NOT NULL,
        idCancha INT NOT NULL,
        idPago INT NULL,
        estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE_PAGO',
        fechaCreacion DATETIME2 NOT NULL,
        activo BIT NOT NULL DEFAULT 1,

        CONSTRAINT FK_RESERVA_CLIENTE
            FOREIGN KEY (idCliente) REFERENCES dbo.CLIENTE(id),

        CONSTRAINT FK_RESERVA_CANCHA
            FOREIGN KEY (idCancha) REFERENCES dbo.CANCHA(id)
    );
END;
GO

IF OBJECT_ID('dbo.COMPROBANTE', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.COMPROBANTE (
        id INT IDENTITY(1,1) PRIMARY KEY,
        idReserva INT NOT NULL UNIQUE,
        serie VARCHAR(20) NOT NULL,
        numero VARCHAR(20) NOT NULL,
        fechaEmision DATETIME2 NOT NULL,
        montoBloques DECIMAL(10,2) NOT NULL,
        comisionPlataforma DECIMAL(10,2) NOT NULL DEFAULT 5.00,
        valorVenta DECIMAL(10,2) NOT NULL,
        montoIgv DECIMAL(10,2) NOT NULL,

        CONSTRAINT FK_COMPROBANTE_RESERVA
            FOREIGN KEY (idReserva) REFERENCES dbo.RESERVA(id),

        CONSTRAINT UQ_COMPROBANTE_SERIENUMERO
            UNIQUE (serie, numero)
    );
END;
GO

IF OBJECT_ID('dbo.PAGO', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.PAGO (
        id INT IDENTITY(1,1) PRIMARY KEY,
        idReserva INT NOT NULL,
        idComprobante INT NULL,
        metodoPago VARCHAR(20),
        monto DECIMAL(10,2) NOT NULL,
        fechaPago DATETIME2 NOT NULL,

        CONSTRAINT FK_PAGO_RESERVA
            FOREIGN KEY (idReserva) REFERENCES dbo.RESERVA(id),

        CONSTRAINT FK_PAGO_COMPROBANTE
            FOREIGN KEY (idComprobante) REFERENCES dbo.COMPROBANTE(id)
    );
END;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.foreign_keys
    WHERE name = 'FK_RESERVA_PAGO'
)
BEGIN
    ALTER TABLE dbo.RESERVA
    ADD CONSTRAINT FK_RESERVA_PAGO
        FOREIGN KEY (idPago) REFERENCES dbo.PAGO(id);
END;
GO

IF OBJECT_ID('dbo.DETALLE_RESERVA', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.DETALLE_RESERVA (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idReserva INT NOT NULL,
        idBloqueHorario INT NOT NULL,
        precio_historico DECIMAL(10,2) NOT NULL,

        CONSTRAINT FK_DETALLE_RESERVA_RESERVA
            FOREIGN KEY (idReserva) REFERENCES dbo.RESERVA(id),

        CONSTRAINT FK_DETALLE_RESERVA_BLOQUE_HORARIO
            FOREIGN KEY (idBloqueHorario) REFERENCES dbo.BLOQUE_HORARIO(id)
    );
END;
GO

IF OBJECT_ID('dbo.RESENA', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.RESENA (
        id INT IDENTITY(1,1) PRIMARY KEY,
        idReserva INT NOT NULL UNIQUE,
        descripcion VARCHAR(120) NULL,
        calificacion DECIMAL(2,1) NOT NULL,
        fechaPublicacion DATETIME2 NOT NULL,

        CONSTRAINT FK_RESENA_RESERVA
            FOREIGN KEY (idReserva) REFERENCES dbo.RESERVA(id)
    );
END;
GO
