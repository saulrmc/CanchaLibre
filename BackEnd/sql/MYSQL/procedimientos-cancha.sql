USE CanchaLibre;

DROP PROCEDURE IF EXISTS listarCanchasDisponibles;
DROP PROCEDURE IF EXISTS listarCanchasAlternativas;

DROP PROCEDURE IF EXISTS insertarCancha;
DROP PROCEDURE IF EXISTS modificarCancha;
DROP PROCEDURE IF EXISTS eliminarCancha;
DROP PROCEDURE IF EXISTS buscarCanchaPorId;
DROP PROCEDURE IF EXISTS listarCanchas;
DROP PROCEDURE IF EXISTS listarDeportesCancha;
DROP PROCEDURE IF EXISTS listarEtiquetasCancha;
DROP PROCEDURE IF EXISTS listarCanchasPorCuenta;

DROP PROCEDURE IF EXISTS listarCanchasPorDistrito;
DROP PROCEDURE IF EXISTS listarDeportesTodasCanchas;
DROP PROCEDURE IF EXISTS listarEtiquetasTodasCanchas;
DROP PROCEDURE IF EXISTS listarBloquesTodasCanchas;

DELIMITER //

CREATE PROCEDURE insertarCancha(
    IN p_nombre VARCHAR(150),
    IN p_descripcion TEXT,
    IN p_direccion VARCHAR(255),
    IN p_imagenUrl VARCHAR(255),
    IN p_idPropietario INT,
    IN p_precioBase DECIMAL(10,2),
    IN p_activo BOOLEAN,
    OUT p_id INT
)
BEGIN
INSERT INTO CANCHA (
    nombre, descripcion, direccion, imagenUrl, idPropietario, precioBase, promedioCalificacion, activo)
VALUES (
           p_nombre,
           p_descripcion,
           p_direccion,
           p_imagenUrl,
           p_idPropietario,
           p_precioBase,
           0.00,
           p_activo);
SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE modificarCancha(
    IN p_nombre VARCHAR(150),
    IN p_descripcion TEXT,
    IN p_direccion VARCHAR(255),
    IN p_imagenUrl VARCHAR(255),
    IN p_idPropietario INT,
    IN p_precioBase DECIMAL(10,2),
    IN p_activo BOOLEAN,
    IN p_id INT
)
BEGIN
UPDATE CANCHA
SET nombre = p_nombre,
    descripcion = p_descripcion,
    direccion = p_direccion,
    imagenUrl = p_imagenUrl,
    idPropietario = p_idPropietario,
    precioBase = p_precioBase,
    activo = p_activo
WHERE id = p_id;
END //

CREATE PROCEDURE eliminarCancha(
    IN p_id INT
)
BEGIN
UPDATE CANCHA
SET activo = FALSE
WHERE id = p_id;
END //

CREATE PROCEDURE buscarCanchaPorId(
    IN p_id INT
)
BEGIN
SELECT
    c.id, c.nombre, c.descripcion, c.direccion, c.imagenUrl, c.idPropietario, c.precioBase, c.promedioCalificacion, c.activo,
    p.id AS prop_id, p.nombres AS prop_nombres, p.correo AS prop_correo, p.telefono AS prop_telefono,
    p.calificacion AS prop_calificacion, p.RUC AS prop_ruc, p.saldo AS prop_saldo, p.activo AS prop_activo,
    cu.id AS cuenta_id, cu.userName AS cuenta_userName
FROM CANCHA c
LEFT JOIN PROPIETARIO p ON c.idPropietario = p.id
LEFT JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
WHERE c.id = p_id;
END //

CREATE PROCEDURE listarCanchas()
BEGIN
SELECT
    c.id, c.nombre, c.descripcion, c.direccion, c.imagenUrl, c.idPropietario, c.precioBase, c.promedioCalificacion, c.activo,
    p.id AS prop_id, p.nombres AS prop_nombres, p.correo AS prop_correo, p.telefono AS prop_telefono,
    p.calificacion AS prop_calificacion, p.RUC AS prop_ruc, p.saldo AS prop_saldo, p.activo AS prop_activo,
    cu.id AS cuenta_id, cu.userName AS cuenta_userName
FROM CANCHA c
LEFT JOIN PROPIETARIO p ON c.idPropietario = p.id
LEFT JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
WHERE c.activo = TRUE;
END //

CREATE PROCEDURE listarDeportesCancha(
    IN p_id INT
)
BEGIN
SELECT deporte
FROM CANCHA_DEPORTE
WHERE idCancha = p_id;
END //

CREATE PROCEDURE listarEtiquetasCancha(
    IN p_id INT
)
BEGIN
SELECT etiqueta
FROM CANCHA_ETIQUETA
WHERE idCancha = p_id;
END //

CREATE PROCEDURE listarCanchasPorCuenta(
    IN p_cuenta VARCHAR(50)
)
BEGIN
SELECT
    c.id,
    c.nombre,
    c.descripcion,
    c.direccion,
    c.imagenUrl,
    c.idPropietario,
    c.precioBase,
    c.promedioCalificacion,
    c.activo
FROM CANCHA c
         INNER JOIN PROPIETARIO p ON c.idPropietario = p.id
         INNER JOIN CUENTA_USUARIO cu ON p.idCuentaUsuario = cu.id
WHERE cu.userName = p_cuenta
  AND c.activo = TRUE;
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
    FROM CANCHA c
    INNER JOIN BLOQUE_HORARIO bh ON bh.idCancha = c.id
    WHERE c.activo = TRUE
      AND bh.activo = TRUE
      AND bh.estado = 'DISPONIBLE';
END //

CREATE PROCEDURE listarCanchasAlternativas(
    IN p_idCancha   INT,
    IN p_horaInicio TIME,
    IN p_horaFin    TIME,
    IN p_dia        INT
)
BEGIN
    SELECT DISTINCT
        c.id,
        c.nombre,
        c.descripcion,
        c.direccion,
        c.imagenUrl,
        c.idPropietario,
        c.precioBase,
        c.promedioCalificacion,
        c.activo
    FROM CANCHA c
             INNER JOIN BLOQUE_HORARIO bh ON bh.idCancha = c.id
             INNER JOIN CANCHA_DEPORTE cd ON cd.idCancha = c.id
    WHERE c.id != p_idCancha
      AND c.activo = TRUE
      AND bh.activo = TRUE
      AND bh.dia = p_dia
      AND bh.estado = 'DISPONIBLE'
      AND bh.horaInicio < p_horaFin
      AND bh.horaFin > p_horaInicio
      AND cd.deporte IN (
        SELECT cd2.deporte
        FROM CANCHA_DEPORTE cd2
        WHERE cd2.idCancha = p_idCancha
    );
END //

CREATE PROCEDURE listarCanchasPorDistrito(
    IN p_distrito VARCHAR(255)
)
BEGIN
    SELECT * FROM CANCHA
    WHERE activo = TRUE
      AND direccion LIKE CONCAT('% - ', p_distrito);
END //

CREATE PROCEDURE listarDeportesTodasCanchas()
BEGIN
    SELECT cd.idCancha, cd.deporte
    FROM CANCHA_DEPORTE cd
    INNER JOIN CANCHA c ON c.id = cd.idCancha
    WHERE c.activo = TRUE;
END //

CREATE PROCEDURE listarEtiquetasTodasCanchas()
BEGIN
    SELECT ce.idCancha, ce.etiqueta
    FROM CANCHA_ETIQUETA ce
    INNER JOIN CANCHA c ON c.id = ce.idCancha
    WHERE c.activo = TRUE;
END //

CREATE PROCEDURE listarBloquesTodasCanchas()
BEGIN
    SELECT bh.id, bh.idCancha, bh.dia, bh.horaInicio, bh.horaFin, bh.precio, bh.estado, bh.activo
    FROM BLOQUE_HORARIO bh
    INNER JOIN CANCHA c ON c.id = bh.idCancha
    WHERE c.activo = TRUE AND bh.activo = TRUE;
END //