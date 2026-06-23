USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarCancha;
DROP PROCEDURE IF EXISTS insertarBloqueHorario;
DROP PROCEDURE IF EXISTS modificarBloqueHorario;
DROP PROCEDURE IF EXISTS listarCanchasDisponibles;
DROP PROCEDURE IF EXISTS listarCanchasAlternativas;

DELIMITER //

CREATE PROCEDURE insertarCancha(
    IN  p_nombre        VARCHAR(150),
    IN  p_descripcion   TEXT,
    IN  p_direccion     VARCHAR(255),
    IN  p_imagenUrl     VARCHAR(255),
    IN  p_precioBase    DECIMAL(10,2),
    IN  p_idPropietario INT,
    OUT p_id            INT
)
BEGIN
    INSERT INTO Cancha (
        activo, nombre, descripcion, direccion, imagenUrl,
        precioBase, promedioCalificacion, idPropietario
    )
    VALUES (
        TRUE, p_nombre, p_descripcion, p_direccion, p_imagenUrl,
        p_precioBase, 0.00, p_idPropietario
    );

    SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE insertarBloqueHorario(
    IN  p_dia        ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
    IN  p_horaInicio TIME,
    IN  p_horaFin    TIME,
    IN  p_precio     DECIMAL(10,2),
    IN  p_estado     ENUM('DISPONIBLE','RESERVADO','BLOQUEADO'),
    IN  p_idCancha   INT,
    OUT p_id         INT
)
BEGIN
    INSERT INTO BloqueHorario (
        activo, dia, horaInicio, horaFin, precio, estado, idCancha
    )
    VALUES (
        TRUE, p_dia, p_horaInicio, p_horaFin, p_precio, p_estado, p_idCancha
    );

    SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE modificarBloqueHorario(
    IN p_id         INT,
    IN p_dia        ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
    IN p_horaInicio TIME,
    IN p_horaFin    TIME,
    IN p_precio     DECIMAL(10,2),
    IN p_estado     ENUM('DISPONIBLE','RESERVADO','BLOQUEADO','MANTENIMIENTO')
)
BEGIN
    UPDATE BloqueHorario
    SET dia        = p_dia,
        horaInicio = p_horaInicio,
        horaFin    = p_horaFin,
        precio     = p_precio,
        estado     = p_estado
    WHERE id = p_id;
END //

CREATE PROCEDURE listarCanchasDisponibles()
BEGIN
    SELECT DISTINCT
        c.id,
        c.activo,
        c.nombre,
        c.descripcion,
        c.direccion,
        c.imagenUrl,
        c.precioBase,
        c.promedioCalificacion,
        c.idPropietario
    FROM Cancha c
    INNER JOIN BloqueHorario bh ON bh.idCancha = c.id
    WHERE c.activo = TRUE
      AND bh.activo = TRUE
      AND bh.estado = 'DISPONIBLE';
END //

CREATE PROCEDURE listarCanchasAlternativas(
    IN p_idCancha    INT,
    IN p_horaInicio  TIME,
    IN p_horaFin     TIME,
    IN p_dia         ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO')
)
BEGIN
    SELECT DISTINCT
        c.id,
        c.activo,
        c.nombre,
        c.descripcion,
        c.direccion,
        c.imagenUrl,
        c.precioBase,
        c.promedioCalificacion,
        c.idPropietario
    FROM Cancha c
    INNER JOIN BloqueHorario bh ON bh.idCancha = c.id
    INNER JOIN Cancha_Deporte cd ON cd.idCancha = c.id
    WHERE c.activo = TRUE
      AND bh.activo = TRUE
      AND c.id != p_idCancha
      AND bh.dia = p_dia
      AND bh.horaInicio <= p_horaInicio
      AND bh.horaFin >= p_horaFin
      AND bh.estado = 'DISPONIBLE'
      AND cd.deporte IN (
          SELECT deporte
          FROM Cancha_Deporte
          WHERE idCancha = p_idCancha
      );
END //

DELIMITER ;
