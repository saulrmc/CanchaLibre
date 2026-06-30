USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.listarCanchasDisponibles;
DROP PROCEDURE IF EXISTS dbo.listarCanchasAlternativas;
DROP PROCEDURE IF EXISTS dbo.insertarCancha;
DROP PROCEDURE IF EXISTS dbo.modificarCancha;
DROP PROCEDURE IF EXISTS dbo.eliminarCancha;
DROP PROCEDURE IF EXISTS dbo.buscarCanchaPorId;
DROP PROCEDURE IF EXISTS dbo.listarCanchas;
DROP PROCEDURE IF EXISTS dbo.listarDeportesCancha;
DROP PROCEDURE IF EXISTS dbo.listarEtiquetasCancha;
DROP PROCEDURE IF EXISTS dbo.listarCanchasPorCuenta;
GO

CREATE PROCEDURE dbo.insertarCancha
    @p_nombre VARCHAR(150),
    @p_descripcion VARCHAR(MAX),
    @p_direccion VARCHAR(255),
    @p_imagenUrl VARCHAR(MAX),
    @p_idPropietario INT,
    @p_precioBase DECIMAL(10,2),
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.CANCHA (
        nombre,
        descripcion,
        direccion,
        imagenUrl,
        idPropietario,
        precioBase,
        promedioCalificacion,
        activo
    )
    VALUES (
        @p_nombre,
        @p_descripcion,
        @p_direccion,
        @p_imagenUrl,
        @p_idPropietario,
        @p_precioBase,
        0.00,
        @p_activo
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.modificarCancha
    @p_nombre VARCHAR(150),
    @p_descripcion VARCHAR(MAX),
    @p_direccion VARCHAR(255),
    @p_imagenUrl VARCHAR(MAX),
    @p_idPropietario INT,
    @p_precioBase DECIMAL(10,2),
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CANCHA
    SET
        nombre = @p_nombre,
        descripcion = @p_descripcion,
        direccion = @p_direccion,
        imagenUrl = @p_imagenUrl,
        idPropietario = @p_idPropietario,
        precioBase = @p_precioBase,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarCancha
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.CANCHA
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarCanchaPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        nombre,
        descripcion,
        direccion,
        imagenUrl,
        idPropietario,
        precioBase,
        promedioCalificacion,
        activo
    FROM dbo.CANCHA
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarCanchas
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        nombre,
        descripcion,
        direccion,
        imagenUrl,
        idPropietario,
        precioBase,
        promedioCalificacion,
        activo
    FROM dbo.CANCHA
    WHERE activo = 1;
END;
GO

CREATE PROCEDURE dbo.listarDeportesCancha
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT deporte
    FROM dbo.CANCHA_DEPORTE
    WHERE idCancha = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarEtiquetasCancha
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT etiqueta
    FROM dbo.CANCHA_ETIQUETA
    WHERE idCancha = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarCanchasPorCuenta
    @p_cuenta VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

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
    FROM dbo.CANCHA c
    INNER JOIN dbo.PROPIETARIO p
        ON c.idPropietario = p.id
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON p.idCuentaUsuario = cu.id
    WHERE cu.userName = @p_cuenta
      AND c.activo = 1;
END;
GO

CREATE PROCEDURE dbo.listarCanchasDisponibles
AS
BEGIN
    SET NOCOUNT ON;

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
    FROM dbo.CANCHA c
    INNER JOIN dbo.BLOQUE_HORARIO bh
        ON bh.idCancha = c.id
    WHERE c.activo = 1
      AND bh.activo = 1
      AND bh.estado = 'DISPONIBLE';
END;
GO

CREATE PROCEDURE dbo.listarCanchasAlternativas
    @p_idCancha INT,
    @p_horaInicio TIME,
    @p_horaFin TIME,
    @p_dia INT
AS
BEGIN
    SET NOCOUNT ON;

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
    FROM dbo.CANCHA c
    INNER JOIN dbo.BLOQUE_HORARIO bh
        ON bh.idCancha = c.id
    INNER JOIN dbo.CANCHA_DEPORTE cd
        ON cd.idCancha = c.id
    WHERE c.id <> @p_idCancha
      AND c.activo = 1
      AND bh.activo = 1
      AND bh.dia = @p_dia
      AND bh.estado = 'DISPONIBLE'
      AND bh.horaInicio < @p_horaFin
      AND bh.horaFin > @p_horaInicio
      AND cd.deporte IN (
            SELECT cd2.deporte
            FROM dbo.CANCHA_DEPORTE cd2
            WHERE cd2.idCancha = @p_idCancha
      );
END;
GO