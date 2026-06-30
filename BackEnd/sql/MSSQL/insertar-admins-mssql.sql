USE [CanchaLibre];
GO

-- ============================================================
-- Script: insertar-admins.sql
-- Descripción: Inserta cuentas de administrador predefinidas.
-- ============================================================

DECLARE @idAdmin1 INT;
DECLARE @idAdmin2 INT;

-- Admin 1: Principal
INSERT INTO dbo.CUENTA_USUARIO (
    userName,
    [password],
    rol,
    intentosFallidos,
    activo
)
VALUES (
    'admin',
    'admin123',
    'ADMINISTRADOR',
    0,
    1
);

SET @idAdmin1 = SCOPE_IDENTITY();

INSERT INTO dbo.ADMINISTRADOR (
    idCuentaUsuario,
    nombres,
    correo,
    telefono,
    activo
)
VALUES (
    @idAdmin1,
    'Administrador Principal',
    'admin@canchalibre.pe',
    '999000001',
    1
);

-- Admin 2: Super Admin
INSERT INTO dbo.CUENTA_USUARIO (
    userName,
    [password],
    rol,
    intentosFallidos,
    activo
)
VALUES (
    'superadmin',
    'super123',
    'ADMINISTRADOR',
    0,
    1
);

SET @idAdmin2 = SCOPE_IDENTITY();

INSERT INTO dbo.ADMINISTRADOR (
    idCuentaUsuario,
    nombres,
    correo,
    telefono,
    activo
)
VALUES (
    @idAdmin2,
    'Super Administrador',
    'superadmin@canchalibre.pe',
    '999000002',
    1
);
GO
