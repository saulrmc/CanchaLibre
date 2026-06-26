USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarPropietario;
DROP PROCEDURE IF EXISTS modificarPropietario;
DROP PROCEDURE IF EXISTS eliminarPropietario;
DROP PROCEDURE IF EXISTS buscarPropietarioPorId;
DROP PROCEDURE IF EXISTS listarPropietarios;
DROP PROCEDURE IF EXISTS buscarPropietarioPorNombre;
DROP PROCEDURE IF EXISTS buscarPropietarioPorCuenta;
DROP PROCEDURE IF EXISTS actualizarSaldo;

DELIMITER //
CREATE PROCEDURE insertarPropietario(
    IN  p_idCuentaUsuario INT,
    IN  p_nombres   VARCHAR(150),
    IN  p_correo    VARCHAR(50),
    IN  p_telefono  VARCHAR(15),
    IN  p_ruc CHAR(11),
    IN  p_activo    BOOLEAN,
    OUT p_id INT)
BEGIN
    INSERT INTO PROPIETARIO(
        idCuentaUsuario,
        nombres,
        correo,
        telefono,
        ruc,
        activo)
    VALUES(p_idCuentaUsuario,
           p_nombres,
           p_correo,
           p_telefono,
           p_ruc,
           p_activo);

    SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE modificarPropietario(
    IN  p_idCuentaUsuario INT,
    IN  p_nombres   VARCHAR(150),
    IN  p_correo    VARCHAR(50),
    IN  p_telefono  VARCHAR(15),
    IN  p_calificacion  DECIMAL(2,1),
    IN  p_ruc   CHAR(11),
    IN  p_saldo DECIMAL(10,2),
    IN  p_activo    BOOLEAN,
    IN  p_id INT)
BEGIN
UPDATE PROPIETARIO
SET
    idCuentaUsuario = p_idCuentaUsuario,
    nombres = p_nombres,
    correo = p_correo,
    telefono = p_telefono,
    calificacion = p_calificacion,
    ruc = p_ruc,
    saldo = p_saldo,
    activo = p_activo
WHERE id = p_id;
END //

CREATE PROCEDURE eliminarPropietario(IN p_id INT)
BEGIN
    UPDATE PROPIETARIO
    SET activo = FALSE
    WHERE id = p_id;
END //

-- 1. Buscar Propietario por ID
CREATE PROCEDURE buscarPropietarioPorId(IN p_id INT)
BEGIN
    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM PROPIETARIO p
             INNER JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
    WHERE p.id = p_id;
END //

-- 2. Listar Propietarios Activos
CREATE PROCEDURE listarPropietarios()
BEGIN
    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM PROPIETARIO p
             INNER JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
    WHERE p.activo = TRUE;
END //

-- 3. Buscar Propietario por Nombre
CREATE PROCEDURE buscarPropietarioPorNombre(IN p_nombres VARCHAR(150))
BEGIN
    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM PROPIETARIO p
             INNER JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
    WHERE p.nombres = p_nombres;
END //

-- 4. Buscar Propietario por Cuenta (UserName)
CREATE PROCEDURE buscarPropietarioPorCuenta(IN p_userName VARCHAR(50))
BEGIN
    SELECT
        p.id AS id,
        p.activo AS activo,
        p.nombres AS nombres,
        p.correo AS correo,
        p.telefono AS telefono,
        p.calificacion AS calificacion,
        p.ruc AS ruc,
        p.saldo AS saldo,
        p.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM PROPIETARIO p
             INNER JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
    WHERE cu.userName = p_userName;
END //

CREATE PROCEDURE actualizarSaldo(
    IN p_idPropietario INT,
    IN p_monto DECIMAL(10,2))
BEGIN
    UPDATE PROPIETARIO
    SET saldo = saldo + p_monto
    WHERE id = p_idPropietario;
END //