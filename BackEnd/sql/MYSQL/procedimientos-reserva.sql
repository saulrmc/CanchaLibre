USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarReserva;
DROP PROCEDURE IF EXISTS cancelarReserva;
DROP PROCEDURE IF EXISTS listarReservasCliente;
DROP PROCEDURE IF EXISTS insertarResena;

DELIMITER //

-- RF03: Realizar reserva por parte de un Cliente.
-- Valida que el bloque horario seleccionado esté DISPONIBLE
-- antes de insertar la reserva.
-- Si el bloque no está disponible retorna p_id = 0.
-- Tras insertar, actualiza el bloque seleccionado a RESERVADO.
CREATE PROCEDURE insertarReserva(
    IN  p_fechaReserva    DATETIME,
    IN  p_idCancha        INT,
    IN  p_idCliente       INT,
    IN  p_idBloqueHorario INT,
    OUT p_id              INT
)
BEGIN
    DECLARE v_disponible INT DEFAULT 0;

    SELECT COUNT(*)
    INTO v_disponible
    FROM BloqueHorario
    WHERE id = p_idBloqueHorario
      AND idCancha = p_idCancha
      AND activo = TRUE
      AND estado = 'DISPONIBLE';

    IF v_disponible = 0 THEN
        SET p_id = 0;
    ELSE
        INSERT INTO Reserva (
            activo, fechaReserva, estado, idCliente, idCancha, idBloqueHorario
        )
        VALUES (
            TRUE, p_fechaReserva, 'ESPERA', p_idCliente, p_idCancha, p_idBloqueHorario
        );

        SET p_id = LAST_INSERT_ID();

        UPDATE BloqueHorario
        SET estado = 'RESERVADO'
        WHERE id = p_idBloqueHorario;
    END IF;
END //

-- RF10: Cancelar reserva con validación de 2 horas antes.
-- Si la cancelación es válida, libera el bloque horario a DISPONIBLE.
-- Retorna p_exito = FALSE si ya no está dentro del plazo.
CREATE PROCEDURE cancelarReserva(
    IN  p_idReserva INT,
    OUT p_exito     BOOLEAN
)
BEGIN
    DECLARE v_fechaReserva DATETIME;
    DECLARE v_idBloqueHorario INT;

    SELECT fechaReserva, idBloqueHorario
    INTO v_fechaReserva, v_idBloqueHorario
    FROM Reserva
    WHERE id = p_idReserva;

    IF v_fechaReserva IS NULL THEN
        SET p_exito = FALSE;
    ELSEIF TIMESTAMPDIFF(HOUR, NOW(), v_fechaReserva) >= 2 THEN
        UPDATE Reserva
        SET estado = 'CANCELADO'
        WHERE id = p_idReserva;

        UPDATE BloqueHorario
        SET estado = 'DISPONIBLE'
        WHERE id = v_idBloqueHorario;

        SET p_exito = TRUE;
    ELSE
        SET p_exito = FALSE;
    END IF;
END //

-- RF09: Listar historial de reservas de un Cliente.
-- Incluye estado, fecha, nombre de cancha, dirección,
-- datos del bloque horario y pago si existe.
CREATE PROCEDURE listarReservasCliente(
    IN p_idCliente INT
)
BEGIN
    SELECT
    r.id AS idReserva,
    r.estado,
    r.idCliente,
    r.idCancha,
    p.idPago,
    r.fechaReserva,
    c.nombre AS nombreCancha,
    c.direccion,
    bh.dia,
    bh.horaInicio,
    bh.horaFin,
    bh.precio,
    p.monto,
    p.metodoPago
    FROM Reserva r
    INNER JOIN Cancha c ON c.id = r.idCancha
    INNER JOIN BloqueHorario bh ON bh.id = r.idBloqueHorario
    LEFT JOIN Comprobante comp ON comp.idReserva = r.id
    LEFT JOIN Pago p ON p.idComprobante = comp.idComprobante
    WHERE r.idCliente = p_idCliente
    ORDER BY r.fechaReserva DESC;
END //

-- RF08: Insertar reseña para una cancha.
-- Valida que el cliente tenga una reserva COMPLETADA para esa cancha.
-- Tras insertar recalcula promedioCalificacion en Cancha.
CREATE PROCEDURE insertarResena(
    IN  p_descripcion  VARCHAR(120),
    IN  p_calificacion INT,
    IN  p_idReserva    INT,
    OUT p_id           INT
)
BEGIN
    DECLARE v_idCancha INT;
    DECLARE v_idCliente INT;
    DECLARE v_estadoRes VARCHAR(20);
    DECLARE v_yaReseno INT DEFAULT 0;

    SELECT idCancha, idCliente, estado
    INTO v_idCancha, v_idCliente, v_estadoRes
    FROM Reserva
    WHERE id = p_idReserva;

    SELECT COUNT(*)
    INTO v_yaReseno
    FROM Resena
    WHERE idCancha = v_idCancha
      AND idCliente = v_idCliente;

    IF v_estadoRes != 'COMPLETADO' OR v_yaReseno > 0 THEN
        SET p_id = 0;
    ELSE
        INSERT INTO Resena (
            activo, descripcion, calificacion, fechaPublicacion, idCancha, idCliente
        )
        VALUES (
            TRUE, p_descripcion, p_calificacion, NOW(), v_idCancha, v_idCliente
        );

        SET p_id = LAST_INSERT_ID();

        UPDATE Cancha
        SET promedioCalificacion = (
            SELECT AVG(rs.calificacion)
            FROM Resena rs
            WHERE rs.idCancha = v_idCancha
              AND rs.activo = TRUE
        )
        WHERE id = v_idCancha;
    END IF;
END //

DELIMITER ;
