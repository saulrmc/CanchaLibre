USE CanchaLibre;

CREATE TABLE EsquemaPrecio (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    precioPorHora DECIMAL(10, 2) NOT NULL,
    conIluminacion BOOLEAN,
    temporada INT
);

CREATE TABLE Administrador (
    idAdministrador INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombres VARCHAR(150),
    contrasena VARCHAR(255),
    correo VARCHAR(100) UNIQUE,
    telefono VARCHAR(20)
);

CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombres VARCHAR(150),
    contrasena VARCHAR(255),
    correo VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    intentosFallidos INT DEFAULT 0,
    ultimaSesion DATETIME NULL,
    rol VARCHAR(20) DEFAULT 'CLIENTE',
    activo TINYINT DEFAULT 1,
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
    rol VARCHAR(20) DEFAULT 'PROPIETARIO',
    activo TINYINT DEFAULT 1,
    ruc VARCHAR(20) NOT NULL
);

CREATE TABLE Cancha (
    idCancha INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    nombre VARCHAR(150),
    descripcion TEXT,
    imageUrl VARCHAR(255),
    disponible BOOLEAN,
    direccion VARCHAR(255),
    deporte VARCHAR(50), -- Aquí se usa el Enum Deporte (FUTBOL, BASQUET, etc)
    idPropietario INT,
    idEsquemaPrecio INT,
    CONSTRAINT FK_Cancha_Propietario FOREIGN KEY (idPropietario) REFERENCES Propietario(idPropietario),
    CONSTRAINT FK_Cancha_Precio FOREIGN KEY (idEsquemaPrecio) REFERENCES EsquemaPrecio(id)
);

CREATE TABLE Reserva (
    idReserva INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fechaHora DATETIME,
    duracion INT,
    estado VARCHAR(50), -- Aquí se usa el Enum EstadoReserva
    idCancha INT,
    idCliente INT,
    CONSTRAINT FK_Reserva_Cancha FOREIGN KEY (idCancha) REFERENCES Cancha(idCancha),
    CONSTRAINT FK_Reserva_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE Notificacion (
    idNotificacion INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    descripcion TEXT,
    fechaEnvio DATETIME,
    idReceptor INT -- Se asocia al ID de quien recibe la notificación
);

CREATE TABLE Pago (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    metodoPago VARCHAR(50), -- Aquí se usa el Enum MetodoPago (YAPE, PLIN)
    monto DECIMAL(10, 2),
    fechaPago DATETIME,
    idReserva INT,
    CONSTRAINT FK_Pago_Reserva FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva)
);

CREATE TABLE Comprobante (
    idComprobante INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    tipoReserva INT,
    monto DECIMAL(10, 2),
    idReserva INT UNIQUE,
    CONSTRAINT FK_Comprobante_Reserva FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva)
);

CREATE TABLE Resena (
    idResena INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    calificacion INT,
    descripcion TEXT,
    fechaPublicacion TIME, -- Atributo literal del diagrama
    idCancha INT,
    idCliente INT,
    CONSTRAINT FK_Resena_Cancha FOREIGN KEY (idCancha) REFERENCES Cancha(idCancha),
    CONSTRAINT FK_Resena_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);