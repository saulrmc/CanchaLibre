USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarCuentaUsuario;
DROP PROCEDURE IF EXISTS modificarCuentaUsuario;
DROP PROCEDURE IF EXISTS eliminarCuentaUsuario;
DROP PROCEDURE IF EXISTS buscarCuentaUsuarioPorId;
DROP PROCEDURE IF EXISTS listarCuentaUsuarios;
DROP PROCEDURE IF EXISTS loginUsuario;
DROP PROCEDURE IF EXISTS actualizarSeguridad;

DELIMITER //
CREATE PROCEDURE insertarCuentaUsuario(
    IN p_userName VARCHAR(50),
    IN p_password VARCHAR(50),
    IN p_rol    VARCHAR(15),
    IN p_fechaBloqueo DATETIME,
    IN p_activo BOOLEAN,
    OUT p_id INT)
BEGIN
    INSERT INTO CUENTA_USUARIO(
        userName,
        password,
        rol,
        fechaBloqueo,
        activo
    )
    VALUES(
           p_userName,
           p_password,
           p_rol,
           p_fechaBloqueo,
           p_activo);
SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE modificarCuentaUsuario(
    IN p_userName VARCHAR(50),
    IN p_password VARCHAR(50),
    IN p_intentosFallidos INT,
    IN p_ultimaSesion DATETIME,
    IN p_fechaBloqueo DATETIME,
    IN p_activo BOOLEAN,
    IN p_id INT)
BEGIN
UPDATE CUENTA_USUARIO
SET
    userName = p_userName,
    password = p_password,
    intentosFallidos = p_intentosFallidos,
    ultimaSesion = p_ultimaSesion,
    fechaBloqueo = p_fechaBloqueo,
    activo = p_activo
WHERE id = p_id;
END //

CREATE PROCEDURE eliminarCuentaUsuario(IN p_id INT)
BEGIN
    UPDATE CUENTA_USUARIO
    SET activo = FALSE
    WHERE id = p_id;
END //

CREATE PROCEDURE buscarCuentaUsuarioPorId(IN p_id INT)
BEGIN
SELECT * FROM CUENTA_USUARIO WHERE id = p_id;
END //

CREATE PROCEDURE listarCuentaUsuarios()
BEGIN
SELECT * FROM CUENTA_USUARIO WHERE activo = TRUE;
END //

CREATE PROCEDURE loginUsuario(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(50),
    OUT p_valido BOOLEAN
)
BEGIN
    DECLARE v_count INT DEFAULT 0;

    SELECT COUNT(*) INTO v_count
    FROM CUENTA_USUARIO
    WHERE userName = p_username
      AND password = p_password;

    IF v_count > 0 THEN
            SET p_valido = TRUE;
    ELSE
            SET p_valido = FALSE;
    END IF;
END //

CREATE PROCEDURE actualizarSeguridad(
    IN p_intentosFallidos INT,
    IN p_ultimaSesion DATETIME,
    IN p_fechaBloqueo DATETIME,
    IN p_id INT
)
BEGIN
    UPDATE CUENTA_USUARIO
    SET
        intentosFallidos = p_intentosFallidos,
        ultimaSesion = p_ultimaSesion,
        fechaBloqueo = p_fechaBloqueo
    WHERE id = p_id;
END //