USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarReserva;
DROP PROCEDURE IF EXISTS cancelarReserva;
DROP PROCEDURE IF EXISTS listarCanchasDisponibles;

DELIMITER //
-- RF03: Realizar reserva por parte de un Cliente
CREATE PROCEDURE insertarReserva(
    IN p_fechaHora DATETIME,
    IN p_duracion INT,
    IN p_idCancha INT,
    IN p_idCliente INT,
    OUT p_id INT)
BEGIN
    INSERT INTO Reserva (fechaHora, duracion, estado, idCancha, idCliente)
    VALUES (p_fechaHora, p_duracion, 'EnEspera', p_idCancha, p_idCliente);
    SET p_id = LAST_INSERT_ID();
END //

-- RF10: Cancelar reserva (Validación de 2 horas antes)
CREATE PROCEDURE cancelarReserva(IN p_idReserva INT, OUT p_exito BOOLEAN)
BEGIN
    DECLARE v_fecha DATETIME;
    SELECT fechaHora INTO v_fecha FROM Reserva WHERE idReserva = p_idReserva;
    
    -- Si la diferencia es mayor o igual a 2 horas, permite cancelar
    IF TIMESTAMPDIFF(HOUR, NOW(), v_fecha) >= 2 THEN
        UPDATE Reserva SET estado = 'Cancelado' WHERE idReserva = p_idReserva;
        SET p_exito = TRUE;
    ELSE
        SET p_exito = FALSE;
    END IF;
END //

-- RF02: Visualizar disponibilidad de canchas
CREATE PROCEDURE listarCanchasDisponibles()
BEGIN
    SELECT idCancha, nombre, deporte, precioPorHora, direccion 
    FROM Cancha 
    WHERE disponible = TRUE;
END //

DELIMITER ;