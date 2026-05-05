USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarCliente;
DROP PROCEDURE IF EXISTS modificarCliente;
DROP PROCEDURE IF EXISTS insertarPropietario;

DELIMITER //
-- RF01: Registro de Cliente
CREATE PROCEDURE insertarCliente(
    IN p_nombres VARCHAR(150),
    IN p_correo VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_contrasena VARCHAR(255),
    IN p_calificacion INT,
    OUT p_id INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Cliente WHERE correo = p_correo) THEN
        INSERT INTO Cliente (nombres, correo, telefono, contrasena, calificacion, intentosFallidos)
        VALUES (p_nombres, p_correo, p_telefono, p_contrasena, p_calificacion, 0, NULL,1,'CLIENTE');
        SET p_id = LAST_INSERT_ID();
    ELSE
        SET p_id = 0;
    END IF;
END //

-- RF13: Editar información personal del Cliente
CREATE PROCEDURE modificarCliente(
    IN p_id INT,
    IN p_nombres VARCHAR(150),
    IN p_contrasena VARCHAR(255),
    IN p_correo VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_intentos INT,
    IN p_ultimaSesion DATETIME,
    IN p_calificacion INT)
BEGIN
UPDATE Cliente
SET nombres = p_nombres,
    contrasena = p_contrasena,
    correo = p_correo,
    telefono = p_telefono,
    intentosFallidos = p_intentos,
    ultimaSesion = p_ultimaSesion,
    calificacion = p_calificacion
WHERE idCliente = p_id;
END //

# -- RF04: Registro de Propietario
# CREATE PROCEDURE insertarPropietario(
#     IN p_nombres VARCHAR(150),
#     IN p_correo VARCHAR(100),
#     IN p_ruc VARCHAR(20),
#     OUT p_id INT)
# BEGIN
#     INSERT INTO Propietario (nombres, correo, ruc)
#     VALUES (p_nombres, p_correo, p_ruc,1,'PROPIETARIO');
#     SET p_id = LAST_INSERT_ID();
# END //

DELIMITER ;