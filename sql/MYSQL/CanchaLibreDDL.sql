USE CanchaLibre;

DROP TABLE IF EXISTS Administrador;
DROP TABLE IF EXISTS Resena;
DROP TABLE IF EXISTS Pago;
DROP TABLE IF EXISTS Comprobante;
DROP TABLE IF EXISTS Reserva;
DROP TABLE IF EXISTS cancha_deportes;
DROP TABLE IF EXISTS Cancha;
DROP TABLE IF EXISTS Propietario;
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS EsquemaPrecio;

CREATE TABLE EsquemaPrecio (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    precioHora DECIMAL(10, 2) NOT NULL,
    conIluminacion BOOLEAN,
    temporada enum('ALTA','MEDIA','BAJA')
);

CREATE TABLE Administrador (
    idAdministrador INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombres VARCHAR(150),
    contrasena VARCHAR(255),
    correo VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    intentosFallidos INT DEFAULT 0,
    ultimaSesion DATETIME
);

CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombres VARCHAR(150),
    contrasena VARCHAR(255),
    correo VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    intentosFallidos INT DEFAULT 0,
    ultimaSesion DATETIME NULL,
    calificacion INT DEFAULT 0
);

CREATE TABLE Propietario (
    idPropietario INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombres VARCHAR(150),
    contrasena VARCHAR(255),
    correo VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    intentosFallidos INT DEFAULT 0,
    ultimaSesion DATETIME NULL,
    calificacion INT
);

CREATE TABLE Cancha (
    idCancha INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombre VARCHAR(150),
    descripcion TEXT,
    imagenUrl VARCHAR(255),
    disponible BOOLEAN,
    #deportes enum('FUTBOL','BASQUET','VOLEY','TENIS'),
    direccion VARCHAR(255),
    idPropietario INT,
    CONSTRAINT FK_Cancha_Propietario FOREIGN KEY (idPropietario) REFERENCES Propietario(idPropietario)
);
CREATE TABLE cancha_deportes (
    idCancha INT NOT NULL,
    deporte ENUM('FUTBOL', 'BASQUET', 'VOLEY', 'TENIS') NOT NULL,
    PRIMARY KEY (idCancha, deporte), -- Evita deportes duplicados en la misma cancha
    CONSTRAINT FK_Deportes_Cancha FOREIGN KEY (idCancha) REFERENCES Cancha(idCancha) ON DELETE CASCADE
);
CREATE TABLE Reserva (
    idReserva INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fechaHora DATETIME,
    duracion TIME,
    estado enum('ESPERA','PAGADO','CANCELADO','COMPLETADO'), 
    idCancha INT,
    idCliente INT,
    CONSTRAINT FK_Reserva_Cancha FOREIGN KEY (idCancha) REFERENCES Cancha(idCancha),
    CONSTRAINT FK_Reserva_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE Pago (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    metodoPago enum('YAPE','PLIN','EFECTIVO'), 
    monto DECIMAL(10, 2),
    fechaPago DATETIME,
    idReserva INT,
    CONSTRAINT FK_Pago_Reserva FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva)
);

CREATE TABLE Comprobante (
    idComprobante INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    tipoReserva INT,
    igv DECIMAL (10,2) DEFAULT 0.18,
    monto DECIMAL(10, 2),
    fechaEmision DATETIME,
    idReserva INT UNIQUE,
    CONSTRAINT FK_Comprobante_Reserva FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva)
);

CREATE TABLE Resena (
    idResena INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    calificacion INT,
    descripcion VARCHAR(120),
    fechaPublicacion DATETIME, 
    idCancha INT,
    idCliente INT,
    CONSTRAINT FK_Resena_Cancha FOREIGN KEY (idCancha) REFERENCES Cancha(idCancha),
    CONSTRAINT FK_Resena_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);
