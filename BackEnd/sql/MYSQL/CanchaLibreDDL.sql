USE CanchaLibre;

DROP TABLE IF EXISTS NotificacionComprobante;
DROP TABLE IF EXISTS NotificacionReserva;
DROP TABLE IF EXISTS NotificacionBloqueo;
DROP TABLE IF EXISTS Notificacion;
DROP TABLE IF EXISTS Resena;
DROP TABLE IF EXISTS Pago;
DROP TABLE IF EXISTS Comprobante;
DROP TABLE IF EXISTS Reserva;
DROP TABLE IF EXISTS Cancha_Etiqueta;
DROP TABLE IF EXISTS Cancha_Deporte;
DROP TABLE IF EXISTS BloqueHorario;
DROP TABLE IF EXISTS Cancha;
DROP TABLE IF EXISTS Administrador;
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS Propietario;
DROP TABLE IF EXISTS Persona;
DROP TABLE IF EXISTS CuentaUsuario;

CREATE TABLE CuentaUsuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN DEFAULT TRUE,
    userName VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'CLIENTE', 'PROPIETARIO') NOT NULL,
    intentosFallidos INT DEFAULT 0,
    ultimaSesion DATETIME NULL,
    fechaBloqueo DATETIME NULL
);

CREATE TABLE Persona (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN DEFAULT TRUE,
    nombres VARCHAR(150) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    idCuentaUsuario INT UNIQUE,
    CONSTRAINT FK_Persona_CuentaUsuario
        FOREIGN KEY (idCuentaUsuario) REFERENCES CuentaUsuario(id)
);

CREATE TABLE Cliente (
    id INT PRIMARY KEY,
    calificacion DECIMAL(3,2) DEFAULT 0,
    CONSTRAINT FK_Cliente_Persona
        FOREIGN KEY (id) REFERENCES Persona(id)
        ON DELETE CASCADE
);

CREATE TABLE Propietario (
    id INT PRIMARY KEY,
    CONSTRAINT FK_Propietario_Persona
        FOREIGN KEY (id) REFERENCES Persona(id)
        ON DELETE CASCADE
);

CREATE TABLE Administrador (
    id INT PRIMARY KEY,
    CONSTRAINT FK_Administrador_Persona
        FOREIGN KEY (id) REFERENCES Persona(id)
        ON DELETE CASCADE
);

CREATE TABLE Cancha (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN DEFAULT TRUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    direccion VARCHAR(255),
    imagenUrl VARCHAR(255),
    idPropietario INT NOT NULL,
    precioBase DECIMAL(10,2) NOT NULL,
    promedioCalificacion DECIMAL(3,2) DEFAULT 0,
    CONSTRAINT FK_Cancha_Propietario
        FOREIGN KEY (idPropietario) REFERENCES Propietario(id)
);

CREATE TABLE Cancha_Deporte (
    idCancha INT NOT NULL,
    deporte ENUM('FUTBOL', 'BASQUET', 'VOLEY', 'TENIS') NOT NULL,
    PRIMARY KEY (idCancha, deporte),
    CONSTRAINT FK_CanchaDeporte_Cancha
        FOREIGN KEY (idCancha) REFERENCES Cancha(id)
        ON DELETE CASCADE
);

CREATE TABLE Cancha_Etiqueta (
    idCancha INT NOT NULL,
    etiqueta ENUM('ILUMINACION', 'PARKING', 'WIFI', 'VESTIDORES', 'DUCHAS', 'BANOS') NOT NULL,
    PRIMARY KEY (idCancha, etiqueta),
    CONSTRAINT FK_CanchaEtiqueta_Cancha
        FOREIGN KEY (idCancha) REFERENCES Cancha(id)
        ON DELETE CASCADE
);

CREATE TABLE BloqueHorario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN DEFAULT TRUE,
    dia ENUM('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado ENUM('DISPONIBLE', 'RESERVADO', 'BLOQUEADO') NOT NULL,
    idCancha INT NOT NULL,
    CONSTRAINT FK_BloqueHorario_Cancha
        FOREIGN KEY (idCancha) REFERENCES Cancha(id)
        ON DELETE CASCADE
);

CREATE TABLE Reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN DEFAULT TRUE,
    fechaReserva DATETIME NOT NULL,
    estado ENUM('ESPERA', 'PAGADO', 'CANCELADO', 'COMPLETADO') NOT NULL,
    idCliente INT NOT NULL,
    idCancha INT NOT NULL,
    idBloqueHorario INT NOT NULL,
    CONSTRAINT FK_Reserva_Cliente
        FOREIGN KEY (idCliente) REFERENCES Cliente(id),
    CONSTRAINT FK_Reserva_Cancha
        FOREIGN KEY (idCancha) REFERENCES Cancha(id),
    CONSTRAINT FK_Reserva_BloqueHorario
        FOREIGN KEY (idBloqueHorario) REFERENCES BloqueHorario(id)
);

CREATE TABLE Comprobante (
    idComprobante INT AUTO_INCREMENT PRIMARY KEY,
    serie VARCHAR(20) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    fechaEmision DATETIME NOT NULL,
    idReserva INT UNIQUE,
    CONSTRAINT FK_Comprobante_Reserva
        FOREIGN KEY (idReserva) REFERENCES Reserva(id),
    CONSTRAINT UQ_Comprobante_SerieNumero
        UNIQUE (serie, numero)
);

CREATE TABLE Pago (
    idPago INT AUTO_INCREMENT PRIMARY KEY,
    metodoPago ENUM('YAPE', 'PLIN') NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fechaPago DATETIME NOT NULL,
    idComprobante INT UNIQUE,
    CONSTRAINT FK_Pago_Comprobante
        FOREIGN KEY (idComprobante) REFERENCES Comprobante(idComprobante)
);

CREATE TABLE Resena (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN DEFAULT TRUE,
    calificacion INT NOT NULL,
    descripcion VARCHAR(120),
    fechaPublicacion DATETIME NOT NULL,
    idCancha INT NOT NULL,
    idCliente INT NOT NULL,
    CONSTRAINT FK_Resena_Cancha
        FOREIGN KEY (idCancha) REFERENCES Cancha(id),
    CONSTRAINT FK_Resena_Cliente
        FOREIGN KEY (idCliente) REFERENCES Cliente(id)
);

CREATE TABLE Notificacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fechaEnvio DATETIME NOT NULL,
    idDestinatario INT NOT NULL,
    CONSTRAINT FK_Notificacion_Destinatario
        FOREIGN KEY (idDestinatario) REFERENCES Persona(id)
);

CREATE TABLE NotificacionBloqueo (
    idNotificacion INT PRIMARY KEY,
    idCuentaUsuario INT NOT NULL,
    CONSTRAINT FK_NotificacionBloqueo_Notificacion
        FOREIGN KEY (idNotificacion) REFERENCES Notificacion(id)
        ON DELETE CASCADE,
    CONSTRAINT FK_NotificacionBloqueo_CuentaUsuario
        FOREIGN KEY (idCuentaUsuario) REFERENCES CuentaUsuario(id)
);

CREATE TABLE NotificacionReserva (
    idNotificacion INT PRIMARY KEY,
    idReserva INT NOT NULL,
    CONSTRAINT FK_NotificacionReserva_Notificacion
        FOREIGN KEY (idNotificacion) REFERENCES Notificacion(id)
        ON DELETE CASCADE,
    CONSTRAINT FK_NotificacionReserva_Reserva
        FOREIGN KEY (idReserva) REFERENCES Reserva(id)
);

CREATE TABLE NotificacionComprobante (
    idNotificacion INT PRIMARY KEY,
    idComprobante INT NOT NULL,
    CONSTRAINT FK_NotificacionComprobante_Notificacion
        FOREIGN KEY (idNotificacion) REFERENCES Notificacion(id)
        ON DELETE CASCADE,
    CONSTRAINT FK_NotificacionComprobante_Comprobante
        FOREIGN KEY (idComprobante) REFERENCES Comprobante(idComprobante)
);
