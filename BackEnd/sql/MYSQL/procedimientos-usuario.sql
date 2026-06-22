
USE CanchaLibre;

DROP PROCEDURE IF EXISTS obtenerCuentaPorUsername;
DROP PROCEDURE IF EXISTS listarClientes;
DROP PROCEDURE IF EXISTS obtenerClientePorId;
DROP PROCEDURE IF EXISTS insertarCliente;
DROP PROCEDURE IF EXISTS insertarPropietario;
DROP PROCEDURE IF EXISTS insertarAdministrador;
DROP PROCEDURE IF EXISTS modificarCliente;
DROP PROCEDURE IF EXISTS modificarPropietario;
DROP PROCEDURE IF EXISTS actualizarIntentosFallidos;
DROP PROCEDURE IF EXISTS bloquearCuenta;

DELIMITER //

CREATE PROCEDURE obtenerCuentaPorUsername(
    IN p_username VARCHAR(100)
)
BEGIN
    SELECT 
        id,
        activo,
        userName,
        password,
        rol,
        intentosFallidos,
        ultimaSesion,
        fechaBloqueo
    FROM CuentaUsuario
    WHERE userName = p_username;
END //

CREATE PROCEDURE actualizarIntentosFallidos(
    IN p_idCuenta INT
)
BEGIN
    UPDATE CuentaUsuario
    SET intentosFallidos = intentosFallidos + 1,
        ultimaSesion = NOW()
    WHERE id = p_idCuenta;
END //

CREATE PROCEDURE bloquearCuenta(
    IN p_idCuenta INT
)
BEGIN
    UPDATE CuentaUsuario
    SET fechaBloqueo = NOW(),
        intentosFallidos = 0,
        activo = FALSE
    WHERE id = p_idCuenta;
END //

CREATE PROCEDURE listarClientes()
BEGIN
    SELECT 
        p.id,
        p.activo,
        p.nombres,
        p.correo,
        p.telefono,
        c.calificacion,
        cu.id AS idCuentaUsuario,
        cu.userName,
        cu.password,
        cu.rol,
        cu.intentosFallidos,
        cu.ultimaSesion,
        cu.fechaBloqueo
    FROM Cliente c
    INNER JOIN Persona p ON c.id = p.id
    LEFT JOIN CuentaUsuario cu ON p.idCuentaUsuario = cu.id;
END //

CREATE PROCEDURE obtenerClientePorId(
    IN p_id INT
)
BEGIN
    SELECT 
        p.id,
        p.activo,
        p.nombres,
        p.correo,
        p.telefono,
        c.calificacion,
        cu.id AS idCuentaUsuario,
        cu.userName,
        cu.password,
        cu.rol,
        cu.intentosFallidos,
        cu.ultimaSesion,
        cu.fechaBloqueo
    FROM Cliente c
    INNER JOIN Persona p ON c.id = p.id
    LEFT JOIN CuentaUsuario cu ON p.idCuentaUsuario = cu.id
    WHERE p.id = p_id;
END //

CREATE PROCEDURE insertarCliente(
    IN  p_nombres  VARCHAR(150),
    IN  p_correo   VARCHAR(100),
    IN  p_telefono VARCHAR(20),
    IN  p_password VARCHAR(255),
    IN  p_username VARCHAR(100),
    OUT p_id       INT
)
BEGIN
    DECLARE v_idCuenta INT;
    DECLARE v_idPersona INT;

    IF EXISTS (SELECT 1 FROM Persona WHERE correo = p_correo)
       OR EXISTS (SELECT 1 FROM CuentaUsuario WHERE userName = p_username) THEN
        SET p_id = 0;
    ELSE
        INSERT INTO CuentaUsuario (
            activo, userName, password, rol, intentosFallidos
        )
        VALUES (
            TRUE, p_username, p_password, 'CLIENTE', 0
        );

        SET v_idCuenta = LAST_INSERT_ID();

        INSERT INTO Persona (
            activo, nombres, correo, telefono, idCuentaUsuario
        )
        VALUES (
            TRUE, p_nombres, p_correo, p_telefono, v_idCuenta
        );

        SET v_idPersona = LAST_INSERT_ID();

        INSERT INTO Cliente (
            id, calificacion
        )
        VALUES (
            v_idPersona, 0
        );

        SET p_id = v_idPersona;
    END IF;
END //

CREATE PROCEDURE insertarPropietario(
    IN  p_nombres  VARCHAR(150),
    IN  p_correo   VARCHAR(100),
    IN  p_telefono VARCHAR(20),
    IN  p_password VARCHAR(255),
    IN  p_username VARCHAR(100),
    OUT p_id       INT
)
BEGIN
    DECLARE v_idCuenta INT;
    DECLARE v_idPersona INT;

    IF EXISTS (SELECT 1 FROM Persona WHERE correo = p_correo)
       OR EXISTS (SELECT 1 FROM CuentaUsuario WHERE userName = p_username) THEN
        SET p_id = 0;
    ELSE
        INSERT INTO CuentaUsuario (
            activo, userName, password, rol, intentosFallidos
        )
        VALUES (
            TRUE, p_username, p_password, 'PROPIETARIO', 0
        );

        SET v_idCuenta = LAST_INSERT_ID();

        INSERT INTO Persona (
            activo, nombres, correo, telefono, idCuentaUsuario
        )
        VALUES (
            TRUE, p_nombres, p_correo, p_telefono, v_idCuenta
        );

        SET v_idPersona = LAST_INSERT_ID();

        INSERT INTO Propietario (id)
        VALUES (v_idPersona);

        SET p_id = v_idPersona;
    END IF;
END //

CREATE PROCEDURE insertarAdministrador(
    IN  p_nombres  VARCHAR(150),
    IN  p_correo   VARCHAR(100),
    IN  p_telefono VARCHAR(20),
    IN  p_password VARCHAR(255),
    IN  p_username VARCHAR(100),
    OUT p_id       INT
)
BEGIN
    DECLARE v_idCuenta INT;
    DECLARE v_idPersona INT;

    IF EXISTS (SELECT 1 FROM Persona WHERE correo = p_correo)
       OR EXISTS (SELECT 1 FROM CuentaUsuario WHERE userName = p_username) THEN
        SET p_id = 0;
    ELSE
        INSERT INTO CuentaUsuario (
            activo, userName, password, rol, intentosFallidos
        )
        VALUES (
            TRUE, p_username, p_password, 'ADMINISTRADOR', 0
        );

        SET v_idCuenta = LAST_INSERT_ID();

        INSERT INTO Persona (
            activo, nombres, correo, telefono, idCuentaUsuario
        )
        VALUES (
            TRUE, p_nombres, p_correo, p_telefono, v_idCuenta
        );

        SET v_idPersona = LAST_INSERT_ID();

        INSERT INTO Administrador (id)
        VALUES (v_idPersona);

        SET p_id = v_idPersona;
    END IF;
END //

CREATE PROCEDURE modificarCliente(
    IN p_id       INT,
    IN p_nombres  VARCHAR(150),
    IN p_correo   VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_password VARCHAR(255)
)
BEGIN
    DECLARE v_idCuenta INT;

    SELECT idCuentaUsuario
    INTO v_idCuenta
    FROM Persona
    WHERE id = p_id;

    UPDATE Persona
    SET nombres = p_nombres,
        correo = p_correo,
        telefono = p_telefono
    WHERE id = p_id;

    UPDATE CuentaUsuario
    SET password = p_password
    WHERE id = v_idCuenta;
END //

CREATE PROCEDURE modificarPropietario(
    IN p_id       INT,
    IN p_nombres  VARCHAR(150),
    IN p_correo   VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_password VARCHAR(255)
)
BEGIN
    DECLARE v_idCuenta INT;

    SELECT idCuentaUsuario
    INTO v_idCuenta
    FROM Persona
    WHERE id = p_id;

    UPDATE Persona
    SET nombres = p_nombres,
        correo = p_correo,
        telefono = p_telefono
    WHERE id = p_id;

    UPDATE CuentaUsuario
    SET password = p_password
    WHERE id = v_idCuenta;
END //

DELIMITER ;
