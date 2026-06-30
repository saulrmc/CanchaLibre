USE CanchaLibre;

-- ============================================================
-- Script: insertar-admins.sql
-- Descripción: Inserta cuentas de administrador predefinidas.
-- El registro de usuarios no permite crear admins, por lo que
-- estas cuentas deben existir previamente en la BD.
-- Nota: Las contraseñas se almacenan en texto plano (VARCHAR 50)
--       tal como espera la lógica de login en CuentaUsuarioBOImpl.
-- ============================================================

-- Admin 1: Principal
INSERT INTO CUENTA_USUARIO (userName, password, rol, intentosFallidos, activo)
VALUES ('admin', 'admin123', 'ADMINISTRADOR', 0, TRUE);
SET @idAdmin1 = LAST_INSERT_ID();

INSERT INTO ADMINISTRADOR (idCuentaUsuario, nombres, correo, telefono, activo)
VALUES (@idAdmin1, 'Administrador Principal', 'admin@canchalibre.pe', '999000001', TRUE);

-- Admin 2: Super Admin
INSERT INTO CUENTA_USUARIO (userName, password, rol, intentosFallidos, activo)
VALUES ('superadmin', 'super123', 'ADMINISTRADOR', 0, TRUE);
SET @idAdmin2 = LAST_INSERT_ID();

INSERT INTO ADMINISTRADOR (idCuentaUsuario, nombres, correo, telefono, activo)
VALUES (@idAdmin2, 'Super Administrador', 'superadmin@canchalibre.pe', '999000002', TRUE);
