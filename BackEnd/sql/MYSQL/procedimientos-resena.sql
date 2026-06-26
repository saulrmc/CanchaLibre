USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarResena;
DROP PROCEDURE IF EXISTS modificarResena;
DROP PROCEDURE IF EXISTS eliminarResena;
DROP PROCEDURE IF EXISTS buscarResenaPorId;
DROP PROCEDURE IF EXISTS listarResenas;
DROP PROCEDURE IF EXISTS listarResenasPorCancha;
DROP PROCEDURE IF EXISTS listarResenasPorCliente;
DELIMITER //

-- RF08: Insertar reseña para una cancha.
-- Valida que el cliente tenga una reserva COMPLETADA para esa cancha.
-- Tras insertar recalcula promedioCalificacion en Cancha.
CREATE PROCEDURE insertarResena(
    IN p_descripcion VARCHAR(120),
    IN p_calificacion DECIMAL(2,1),
    IN p_fechaPublicacion DATETIME,
    IN p_idReserva INT,
    OUT p_id INT
)
BEGIN
DECLARE v_idCancha INT;

INSERT INTO RESENA (
    descripcion, calificacion, fechaPublicacion, idReserva)
VALUES (
           p_descripcion,
           p_calificacion,
           p_fechaPublicacion,
           p_idReserva);
SET p_id = LAST_INSERT_ID();

SELECT idCancha
INTO v_idCancha
FROM RESERVA
WHERE id = p_idReserva;

UPDATE CANCHA
SET promedioCalificacion = (
    SELECT AVG(rs.calificacion)
    FROM RESENA rs
             INNER JOIN RESERVA r ON rs.idReserva = r.id
    WHERE r.idCancha = v_idCancha
)
WHERE id = v_idCancha;
END //

CREATE PROCEDURE modificarResena(
    IN p_descripcion VARCHAR(120),
    IN p_calificacion DECIMAL(2,1),
    IN p_fechaPublicacion DATETIME,
    IN p_id INT
)
BEGIN
DECLARE v_idCancha INT;

UPDATE RESENA
SET descripcion = p_descripcion,
    calificacion = p_calificacion,
    fechaPublicacion = p_fechaPublicacion
WHERE id = p_id;

SELECT r.idCancha
INTO v_idCancha
FROM RESENA rs
         INNER JOIN RESERVA r ON rs.idReserva = r.id
WHERE rs.id = p_id;

UPDATE CANCHA
SET promedioCalificacion = (
    SELECT AVG(rs.calificacion)
    FROM RESENA rs
             INNER JOIN RESERVA r ON rs.idReserva = r.id
    WHERE r.idCancha = v_idCancha
)
WHERE id = v_idCancha;
END //

CREATE PROCEDURE eliminarResena(
    IN p_id INT
)
BEGIN
DECLARE v_idCancha INT;

SELECT r.idCancha
INTO v_idCancha
FROM RESENA rs
         INNER JOIN RESERVA r ON rs.idReserva = r.id
WHERE rs.id = p_id;

DELETE FROM RESENA
WHERE id = p_id;

UPDATE CANCHA
SET promedioCalificacion = (
    SELECT COALESCE(AVG(rs.calificacion), 0.00)
    FROM RESENA rs
             INNER JOIN RESERVA r ON rs.idReserva = r.id
    WHERE r.idCancha = v_idCancha
)
WHERE id = v_idCancha;
END //

CREATE PROCEDURE buscarResenaPorId(
    IN p_id INT
)
BEGIN
SELECT id, descripcion, calificacion, fechaPublicacion, idReserva
FROM RESENA
WHERE id = p_id;
END //

CREATE PROCEDURE listarResenas()
BEGIN
SELECT id, descripcion, calificacion, fechaPublicacion, idReserva
FROM RESENA;
END //

CREATE PROCEDURE listarResenasPorCancha(
    IN p_idCancha INT
)
BEGIN
SELECT
    rs.id,
    rs.descripcion,
    rs.calificacion,
    rs.fechaPublicacion,
    rs.idReserva
FROM RESENA rs
         INNER JOIN RESERVA r ON rs.idReserva = r.id
WHERE r.idCancha = p_idCancha
ORDER BY rs.fechaPublicacion DESC;
END //

CREATE PROCEDURE listarResenasPorCliente(
    IN p_idCliente INT
)
BEGIN
SELECT
    rs.id,
    rs.descripcion,
    rs.calificacion,
    rs.fechaPublicacion,
    rs.idReserva
FROM RESENA rs
         INNER JOIN RESERVA r ON rs.idReserva = r.id
WHERE r.idCliente = p_idCliente
ORDER BY rs.fechaPublicacion DESC;
END //

DELIMITER ;