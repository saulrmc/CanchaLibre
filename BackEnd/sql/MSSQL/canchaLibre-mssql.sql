USE CanchaLibre;
GO


IF OBJECT_ID('CUENTA_USUARIO', 'U') IS NULL
BEGIN
    CREATE TABLE CUENTA_USUARIO(
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        userName VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(50) NOT NULL,
        rol VARCHAR(15) NOT NULL,
        intentosFallidos INT NOT NULL DEFAULT 0,
        ultimaSesion DATETIME NULL,
        fechaBloqueo DATETIME NULL,
        activo BIT NOT NULL DEFAULT 0
    );
END;

IF OBJECT_ID('CLIENTE', 'U') IS NULL
BEGIN
    CREATE TABLE CLIENTE(
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCuentaUsuario INT NULL,
        nombres VARCHAR(150) NOT NULL,
        correo VARCHAR(50) NOT NULL,
        telefono VARCHAR(15),
        calificacion DECIMAL(2,1) NOT NULL DEFAULT 0.0,
        activo BIT NOT NULL DEFAULT 1
    );
END;

IF OBJECT_ID('PROPIETARIO', 'U') IS NULL
BEGIN
    CREATE TABLE PROPIETARIO(
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCuentaUsuario INT NULL,
        nombres VARCHAR(150) NOT NULL,
        correo VARCHAR(50) NOT NULL,
        telefono VARCHAR(15),
        calificacion DECIMAL(2,1) NOT NULL DEFAULT 0.0,
        ruc CHAR(11) NOT NULL UNIQUE,
        saldo DECIMAL(10,2) NOT NULL DEFAULT 0.0,
        activo BIT NOT NULL DEFAULT 1
    );
END;

IF OBJECT_ID('ADMINISTRADOR', 'U') IS NULL
BEGIN
    CREATE TABLE ADMINISTRADOR(
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCuentaUsuario INT NULL,
        nombres VARCHAR(150) NOT NULL,
        correo VARCHAR(50) NOT NULL,
        telefono VARCHAR(15),
        activo BIT NOT NULL DEFAULT 1
    );
END;

IF OBJECT_ID('CANCHA', 'U') IS NULL
BEGIN
    CREATE TABLE CANCHA (
        id INT IDENTITY(1,1) PRIMARY KEY,
        nombre VARCHAR(150) NOT NULL,
        descripcion VARCHAR(MAX) NULL,
        direccion VARCHAR(255) NOT NULL,
        imagenUrl VARCHAR(MAX) NULL,
        idPropietario INT NOT NULL,
        precioBase DECIMAL(10,2) NOT NULL,
        promedioCalificacion DECIMAL(3,2) NOT NULL DEFAULT 0.00,
        activo BIT NOT NULL DEFAULT 1
    );
END;

IF OBJECT_ID('CANCHA_DEPORTE', 'U') IS NULL
BEGIN
    CREATE TABLE CANCHA_DEPORTE (
        idCancha INT NOT NULL,
        deporte VARCHAR(50) NOT NULL,
        PRIMARY KEY (idCancha, deporte)
    );
END;

IF OBJECT_ID('CANCHA_ETIQUETA', 'U') IS NULL
BEGIN
    CREATE TABLE CANCHA_ETIQUETA (
         idCancha INT NOT NULL,
         etiqueta VARCHAR(50) NOT NULL,
         PRIMARY KEY (idCancha, etiqueta)
    );
END;

IF OBJECT_ID('BLOQUE_HORARIO', 'U') IS NULL
BEGIN
    CREATE TABLE BLOQUE_HORARIO(
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idCancha INT NOT NULL,
        dia INT NOT NULL,
        horaInicio TIME NOT NULL,
        horaFin TIME NOT NULL,
        precio DECIMAL(10,2) NOT NULL,
        estado VARCHAR(50) NOT NULL,
        activo BIT NOT NULL DEFAULT 1
    );
END;

IF OBJECT_ID('RESERVA', 'U') IS NULL
BEGIN
    CREATE TABLE RESERVA(
        id INT IDENTITY(1,1) PRIMARY KEY,
        idCliente INT NOT NULL,
        idCancha INT NOT NULL,
        idPago INT NULL, -- Se relacionará por ALTER TABLE más abajo
        estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE_PAGO',
        fechaCreacion DATETIME NOT NULL,
        activo BIT NOT NULL DEFAULT 1
    );
END;

IF OBJECT_ID('PAGO', 'U') IS NULL
BEGIN
    CREATE TABLE PAGO(
       id INT IDENTITY(1,1) PRIMARY KEY,
       idReserva INT NOT NULL,
       idComprobante INT NULL,
       metodoPago VARCHAR(20),
       monto DECIMAL(10,2) NOT NULL,
       fechaPago DATETIME NOT NULL
    );
END;

IF OBJECT_ID('COMPROBANTE', 'U') IS NULL
BEGIN
    CREATE TABLE COMPROBANTE (
       id INT IDENTITY(1,1) PRIMARY KEY,
       idReserva INT NOT NULL UNIQUE,
       serie VARCHAR(20) NOT NULL,
       numero VARCHAR(20) NOT NULL,
       fechaEmision DATETIME NOT NULL,
       montoBloques DECIMAL(10,2) NOT NULL,
       comisionPlataforma DECIMAL(10,2) NOT NULL DEFAULT 5.00,
       valorVenta DECIMAL(10,2) NOT NULL,
       montoIgv DECIMAL(10,2) NOT NULL,
       CONSTRAINT UQ_COMPROBANTE_SERIENUMERO UNIQUE (serie, numero)
    );
END;

IF OBJECT_ID('DETALLE_RESERVA', 'U') IS NULL
BEGIN
    CREATE TABLE DETALLE_RESERVA (
        id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
        idReserva INT NOT NULL,
        idBloqueHorario INT NOT NULL,
        precio_historico DECIMAL(10,2) NOT NULL
    );
END;

IF OBJECT_ID('RESENA', 'U') IS NULL
BEGIN
    CREATE TABLE RESENA (
        id INT IDENTITY(1,1) PRIMARY KEY,
        idReserva INT NOT NULL UNIQUE,
        descripcion VARCHAR(120) NULL,
        calificacion DECIMAL(2,1) NOT NULL,
        fechaPublicacion DATETIME NOT NULL
    );
END;

GO

-- ==========================================
-- 2. ASIGNACIÓN DE LLAVES FORÁNEAS (CONSTRAINTS)
-- ==========================================

ALTER TABLE CLIENTE ADD CONSTRAINT FK_CLIENTE_CUENTA_USUARIO 
    FOREIGN KEY (idCuentaUsuario) REFERENCES CUENTA_USUARIO(id);

ALTER TABLE PROPIETARIO ADD CONSTRAINT FK_PROPIETARIO_CUENTA_USUARIO 
    FOREIGN KEY (idCuentaUsuario) REFERENCES CUENTA_USUARIO(id);

ALTER TABLE ADMINISTRADOR ADD CONSTRAINT FK_ADMINISTRADOR_CUENTA_USUARIO 
    FOREIGN KEY (idCuentaUsuario) REFERENCES CUENTA_USUARIO(id);

ALTER TABLE CANCHA ADD CONSTRAINT FK_CANCHA_PROPIETARIO 
    FOREIGN KEY (idPropietario) REFERENCES PROPIETARIO(id);

ALTER TABLE CANCHA_DEPORTE ADD CONSTRAINT FK_CANCHA_DEPORTE_CANCHA 
    FOREIGN KEY (idCancha) REFERENCES CANCHA(id) ON DELETE CASCADE;

ALTER TABLE CANCHA_ETIQUETA ADD CONSTRAINT FK_CANCHA_ETIQUETA_CANCHA 
    FOREIGN KEY (idCancha) REFERENCES CANCHA(id) ON DELETE CASCADE;

ALTER TABLE BLOQUE_HORARIO ADD CONSTRAINT FK_BLOQUE_HORARIO_CANCHA 
    FOREIGN KEY (idCancha) REFERENCES CANCHA(id);

ALTER TABLE RESERVA ADD CONSTRAINT FK_RESERVA_CLIENTE 
    FOREIGN KEY (idCliente) REFERENCES CLIENTE(id);

ALTER TABLE RESERVA ADD CONSTRAINT FK_RESERVA_CANCHA 
    FOREIGN KEY (idCancha) REFERENCES CANCHA(id);

ALTER TABLE RESERVA ADD CONSTRAINT FK_RESERVA_PAGO 
    FOREIGN KEY (idPago) REFERENCES PAGO(id);

ALTER TABLE COMPROBANTE ADD CONSTRAINT FK_COMPROBANTE_RESERVA 
    FOREIGN KEY (idReserva) REFERENCES RESERVA(id);

ALTER TABLE PAGO ADD CONSTRAINT FK_PAGO_RESERVA 
    FOREIGN KEY (idReserva) REFERENCES RESERVA(id);

ALTER TABLE PAGO ADD CONSTRAINT FK_PAGO_COMPROBANTE 
    FOREIGN KEY (idComprobante) REFERENCES COMPROBANTE(id);

ALTER TABLE DETALLE_RESERVA ADD CONSTRAINT FK_DETALLE_RESERVA_RESERVA 
    FOREIGN KEY (idReserva) REFERENCES RESERVA(id);

ALTER TABLE DETALLE_RESERVA ADD CONSTRAINT FK_DETALLE_RESERVA_BLOQUE_HORARIO 
    FOREIGN KEY (idBloqueHorario) REFERENCES BLOQUE_HORARIO(id);

ALTER TABLE RESENA ADD CONSTRAINT FK_RESENA_RESERVA 
    FOREIGN KEY (idReserva) REFERENCES RESERVA(id);

GO

-- ==========================================
-- 3. TABLAS COMENTADAS (Traducción de respaldo)
-- ==========================================
/*
IF OBJECT_ID('Notificacion', 'U') IS NULL
BEGIN
    CREATE TABLE Notificacion (
        id INT IDENTITY(1,1) PRIMARY KEY,
        fechaEnvio DATETIME NOT NULL,
        idDestinatario INT NOT NULL,
        -- Nota: Asegúrate de tener la tabla Persona creada si descomentas esto
        -- CONSTRAINT FK_Notificacion_Destinatario FOREIGN KEY (idDestinatario) REFERENCES Persona(id)
    );
END;

IF OBJECT_ID('NotificacionBloqueo', 'U') IS NULL
BEGIN
    CREATE TABLE NotificacionBloqueo (
        idNotificacion INT PRIMARY KEY,
        idCuentaUsuario INT NOT NULL,
        CONSTRAINT FK_NotificacionBloqueo_Notificacion FOREIGN KEY (idNotificacion) REFERENCES Notificacion(id) ON DELETE CASCADE,
        CONSTRAINT FK_NotificacionBloqueo_CuentaUsuario FOREIGN KEY (idCuentaUsuario) REFERENCES CUENTA_USUARIO(id)
    );
END;

IF OBJECT_ID('NotificacionReserva', 'U') IS NULL
BEGIN
    CREATE TABLE NotificacionReserva (
        idNotificacion INT PRIMARY KEY,
        idReserva INT NOT NULL,
        CONSTRAINT FK_NotificacionReserva_Notificacion FOREIGN KEY (idNotificacion) REFERENCES Notificacion(id) ON DELETE CASCADE,
        CONSTRAINT FK_NotificacionReserva_Reserva FOREIGN KEY (idReserva) REFERENCES RESERVA(id)
    );
END;

IF OBJECT_ID('NotificacionComprobante', 'U') IS NULL
BEGIN
    CREATE TABLE NotificacionComprobante (
        idNotificacion INT PRIMARY KEY,
        idComprobante INT NOT NULL,
        CONSTRAINT FK_NotificacionComprobante_Notificacion FOREIGN KEY (idNotificacion) REFERENCES Notificacion(id) ON DELETE CASCADE,
        CONSTRAINT FK_NotificacionComprobante_Comprobante FOREIGN KEY (idComprobante) REFERENCES COMPROBANTE(id)
    );
END;
*/
