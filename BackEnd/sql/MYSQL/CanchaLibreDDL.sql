DROP DATABASE IF EXISTS CanchaLibre;
CREATE DATABASE CanchaLibre;
USE CanchaLibre;

-- ============================================================
-- CUENTA_USUARIO
-- Centraliza credenciales y rol de todos los usuarios (RF06)
-- ============================================================
CREATE TABLE CUENTA_USUARIO (
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    userName      VARCHAR(50) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,               -- guardar hash, nunca texto plano
    rol           ENUM('ADMINISTRADOR','PROPIETARIO','CLIENTE') NOT NULL,
    intentosFallidos INT NOT NULL DEFAULT 0,           -- RF06: bloqueo tras 3 intentos
    ultimaSesion  DATETIME NULL,                       -- RF06: límite de 1 minuto entre intentos
    fechaBloqueo  DATETIME NULL                        -- RF06: fecha en que se bloqueó la cuenta
);

-- ============================================================
-- ADMINISTRADOR (TPC: hereda Persona + Registro)
-- ============================================================
CREATE TABLE ADMINISTRADOR (
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    nombres       VARCHAR(150) NOT NULL,
    correo        VARCHAR(100) NOT NULL UNIQUE,
    telefono      VARCHAR(20),
    idCuenta      INT NOT NULL UNIQUE,
    CONSTRAINT FK_Administrador_Cuenta FOREIGN KEY (idCuenta)
        REFERENCES CUENTA_USUARIO(id)
);

-- ============================================================
-- PROPIETARIO (TPC: hereda Persona + Registro)
-- ============================================================
CREATE TABLE PROPIETARIO (
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    nombres       VARCHAR(150) NOT NULL,
    correo        VARCHAR(100) NOT NULL UNIQUE,
    telefono      VARCHAR(20),
    calificacion  INT NOT NULL DEFAULT 0,
    idCuenta      INT NOT NULL UNIQUE,
    CONSTRAINT FK_Propietario_Cuenta FOREIGN KEY (idCuenta)
        REFERENCES CUENTA_USUARIO(id)
);

-- ============================================================
-- CLIENTE (TPC: hereda Persona + Registro)
-- ============================================================
CREATE TABLE CLIENTE (
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo        BOOLEAN NOT NULL DEFAULT TRUE,
    nombres       VARCHAR(150) NOT NULL,
    correo        VARCHAR(100) NOT NULL UNIQUE,        -- RF01: correo único
    telefono      VARCHAR(20),
    calificacion  INT NOT NULL DEFAULT 0,
    idCuenta      INT NOT NULL UNIQUE,
    CONSTRAINT FK_Cliente_Cuenta FOREIGN KEY (idCuenta)
        REFERENCES CUENTA_USUARIO(id)
);

-- ============================================================
-- CANCHA (RF02, RF04)
-- ============================================================
CREATE TABLE CANCHA (
    id                   INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo               BOOLEAN NOT NULL DEFAULT TRUE,
    nombre               VARCHAR(150) NOT NULL,
    descripcion          TEXT,
    direccion            VARCHAR(255),
    imagenUrl            VARCHAR(255),
    precioBase           DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    promedioCalificacion DECIMAL(3,2) NOT NULL DEFAULT 0.00, -- se recalcula al insertar/modificar reseña
    idPropietario        INT NOT NULL,
    CONSTRAINT FK_Cancha_Propietario FOREIGN KEY (idPropietario)
        REFERENCES PROPIETARIO(id)
);

-- ============================================================
-- CANCHA_DEPORTES: relación many-to-many Cancha <-> Deporte (RF04)
-- ============================================================
CREATE TABLE CANCHA_DEPORTES (
    idCancha    INT NOT NULL,
    deporte     ENUM('FUTBOL','BASQUET','VOLEY','TENIS') NOT NULL,
    PRIMARY KEY (idCancha, deporte),
    CONSTRAINT FK_CanchaDeportes_Cancha FOREIGN KEY (idCancha)
        REFERENCES CANCHA(id) ON DELETE CASCADE
);

-- ============================================================
-- CANCHA_ETIQUETAS: relación many-to-many Cancha <-> Etiqueta (RF02)
-- ============================================================
CREATE TABLE CANCHA_ETIQUETAS (
    idCancha    INT NOT NULL,
    etiqueta    ENUM('ILUMINACION','PARKING','WIFI','VESTIDORES','DUCHAS','BANOS') NOT NULL,
    PRIMARY KEY (idCancha, etiqueta),
    CONSTRAINT FK_CanchaEtiquetas_Cancha FOREIGN KEY (idCancha)
        REFERENCES CANCHA(id) ON DELETE CASCADE
);

-- ============================================================
-- BLOQUE_HORARIO (RF02, RF05)
-- Representa la plantilla semanal de disponibilidad de una cancha.
-- precio NULL indica que el propietario aun no configuro precio
-- para ese bloque (distinto a precio = 0.00)
-- ============================================================
CREATE TABLE BLOQUE_HORARIO (
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,
    dia         ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
    horaInicio  TIME NOT NULL,
    horaFin     TIME NOT NULL,
    precio      DECIMAL(10,2) NULL,                    -- NULL = sin precio configurado
    estado      ENUM('DISPONIBLE','RESERVADO','BLOQUEADO','MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
    idCancha    INT NOT NULL,
    CONSTRAINT FK_BloqueHorario_Cancha FOREIGN KEY (idCancha)
        REFERENCES CANCHA(id) ON DELETE CASCADE
);

-- ============================================================
-- RESERVA (RF03, RF09, RF10)
-- fechaHoraInicio y fechaHoraFin definen el rango reservado.
-- El precio se calcula buscando los bloques de la cancha
-- que caen en ese rango.
-- COMPLETADA se agrega para cubrir RF09 (historial con estado completada)
-- ============================================================
CREATE TABLE RESERVA (
    id              INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    estado          ENUM('PENDIENTE_PAGO','CONFIRMADA','CANCELADA','RECHAZADA','COMPLETADA') NOT NULL DEFAULT 'PENDIENTE_PAGO',
    fechaHoraInicio DATETIME NOT NULL,
    fechaHoraFin    DATETIME NOT NULL,
    idCliente       INT NOT NULL,
    idCancha        INT NOT NULL,
    CONSTRAINT FK_Reserva_Cliente FOREIGN KEY (idCliente)
        REFERENCES CLIENTE(id),
    CONSTRAINT FK_Reserva_Cancha FOREIGN KEY (idCancha)
        REFERENCES CANCHA(id)
);

-- ============================================================
-- PAGO (RF11)
-- Una reserva tiene un solo pago (UNIQUE en idReserva)
-- ============================================================
CREATE TABLE PAGO (
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,
    metodoPago  ENUM('YAPE','PLIN','EFECTIVO') NOT NULL,
    monto       DECIMAL(10,2) NOT NULL,
    fechaPago   DATETIME NOT NULL,
    idReserva   INT NOT NULL UNIQUE,
    CONSTRAINT FK_Pago_Reserva FOREIGN KEY (idReserva)
        REFERENCES RESERVA(id)
);

-- ============================================================
-- COMPROBANTE (RF14)
-- comisionPlataforma (5.00) e igv (0.18) son constantes del
-- modelo Java y no se persisten como columnas.
-- Solo se persisten los montos calculados:
--   montoBloques : suma de precios de bloques reservados
--   valorVenta   : montoBloques + comisionPlataforma
--   montoTotal   : valorVenta + (valorVenta * 0.18)
-- Una reserva genera un solo comprobante (UNIQUE en idReserva)
-- ============================================================
CREATE TABLE COMPROBANTE (
    id              INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    serie           VARCHAR(10) NOT NULL,
    numero          VARCHAR(20) NOT NULL,
    fechaEmision    DATETIME NOT NULL,
    montoBloques    DECIMAL(10,2) NOT NULL,
    valorVenta      DECIMAL(10,2) NOT NULL,
    montoTotal      DECIMAL(10,2) NOT NULL,
    idReserva       INT NOT NULL UNIQUE,
    CONSTRAINT FK_Comprobante_Reserva FOREIGN KEY (idReserva)
        REFERENCES RESERVA(id)
);

-- ============================================================
-- RESENA (RF08)
-- Se verifica en la capa de negocio que el cliente tenga
-- una reserva COMPLETADA para poder dejar resena.
-- Desde idReserva se deriva el cliente y la cancha por JOIN.
-- Una reserva genera como maximo una resena (UNIQUE en idReserva)
-- Al insertar o modificar una resena se debe recalcular
-- promedioCalificacion en CANCHA.
-- ============================================================
CREATE TABLE RESENA (
    id                  INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    activo              BOOLEAN NOT NULL DEFAULT TRUE,
    descripcion         VARCHAR(500),
    calificacion        INT NOT NULL,
    fechaPublicacion    DATETIME NOT NULL,
    idReserva           INT NOT NULL UNIQUE,
    CONSTRAINT FK_Resena_Reserva FOREIGN KEY (idReserva)
        REFERENCES RESERVA(id),
    CONSTRAINT CHK_Calificacion CHECK (calificacion BETWEEN 1 AND 5)
);

