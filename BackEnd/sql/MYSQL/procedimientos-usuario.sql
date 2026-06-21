
USE CanchaLibre;

DROP PROCEDURE IF EXISTS obtenerCuentaPorUsername;
DROP PROCEDURE IF EXISTS insertarCliente;
DROP PROCEDURE IF EXISTS insertarPropietario;
DROP PROCEDURE IF EXISTS insertarAdministrador;
DROP PROCEDURE IF EXISTS modificarCliente;
DROP PROCEDURE IF EXISTS modificarPropietario;
DROP PROCEDURE IF EXISTS actualizarIntentosFallidos;
DROP PROCEDURE IF EXISTS bloquearCuenta;

DELIMITER //

-- RF01, RF06: Recupera datos de la cuenta para que Java valide
-- el hash de la contraseña y gestione los intentos fallidos.
-- Retorna también rol, intentosFallidos y fechaBloqueo para
-- que la capa de negocio decida si la cuenta está bloqueada.
CREATE PROCEDURE obtenerCuentaPorUsername(
    IN p_username VARCHAR(50))
BEGIN
    SELECT id, activo, userName, password, rol,
           intentosFallidos, ultimaSesion, fechaBloqueo
    FROM CUENTA_USUARIO
    WHERE userName = p_username;
END //

-- RF06: Actualiza intentos fallidos y ultimaSesion tras un intento
-- de login incorrecto. La lógica de bloqueo (3 intentos en 1 minuto)
-- se evalúa en Java antes de llamar a bloquearCuenta.
CREATE PROCEDURE actualizarIntentosFallidos(
    IN p_idCuenta INT)
BEGIN
    UPDATE CUENTA_USUARIO
    SET intentosFallidos = intentosFallidos + 1,
        ultimaSesion     = NOW()
    WHERE id = p_idCuenta;
END //

-- RF06: Bloquea la cuenta y resetea intentos fallidos.
-- Se llama desde Java cuando se detectan 3 intentos en menos de 1 minuto.
CREATE PROCEDURE bloquearCuenta(
    IN p_idCuenta INT)
BEGIN
    UPDATE CUENTA_USUARIO
    SET fechaBloqueo     = NOW(),
        intentosFallidos = 0,
        activo           = FALSE
    WHERE id = p_idCuenta;
END //

-- RF01: Registro de Cliente.
-- Crea primero la CUENTA_USUARIO y luego el CLIENTE vinculado.
-- Valida que el correo no esté previamente registrado en CLIENTE.
-- La contraseña debe llegar ya hasheada desde Java.
-- Retorna p_id = 0 si el correo ya existe.
CREATE PROCEDURE insertarCliente(
    IN  p_nombres    VARCHAR(150),
    IN  p_correo     VARCHAR(100),
    IN  p_telefono   VARCHAR(20),
    IN  p_password   VARCHAR(255),
    IN  p_username   VARCHAR(50),
    OUT p_id         INT)
BEGIN
    DECLARE v_idCuenta INT;

    IF EXISTS (SELECT 1 FROM CLIENTE WHERE correo = p_correo) THEN
        SET p_id = 0;
    ELSE
        INSERT INTO CUENTA_USUARIO (userName, password, rol, intentosFallidos)
        VALUES (p_username, p_password, 'CLIENTE', 0);
        SET v_idCuenta = LAST_INSERT_ID();

        INSERT INTO CLIENTE (nombres, correo, telefono, calificacion, idCuenta)
        VALUES (p_nombres, p_correo, p_telefono, 0, v_idCuenta);
        SET p_id = LAST_INSERT_ID();
    END IF;
END //

-- RF04: Registro de Propietario.
-- Mismo patrón que insertarCliente.
-- Valida que el correo no esté previamente registrado en PROPIETARIO.
CREATE PROCEDURE insertarPropietario(
    IN  p_nombres    VARCHAR(150),
    IN  p_correo     VARCHAR(100),
    IN  p_telefono   VARCHAR(20),
    IN  p_password   VARCHAR(255),
    IN  p_username   VARCHAR(50),
    OUT p_id         INT)
BEGIN
    DECLARE v_idCuenta INT;

    IF EXISTS (SELECT 1 FROM PROPIETARIO WHERE correo = p_correo) THEN
        SET p_id = 0;
    ELSE
        INSERT INTO CUENTA_USUARIO (userName, password, rol, intentosFallidos)
        VALUES (p_username, p_password, 'PROPIETARIO', 0);
        SET v_idCuenta = LAST_INSERT_ID();

        INSERT INTO PROPIETARIO (nombres, correo, telefono, calificacion, idCuenta)
        VALUES (p_nombres, p_correo, p_telefono, 0, v_idCuenta);
        SET p_id = LAST_INSERT_ID();
    END IF;
END //

-- Registro de Administrador.
-- No está en los RF explícitamente pero es necesario para el sistema.
CREATE PROCEDURE insertarAdministrador(
    IN  p_nombres    VARCHAR(150),
    IN  p_correo     VARCHAR(100),
    IN  p_telefono   VARCHAR(20),
    IN  p_password   VARCHAR(255),
    IN  p_username   VARCHAR(50),
    OUT p_id         INT)
BEGIN
    DECLARE v_idCuenta INT;

    IF EXISTS (SELECT 1 FROM ADMINISTRADOR WHERE correo = p_correo) THEN
        SET p_id = 0;
    ELSE
        INSERT INTO CUENTA_USUARIO (userName, password, rol, intentosFallidos)
        VALUES (p_username, p_password, 'ADMINISTRADOR', 0);
        SET v_idCuenta = LAST_INSERT_ID();

        INSERT INTO ADMINISTRADOR (nombres, correo, telefono, idCuenta)
        VALUES (p_nombres, p_correo, p_telefono, v_idCuenta);
        SET p_id = LAST_INSERT_ID();
    END IF;
END //

-- RF13: Editar información personal del Cliente.
-- Solo expone campos editables por el usuario:
-- nombres, correo, telefono y password.
-- intentosFallidos y ultimaSesion son de control interno
-- y se gestionan en otros procedimientos.
CREATE PROCEDURE modificarCliente(
    IN p_id        INT,
    IN p_nombres   VARCHAR(150),
    IN p_correo    VARCHAR(100),
    IN p_telefono  VARCHAR(20),
    IN p_password  VARCHAR(255))
BEGIN
    DECLARE v_idCuenta INT;

    UPDATE CLIENTE
    SET nombres  = p_nombres,
        correo   = p_correo,
        telefono = p_telefono
    WHERE id = p_id;

    SELECT idCuenta INTO v_idCuenta FROM CLIENTE WHERE id = p_id;

    UPDATE CUENTA_USUARIO
    SET password = p_password
    WHERE id = v_idCuenta;
END //

-- RF13: Editar información personal del Propietario.
-- Mismo patrón que modificarCliente.
CREATE PROCEDURE modificarPropietario(
    IN p_id        INT,
    IN p_nombres   VARCHAR(150),
    IN p_correo    VARCHAR(100),
    IN p_telefono  VARCHAR(20),
    IN p_password  VARCHAR(255))
BEGIN
    DECLARE v_idCuenta INT;

    UPDATE PROPIETARIO
    SET nombres  = p_nombres,
        correo   = p_correo,
        telefono = p_telefono
    WHERE id = p_id;

    SELECT idCuenta INTO v_idCuenta FROM PROPIETARIO WHERE id = p_id;

    UPDATE CUENTA_USUARIO
    SET password = p_password
    WHERE id = v_idCuenta;
END //

DELIMITER ;
