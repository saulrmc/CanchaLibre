USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarBloqueHorario;
DROP PROCEDURE IF EXISTS listarBloquesPorCancha;
DROP PROCEDURE IF EXISTS eliminarBloqueHorario;
DROP PROCEDURE IF EXISTS modificarBloqueHorario;
DROP PROCEDURE IF EXISTS buscarBloqueHorarioPorId;
DROP PROCEDURE IF EXISTS listarBloquesHorario;

DROP PROCEDURE IF EXISTS insertarBloqueHorarioReserva;
DROP PROCEDURE IF EXISTS listarBloquesPorReserva;

DELIMITER //
CREATE PROCEDURE insertarBloqueHorario(
    IN p_idCancha INT,
    IN p_dia INT,
    IN p_horaInicio TIME,
    IN p_horaFin TIME,
    IN p_precio DECIMAL(10,2),
    IN p_estado VARCHAR(50),
    IN p_activo BOOLEAN,
    OUT p_id INT
)
BEGIN
INSERT INTO BLOQUE_HORARIO (
    idCancha,dia, horaInicio, horaFin, precio, estado, activo)
VALUES (
        p_idCancha,
        p_dia,
        p_horaInicio,
        p_horaFin,
        p_precio,
        p_estado,
        p_activo);
SET p_id = LAST_INSERT_ID();
END //

DELIMITER //

CREATE PROCEDURE listarBloquesPorCancha(
    IN p_idCancha INT
)
BEGIN
SELECT id, idCancha, dia, horaInicio, horaFin, precio, estado, activo
FROM BLOQUE_HORARIO
WHERE idCancha = p_idCancha
  AND activo = TRUE
ORDER BY dia, horaInicio;
END //

CREATE PROCEDURE eliminarBloqueHorario(
    IN p_id INT
)
BEGIN
UPDATE BLOQUE_HORARIO
SET activo = FALSE
WHERE id = p_id;
END //

CREATE PROCEDURE modificarBloqueHorario(
    IN p_dia INT,
    IN p_horaInicio TIME,
    IN p_horaFin TIME,
    IN p_precio DECIMAL(10,2),
    IN p_estado VARCHAR(50),
    IN p_activo BOOLEAN,
    IN p_id INT
)
BEGIN
UPDATE BLOQUE_HORARIO
SET dia = p_dia,
    horaInicio = p_horaInicio,
    horaFin = p_horaFin,
    precio = p_precio,
    estado = p_estado,
    activo = p_activo
WHERE id = p_id;
END //

CREATE PROCEDURE buscarBloqueHorarioPorId(
    IN p_id INT
)
BEGIN
SELECT id, idCancha, dia, horaInicio, horaFin, precio, estado, activo
FROM BLOQUE_HORARIO
WHERE id = p_id;
END //

CREATE PROCEDURE listarBloquesHorario()
BEGIN
SELECT id, idCancha, dia, horaInicio, horaFin, precio, estado, activo
FROM BLOQUE_HORARIO
WHERE activo = TRUE
ORDER BY idCancha, dia, horaInicio;
END //

CREATE PROCEDURE insertarBloqueHorarioReserva(
    IN p_idReserva INT,
    IN p_idBloqueHorario INT,
    IN p_precioHistorico DECIMAL(10,2),
    OUT p_id INT
)
BEGIN
    INSERT INTO DETALLE_RESERVA (
        idReserva, idBloqueHorario, precio_historico)
    VALUES (p_idReserva,
            p_idBloqueHorario,
            p_precioHistorico);
    SET p_id = LAST_INSERT_ID();
    UPDATE BLOQUE_HORARIO
    SET estado = 'RESERVADO'
    WHERE id = p_idBloqueHorario;
END //

DELIMITER //

CREATE PROCEDURE listarBloquesPorReserva(
    IN p_idReserva INT
)
BEGIN
    SELECT bh.id, bh.idCancha, bh.dia, bh.horaInicio, bh.horaFin,
           bh.precio, bh.estado, bh.activo
    FROM BLOQUE_HORARIO bh
    INNER JOIN DETALLE_RESERVA dr ON bh.id = dr.idBloqueHorario
    WHERE dr.idReserva = p_idReserva;
END //
