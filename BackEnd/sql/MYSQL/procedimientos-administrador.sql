USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarAdministrador;
DROP PROCEDURE IF EXISTS modificarAdministrador;
DROP PROCEDURE IF EXISTS eliminarAdministrador;
DROP PROCEDURE IF EXISTS buscarAdministradorPorId;
DROP PROCEDURE IF EXISTS listarAdministradores;
DROP PROCEDURE IF EXISTS buscarAdministradorPorNombre;
DROP PROCEDURE IF EXISTS buscarAdministradorPorCuenta;

DELIMITER //
CREATE PROCEDURE insertarAdministrador(
    IN  p_idCuentaUsuario INT,
    IN  p_nombres   VARCHAR(150),
    IN  p_correo    VARCHAR(50),
    IN  p_telefono  VARCHAR(15),
    IN  p_activo    BOOLEAN,
    OUT p_id INT)
BEGIN
    INSERT INTO ADMINISTRADOR(
        idCuentaUsuario,
        nombres,
        correo,
        telefono,
        activo)
    VALUES(p_idCuentaUsuario,
           p_nombres,
           p_correo,
           p_telefono,
           p_activo);

    SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE modificarAdministrador(
    IN  p_idCuentaUsuario INT,
    IN  p_nombres   VARCHAR(150),
    IN  p_correo    VARCHAR(50),
    IN  p_telefono  VARCHAR(15),
    IN  p_activo    BOOLEAN,
    IN  p_id INT)
BEGIN
    UPDATE ADMINISTRADOR
    SET
        idCuentaUsuario = p_idCuentaUsuario,
        nombres = p_nombres,
        correo = p_correo,
        telefono = p_telefono,
        activo = p_activo
    WHERE id = p_id;
END //

CREATE PROCEDURE eliminarAdministrador(IN p_id INT)
BEGIN
    UPDATE ADMINISTRADOR
    SET activo = FALSE
    WHERE id = p_id;
END //

CREATE PROCEDURE buscarAdministradorPorId(IN p_id INT)
BEGIN
    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM ADMINISTRADOR a
             INNER JOIN CUENTA_USUARIO cu ON a.idCuentaUsuario = cu.id
    WHERE a.id = p_id;
END //

CREATE PROCEDURE listarAdministradores()
BEGIN
    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM ADMINISTRADOR a
             INNER JOIN CUENTA_USUARIO cu ON a.idCuentaUsuario = cu.id
    WHERE a.activo = TRUE;
END //

CREATE PROCEDURE buscarAdministradorPorNombre(IN p_nombres VARCHAR(150))
BEGIN
    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM ADMINISTRADOR a
             INNER JOIN CUENTA_USUARIO cu ON a.idCuentaUsuario = cu.id
    WHERE a.nombres = p_nombres;
END //

CREATE PROCEDURE buscarAdministradorPorCuenta(IN p_userName VARCHAR(50))
BEGIN
    SELECT
        a.id AS id,
        a.activo AS activo,
        a.nombres AS nombres,
        a.correo AS correo,
        a.telefono AS telefono,
        a.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM ADMINISTRADOR a
             INNER JOIN CUENTA_USUARIO cu ON a.idCuentaUsuario = cu.id
    WHERE cu.userName = p_userName;
END //
