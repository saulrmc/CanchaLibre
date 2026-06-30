USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarReserva;
DROP PROCEDURE IF EXISTS modificarReserva;
DROP PROCEDURE IF EXISTS eliminarReserva;
DROP PROCEDURE IF EXISTS cancelarReserva; /* unused*/
DROP PROCEDURE IF EXISTS buscarReservaPorId;
DROP PROCEDURE IF EXISTS listarReservas;
DROP PROCEDURE IF EXISTS listarReservasPorCuenta;
DROP PROCEDURE IF EXISTS listarReservasPorId;
DROP PROCEDURE IF EXISTS listarReservasPorCancha;

DROP PROCEDURE IF EXISTS listarReservasCliente; /* unused */
DELIMITER //

-- RF03: Realizar reserva por parte de un Cliente.
-- Valida que el bloque horario seleccionado esté DISPONIBLE
-- antes de insertar la reserva.
-- Si el bloque no está disponible retorna p_id = 0.
-- Tras insertar, actualiza el bloque seleccionado a RESERVADO.

CREATE PROCEDURE insertarReserva(
    IN p_estado VARCHAR(20),
    IN p_idCliente INT,
    IN p_idCancha INT,
    IN p_fechaCreacion DATETIME,
    IN p_idPago INT,         -- Recibe el ID o NULL desde Java
    IN p_activo BOOLEAN,      -- Recibe el estado lógico
    OUT p_id INT             -- Parámetro de salida para el ID generado
)
BEGIN
    INSERT INTO RESERVA (
        idCliente,
        idCancha,
        estado,
        fechaCreacion,
        idPago,
        activo
    )
    VALUES (
               p_idCliente,
               p_idCancha,
               p_estado,
               p_fechaCreacion,
               p_idPago,
               p_activo
           );

    SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE modificarReserva(
    IN p_estado VARCHAR(20),
    IN p_idPago INT,
    IN p_activo BOOLEAN,
    IN p_id INT
)
BEGIN
UPDATE RESERVA
SET estado = p_estado,
    idPago = p_idPago,
    activo = p_activo
WHERE id = p_id;
END //

CREATE PROCEDURE eliminarReserva(
    IN p_id INT
)
BEGIN
UPDATE RESERVA
SET activo = FALSE
WHERE id = p_id;
END //

CREATE PROCEDURE buscarReservaPorId(
    IN p_id INT
)
BEGIN
SELECT id, estado, idCliente, idCancha, idPago, fechaCreacion, activo
FROM RESERVA
WHERE id = p_id;
END //

CREATE PROCEDURE listarReservas()
BEGIN
SELECT id, estado, idCliente, idCancha, idPago, fechaCreacion, activo
FROM RESERVA WHERE activo = TRUE;
END //

CREATE PROCEDURE listarReservasPorCuenta(
    IN p_cuenta VARCHAR(50)
)
BEGIN
    SELECT
        r.id AS id,
        r.estado AS estado,
        r.fechaCreacion AS fechaCreacion,
        r.activo AS activo,
        c.id AS idCliente,
        cu.userName AS usuarioCliente,
        ca.id AS idCancha,
        ca.nombre AS nombreCancha,
        p.id AS idPago,
        p.monto AS montoPago
    FROM RESERVA r
             INNER JOIN CLIENTE c ON r.idCliente = c.id
             INNER JOIN CUENTA_USUARIO cu ON c.idCuentaUsuario = cu.id
             INNER JOIN CANCHA ca ON r.idCancha = ca.id
             LEFT JOIN PAGO p ON p.idReserva = r.id
    WHERE cu.userName = p_cuenta;
END //

CREATE PROCEDURE listarReservasPorId(
    IN p_idCliente INT
)
BEGIN
    SELECT
        r.id AS id,
        r.estado AS estado,
        r.fechaCreacion AS fechaCreacion,
        r.activo AS activo,
        c.id AS idCliente,
        cu.userName AS usuarioCliente,
        ca.id AS idCancha,
        ca.nombre AS nombreCancha,
        p.id AS idPago,
        p.monto AS montoPago
    FROM RESERVA r
             INNER JOIN CLIENTE c ON r.idCliente = c.id
             INNER JOIN CUENTA_USUARIO cu ON c.idCuentaUsuario = cu.id
             INNER JOIN CANCHA ca ON r.idCancha = ca.id
             LEFT JOIN PAGO p ON p.idReserva = r.id
    WHERE r.idCliente = p_idCliente;
END //

-- RF10: Cancelar reserva con validación de 24 horas antes.
-- Si la cancelación es válida, libera el bloque horario a DISPONIBLE.
-- Retorna p_exito = FALSE si ya no está dentro del plazo.
CREATE PROCEDURE cancelarReserva(
    IN  p_idReserva INT,
    OUT p_exito     BOOLEAN
)
BEGIN
    DECLARE v_fechaCreacion DATETIME;

    SELECT fechaCreacion
    INTO v_fechaCreacion
    FROM RESERVA
    WHERE id = p_idReserva;

    IF v_fechaCreacion IS NULL THEN
        SET p_exito = FALSE;
        -- Valida que la cancelación sea con al menos 24 horas de anticipación
    ELSEIF TIMESTAMPDIFF(HOUR, NOW(), v_fechaCreacion) >= 24 THEN
        -- Cambiar estado de la reserva a CANCELADA
        UPDATE RESERVA
        SET estado = 'CANCELADA'
        WHERE id = p_idReserva;

        -- Liberar todos los bloques asociados en DETALLE_RESERVA
        UPDATE BLOQUE_HORARIO bh
            INNER JOIN DETALLE_RESERVA dr ON bh.id = dr.idBloqueHorario
        SET bh.estado = 'DISPONIBLE'
        WHERE dr.idReserva = p_idReserva;

        SET p_exito = TRUE;
    ELSE
        SET p_exito = FALSE;
    END IF;
END //

CREATE PROCEDURE listarReservasCliente(
    IN p_idCliente INT
)
BEGIN
    SELECT
        r.id AS idReserva,
        r.estado,
        r.idCliente,
        r.idCancha,
        r.fechaCreacion,
        c.nombre AS nombreCancha,
        c.direccion,
        bh.dia,
        bh.horaInicio,
        bh.horaFin,
        bh.precio,
        p.id AS idPago,
        p.monto,
        p.metodoPago
    FROM RESERVA r
             INNER JOIN CANCHA c ON c.id = r.idCancha
             INNER JOIN DETALLE_RESERVA dr ON r.id = dr.idReserva
             INNER JOIN BLOQUE_HORARIO bh ON bh.id = dr.idBloqueHorario
             LEFT JOIN PAGO p ON p.idReserva = r.id
    WHERE r.idCliente = p_idCliente
    ORDER BY r.fechaCreacion DESC;
END //

CREATE PROCEDURE listarReservasPorCancha(
    IN p_idCancha INT
)
BEGIN
    SELECT id, estado, idCliente, idCancha, idPago, fechaCreacion, activo
    FROM RESERVA
    WHERE idCancha = p_idCancha AND activo = TRUE;
END //

DROP PROCEDURE IF EXISTS listarBloquesTodasReservas;

CREATE PROCEDURE listarBloquesTodasReservas()
BEGIN
    SELECT dr.idReserva, bh.id, bh.idCancha, bh.dia, bh.horaInicio, bh.horaFin, bh.precio, bh.estado, bh.activo
    FROM DETALLE_RESERVA dr
    INNER JOIN BLOQUE_HORARIO bh ON dr.idBloqueHorario = bh.id
    INNER JOIN RESERVA r ON dr.idReserva = r.id
    WHERE r.activo = TRUE;
END //

DELIMITER ;