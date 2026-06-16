USE canchaLibre;
GO

DELETE FROM COMPROBANTE;
DELETE FROM PAGO;
DELETE FROM RESENA;
DELETE FROM RESERVA;
DELETE FROM CANCHA;
DELETE FROM ADMINISTRADOR;
DELETE FROM PROPIETARIO;
DELETE FROM CLIENTE;
DELETE FROM CUENTA_USUARIO;
GO

DBCC CHECKIDENT ('COMPROBANTE', RESEED, 0);
DBCC CHECKIDENT ('PAGO', RESEED, 0);
DBCC CHECKIDENT ('RESENA', RESEED, 0);
DBCC CHECKIDENT ('RESERVA', RESEED, 0);
DBCC CHECKIDENT ('CANCHA', RESEED, 0);
DBCC CHECKIDENT ('ADMINISTRADOR', RESEED, 0);
DBCC CHECKIDENT ('PROPIETARIO', RESEED, 0);
DBCC CHECKIDENT ('CLIENTE', RESEED, 0);
DBCC CHECKIDENT ('CUENTA_USUARIO', RESEED, 0);
GO

USE canchaLibre;
GO

-- ============================================================================
-- 1. CUENTA_USUARIO (Centralized Security & Credentials)
-- ============================================================================
SET IDENTITY_INSERT CUENTA_USUARIO ON;
INSERT INTO CUENTA_USUARIO (id, correo, contrasena, intentosFallidos, ultimaSesion, activo) VALUES
(1, 'maria.garcia@test.com',       'clave123',      0, '2026-06-15 10:00:00', 1),
(2, 'juan.perez@test.com',         'userpass456',   1, '2026-06-16 11:30:00', 1),
(3, 'roberto.canchas@negocio.com', 'adminPass2026', 0, '2026-06-15 08:00:00', 1),
(4, 'carlos.c@complejo.com',       'canchas123',    0, '2026-06-16 09:15:00', 1),
(5, 'admin@canchalibre.com',       'rootPass2026',  0, '2026-06-16 17:00:00', 1);
SET IDENTITY_INSERT CUENTA_USUARIO OFF;
GO

-- ============================================================================
-- 2. PROFILE TABLES (References CUENTA_USUARIO)
-- ============================================================================

-- CLIENTE
SET IDENTITY_INSERT CLIENTE ON;
INSERT INTO CLIENTE (id, idCuentaUsuario, nombres, telefono, calificacion, activo) VALUES
(1, 1, 'Maria Garcia', '999888777', 5, 1),
(2, 2, 'Juan Perez',   '988777666', 4, 1);
SET IDENTITY_INSERT CLIENTE OFF;
GO

-- PROPIETARIO
SET IDENTITY_INSERT PROPIETARIO ON;
INSERT INTO PROPIETARIO (id, idCuentaUsuario, nombres, telefono, calificacion, activo) VALUES
(1, 3, 'Roberto Tueño',   '987654321', 5, 1),
(2, 4, 'Carlos Canchas',  '912345678', 4, 1);
SET IDENTITY_INSERT PROPIETARIO OFF;
GO

-- ADMINISTRADOR
SET IDENTITY_INSERT ADMINISTRADOR ON;
INSERT INTO ADMINISTRADOR (id, idCuentaUsuario, nombres, telefono, activo) VALUES
(1, 5, 'Super Admin', '900000001', 1);
SET IDENTITY_INSERT ADMINISTRADOR OFF;
GO

-- ============================================================================
-- 3. INFRASTRUCTURE & REVIEWS LAYER
-- ============================================================================

-- CANCHA (References PROPIETARIO)
SET IDENTITY_INSERT CANCHA ON;
INSERT INTO CANCHA (id, idPropietario, nombre, descripcion, deportes, imagenUrl, disponible, direccion, activo) VALUES
(1, 1, 'Estadio Central P10',   'Césped sintético con iluminación nocturna profesional.', 'FUTBOL', 'https://images.com/c1.jpg', 1, 'Av. Deporte 123, Lima', 1),
(2, 1, 'Cancha Tenis Alborada', 'Arcilla batida de primera calidad.',                    'TENIS',  'https://images.com/c2.jpg', 1, 'Av. El Sol 456, Surco',   1),
(3, 2, 'Complejo El Golazo',    'Cancha de grass natural de futbol 7x7.',                'FUTBOL', 'https://images.com/c3.jpg', 0, 'Jr. Union 789, San Miguel', 1);
SET IDENTITY_INSERT CANCHA OFF;
GO

-- RESENA (References CANCHA and CLIENTE)
SET IDENTITY_INSERT RESENA ON;
INSERT INTO RESENA (id, idCancha, idCliente, estrellas, comentario, fechaResena, activo) VALUES
(1, 1, 1, 5, 'Excelente iluminación nocturna y campo muy bien cuidado.', '2026-06-15 22:00:00', 1),
(2, 1, 2, 4, 'El terreno está perfecto, pero el estacionamiento es chico.', '2026-06-16 14:00:00', 1);
SET IDENTITY_INSERT RESENA OFF;
GO

-- ============================================================================
-- 4. CORE TRANSACTION LAYER
-- ============================================================================

-- RESERVA (References CLIENTE and CANCHA)
SET IDENTITY_INSERT RESERVA ON;
INSERT INTO RESERVA (id, idCliente, idCancha, fechaHora, duracion, estado, activo) VALUES
(1, 1, 1, '2026-06-20 19:00:00', '01:30:00', 'ESPERA',     1),
(2, 2, 2, '2026-06-21 08:00:00', '02:00:00', 'COMPLETADO', 1);
SET IDENTITY_INSERT RESERVA OFF;
GO

-- PAGO (References RESERVA)
SET IDENTITY_INSERT PAGO ON;
INSERT INTO PAGO (id, idReserva, metodoPago, monto, fechaPago, activo) VALUES
(1, 1, 'EFECTIVO', 90.00,  '2026-06-16 12:00:00', 1),
(2, 2, 'TARJETA',  120.00, '2026-06-16 12:30:00', 1);
SET IDENTITY_INSERT PAGO OFF;
GO

-- COMPROBANTE (References RESERVA)
SET IDENTITY_INSERT COMPROBANTE ON;
INSERT INTO COMPROBANTE (id, idReserva, igv, fechaEmision, total, activo) VALUES
(1, 1, 0.18, '2026-06-16 12:01:00', 106.20, 1),
(2, 2, 0.18, '2026-06-16 12:31:00', 141.60, 1);
SET IDENTITY_INSERT COMPROBANTE OFF;
GO