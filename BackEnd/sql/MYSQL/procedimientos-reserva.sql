USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarReserva;
DROP PROCEDURE IF EXISTS cancelarReserva;
DROP PROCEDURE IF EXISTS listarReservasCliente;
DROP PROCEDURE IF EXISTS insertarResena;

DELIMITER //

-- RF03: Realizar reserva por parte de un Cliente.
-- Valida que los bloques de la cancha en el rango solicitado
-- estén DISPONIBLES antes de insertar.
-- Si algún bloque no está disponible retorna p_id = 0.
-- Tras insertar, actualiza los bloques afectados a RESERVADO.
CREATE PROCEDURE insertarReserva(
    IN  p_fechaHoraInicio DATETIME,
    IN  p_fechaHoraFin    DATETIME,
    IN  p_idCancha        INT,
    IN  p_idCliente       INT,
    OUT p_id              INT)
BEGIN
    DECLARE v_bloqueNoDisponible INT DEFAULT 0;

    -- Verificar que todos los bloques del rango estén disponibles
    SELECT COUNT(*) INTO v_bloqueNoDisponible
    FROM BLOQUE_HORARIO
    WHERE idCancha    = p_idCancha
      AND dia         = UPPER(DAYNAME(p_fechaHoraInicio))
      AND horaInicio  >= TIME(p_fechaHoraInicio)
      AND horaFin     <= TIME(p_fechaHoraFin)
      AND estado     != 'DISPONIBLE';

    IF v_bloqueNoDisponible > 0 THEN
        SET p_id = 0;
    ELSE
        INSERT INTO RESERVA (estado, fechaHoraInicio, fechaHoraFin, idCliente, idCancha)
        VALUES ('PENDIENTE_PAGO', p_fechaHoraInicio, p_fechaHoraFin, p_idCliente, p_idCancha);
        SET p_id = LAST_INSERT_ID();

        -- Marcar bloques como RESERVADO
        UPDATE BLOQUE_HORARIO
        SET estado = 'RESERVADO'
        WHERE idCancha   = p_idCancha
          AND dia        = UPPER(DAYNAME(p_fechaHoraInicio))
          AND horaInicio >= TIME(p_fechaHoraInicio)
          AND horaFin    <= TIME(p_fechaHoraFin);
    END IF;
END //

-- RF10: Cancelar reserva con validación de 2 horas antes.
-- Si la cancelación es válida, libera los bloques a DISPONIBLE.
-- Retorna p_exito = FALSE si ya no está dentro del plazo.
CREATE PROCEDURE cancelarReserva(
    IN  p_idReserva INT,
    OUT p_exito     BOOLEAN)
BEGIN
    DECLARE v_fechaHoraInicio DATETIME;
    DECLARE v_idCancha        INT;

    SELECT fechaHoraInicio, idCancha
    INTO   v_fechaHoraInicio, v_idCancha
    FROM   RESERVA
    WHERE  id = p_idReserva;

    IF TIMESTAMPDIFF(HOUR, NOW(), v_fechaHoraInicio) >= 2 THEN
        UPDATE RESERVA
        SET estado = 'CANCELADA'
        WHERE id = p_idReserva;

        -- Liberar los bloques afectados
        UPDATE BLOQUE_HORARIO
        SET estado = 'DISPONIBLE'
        WHERE idCancha   = v_idCancha
          AND dia        = UPPER(DAYNAME(v_fechaHoraInicio))
          AND horaInicio >= TIME(v_fechaHoraInicio)
          AND horaFin    <= TIME((SELECT fechaHoraFin FROM RESERVA WHERE id = p_idReserva));

        SET p_exito = TRUE;
    ELSE
        SET p_exito = FALSE;
    END IF;
END //

-- RF09: Listar historial de reservas de un Cliente.
-- Incluye estado, fechas, nombre de cancha y dirección
-- para que el frontend pueda mostrar el detalle de cada una.
CREATE PROCEDURE listarReservasCliente(
    IN p_idCliente INT)
BEGIN
    SELECT
        r.id              AS idReserva,
        r.estado,
        r.fechaHoraInicio,
        r.fechaHoraFin,
        c.nombre          AS nombreCancha,
        c.direccion,
        p.monto,
        p.metodoPago
    FROM RESERVA r
    INNER JOIN CANCHA c ON c.id = r.idCancha
    LEFT  JOIN PAGO   p ON p.idReserva = r.id
    WHERE r.idCliente = p_idCliente
    ORDER BY r.fechaHoraInicio DESC;
END //

-- RF08: Insertar reseña para una cancha.
-- Valida que el cliente tenga una reserva COMPLETADA para esa cancha
-- y que no haya dejado ya una reseña para esa reserva.
-- Tras insertar recalcula promedioCalificacion en CANCHA.
CREATE PROCEDURE insertarResena(
    IN  p_descripcion  VARCHAR(500),
    IN  p_calificacion INT,
    IN  p_idReserva    INT,
    OUT p_id           INT)
BEGIN
    DECLARE v_idCancha     INT;
    DECLARE v_idCliente    INT;
    DECLARE v_estadoRes    VARCHAR(20);
    DECLARE v_yaReseno     INT DEFAULT 0;

    SELECT idCancha, idCliente, estado
    INTO   v_idCancha, v_idCliente, v_estadoRes
    FROM   RESERVA
    WHERE  id = p_idReserva;

    -- Verificar que la reserva esté COMPLETADA
    -- y que no exista ya una reseña para esa reserva
    SELECT COUNT(*) INTO v_yaReseno
    FROM RESENA WHERE idReserva = p_idReserva;

    IF v_estadoRes != 'COMPLETADA' OR v_yaReseno > 0 THEN
        SET p_id = 0;
    ELSE
        INSERT INTO RESENA (descripcion, calificacion, fechaPublicacion, idReserva)
        VALUES (p_descripcion, p_calificacion, NOW(), p_idReserva);
        SET p_id = LAST_INSERT_ID();

        -- Recalcular promedioCalificacion en CANCHA
        UPDATE CANCHA
        SET promedioCalificacion = (
            SELECT AVG(rs.calificacion)
            FROM RESENA rs
            INNER JOIN RESERVA rv ON rv.id = rs.idReserva
            WHERE rv.idCancha = v_idCancha
              AND rs.activo   = TRUE
        )
        WHERE id = v_idCancha;
    END IF;
END //

DELIMITER ;
