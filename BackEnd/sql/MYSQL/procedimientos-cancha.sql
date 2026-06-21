USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarCancha;
DROP PROCEDURE IF EXISTS insertarBloqueHorario;
DROP PROCEDURE IF EXISTS modificarBloqueHorario;
DROP PROCEDURE IF EXISTS listarCanchasDisponibles;
DROP PROCEDURE IF EXISTS listarCanchasAlternativas;

DELIMITER //

-- RF04: Registrar cancha nueva por parte de un Propietario.
-- Los deportes y etiquetas se insertan por separado en sus
-- tablas intermedias desde la capa de negocio en Java,
-- ya que son listas de tamaño variable.
CREATE PROCEDURE insertarCancha(
    IN  p_nombre        VARCHAR(150),
    IN  p_descripcion   TEXT,
    IN  p_direccion     VARCHAR(255),
    IN  p_imagenUrl     VARCHAR(255),
    IN  p_precioBase    DECIMAL(10,2),
    IN  p_idPropietario INT,
    OUT p_id            INT)
BEGIN
    INSERT INTO CANCHA (nombre, descripcion, direccion, imagenUrl, precioBase, promedioCalificacion, idPropietario)
    VALUES (p_nombre, p_descripcion, p_direccion, p_imagenUrl, p_precioBase, 0.00, p_idPropietario);
    SET p_id = LAST_INSERT_ID();
END //

-- RF05: Insertar bloque horario para una cancha.
-- precio NULL indica que aún no se ha configurado precio para el bloque.
-- estado por defecto DISPONIBLE.
CREATE PROCEDURE insertarBloqueHorario(
    IN  p_dia        ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
    IN  p_horaInicio TIME,
    IN  p_horaFin    TIME,
    IN  p_precio     DECIMAL(10,2),
    IN  p_estado     ENUM('DISPONIBLE','RESERVADO','BLOQUEADO','MANTENIMIENTO'),
    IN  p_idCancha   INT,
    OUT p_id         INT)
BEGIN
    INSERT INTO BLOQUE_HORARIO (dia, horaInicio, horaFin, precio, estado, idCancha)
    VALUES (p_dia, p_horaInicio, p_horaFin, p_precio, p_estado, p_idCancha);
    SET p_id = LAST_INSERT_ID();
END //

-- RF05: Modificar bloque horario existente.
-- Permite al propietario cambiar precio, horario o estado
-- (ej. bloquear por mantenimiento).
CREATE PROCEDURE modificarBloqueHorario(
    IN p_id         INT,
    IN p_dia        ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
    IN p_horaInicio TIME,
    IN p_horaFin    TIME,
    IN p_precio     DECIMAL(10,2),
    IN p_estado     ENUM('DISPONIBLE','RESERVADO','BLOQUEADO','MANTENIMIENTO'))
BEGIN
    UPDATE BLOQUE_HORARIO
    SET dia        = p_dia,
        horaInicio = p_horaInicio,
        horaFin    = p_horaFin,
        precio     = p_precio,
        estado     = p_estado
    WHERE id = p_id;
END //

-- RF02: Listar canchas disponibles con sus datos principales.
-- Solo muestra canchas activas que tengan al menos un bloque
-- con estado DISPONIBLE y precio configurado (no NULL).
-- El frontend puede filtrar adicionalmente por deporte, fecha u horario.
CREATE PROCEDURE listarCanchasDisponibles()
BEGIN
    SELECT DISTINCT
        c.id,
        c.nombre,
        c.descripcion,
        c.direccion,
        c.imagenUrl,
        c.precioBase,
        c.promedioCalificacion
    FROM CANCHA c
    INNER JOIN BLOQUE_HORARIO bh ON bh.idCancha = c.id
    WHERE c.activo = TRUE
      AND bh.estado   = 'DISPONIBLE'
      AND bh.precio   IS NOT NULL;
END //

-- RF12: Listar canchas alternativas cuando no hay disponibilidad
-- para la cancha seleccionada en el rango horario solicitado.
-- Busca otras canchas activas con el mismo deporte que tengan
-- bloques disponibles que cubran el rango solicitado.
CREATE PROCEDURE listarCanchasAlternativas(
    IN p_idCancha    INT,
    IN p_horaInicio  TIME,
    IN p_horaFin     TIME,
    IN p_dia         ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'))
BEGIN
    SELECT DISTINCT
        c.id,
        c.nombre,
        c.descripcion,
        c.direccion,
        c.imagenUrl,
        c.precioBase,
        c.promedioCalificacion
    FROM CANCHA c
    INNER JOIN BLOQUE_HORARIO bh ON bh.idCancha = c.id
    INNER JOIN CANCHA_DEPORTES cd ON cd.idCancha = c.id
    WHERE c.activo    = TRUE
      AND c.id       != p_idCancha
      AND bh.dia      = p_dia
      AND bh.horaInicio <= p_horaInicio
      AND bh.horaFin  >= p_horaFin
      AND bh.estado   = 'DISPONIBLE'
      AND bh.precio   IS NOT NULL
      AND cd.deporte IN (
          SELECT deporte FROM CANCHA_DEPORTES WHERE idCancha = p_idCancha
      );
END //

DELIMITER ;
