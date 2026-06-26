USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarCliente;
DROP PROCEDURE IF EXISTS modificarCliente;
DROP PROCEDURE IF EXISTS eliminarCliente;
DROP PROCEDURE IF EXISTS buscarClientePorId;
DROP PROCEDURE IF EXISTS listarClientes;
DROP PROCEDURE IF EXISTS buscarClientePorNombre;
DROP PROCEDURE IF EXISTS buscarClientePorCuenta;

DELIMITER //
CREATE PROCEDURE insertarCliente(
    IN  p_idCuentaUsuario INT,
    IN  p_nombres   VARCHAR(150),
    IN  p_correo    VARCHAR(50),
    IN  p_telefono  VARCHAR(15),
    IN  p_activo    BOOLEAN,
    OUT p_id INT)
BEGIN
INSERT INTO CLIENTE(
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

CREATE PROCEDURE modificarCliente(
    IN  p_idCuentaUsuario INT,
    IN  p_nombres   VARCHAR(150),
    IN  p_correo    VARCHAR(50),
    IN  p_telefono  VARCHAR(15),
    IN  p_calificacion  DECIMAL(2,1),
    IN  p_activo    BOOLEAN,
    IN  p_id INT)
BEGIN
    UPDATE CLIENTE
    SET
        idCuentaUsuario = p_idCuentaUsuario,
        nombres = p_nombres,
        correo = p_correo,
        telefono = p_telefono,
        calificacion = p_calificacion,
        activo = p_activo
    WHERE id = p_id;
END //

CREATE PROCEDURE eliminarCliente(IN p_id INT)
BEGIN
    UPDATE CLIENTE
    SET activo = FALSE
    WHERE id = p_id;
END //

-- 1. Buscar por ID
CREATE PROCEDURE buscarClientePorId(IN p_id INT)
BEGIN
    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM CLIENTE c
             INNER JOIN CUENTA_USUARIO cu ON c.idCuentaUsuario = cu.id
    WHERE c.id = p_id;
END //

-- 2. Listar Clientes Activos
CREATE PROCEDURE listarClientes()
BEGIN
    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM CLIENTE c
             INNER JOIN CUENTA_USUARIO cu ON c.idCuentaUsuario = cu.id
    WHERE c.activo = TRUE;
END //

-- 3. Buscar por Nombre (Utiliza LIKE para mayor flexibilidad si se requiere escalar)
CREATE PROCEDURE buscarClientePorNombre(IN p_nombres VARCHAR(150))
BEGIN
    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM CLIENTE c
             INNER JOIN CUENTA_USUARIO cu ON c.idCuentaUsuario = cu.id
    WHERE c.nombres = p_nombres;
END //

-- 4. Buscar por Cuenta (UserName)
CREATE PROCEDURE buscarClientePorCuenta(IN p_userName VARCHAR(50))
BEGIN
    SELECT
        c.id AS id,
        c.activo AS activo,
        c.nombres AS nombres,
        c.correo AS correo,
        c.telefono AS telefono,
        c.calificacion AS calificacion,
        c.idCuentaUsuario AS idCuentaUsuario,
        cu.userName AS userName,
        cu.password AS password,
        cu.rol AS rol,
        cu.intentosFallidos AS intentosFallidos,
        cu.ultimaSesion AS ultimaSesion,
        cu.fechaBloqueo AS fechaBloqueo
    FROM CLIENTE c
             INNER JOIN CUENTA_USUARIO cu ON c.idCuentaUsuario = cu.id
    WHERE cu.userName = p_userName;
END //

DELIMITER ;