USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarResena;
DROP PROCEDURE IF EXISTS dbo.modificarResena;
DROP PROCEDURE IF EXISTS dbo.eliminarResena;
DROP PROCEDURE IF EXISTS dbo.buscarResenaPorId;
DROP PROCEDURE IF EXISTS dbo.listarResenas;
DROP PROCEDURE IF EXISTS dbo.listarResenasPorCancha;
DROP PROCEDURE IF EXISTS dbo.listarResenasPorCliente;
GO

CREATE PROCEDURE dbo.insertarResena
    @p_descripcion VARCHAR(120),
    @p_calificacion DECIMAL(2,1),
    @p_fechaPublicacion DATETIME2,
    @p_idReserva INT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @v_idCancha INT;

    INSERT INTO dbo.RESENA (
        descripcion,
        calificacion,
        fechaPublicacion,
        idReserva
    )
    VALUES (
        @p_descripcion,
        @p_calificacion,
        @p_fechaPublicacion,
        @p_idReserva
    );

    SET @p_id = SCOPE_IDENTITY();

    SELECT @v_idCancha = idCancha
    FROM dbo.RESERVA
    WHERE id = @p_idReserva;

    UPDATE dbo.CANCHA
    SET promedioCalificacion = (
        SELECT AVG(rs.calificacion)
        FROM dbo.RESENA rs
        INNER JOIN dbo.RESERVA r 
            ON rs.idReserva = r.id
        WHERE r.idCancha = @v_idCancha
    )
    WHERE id = @v_idCancha;
END;
GO

CREATE PROCEDURE dbo.modificarResena
    @p_descripcion VARCHAR(120),
    @p_calificacion DECIMAL(2,1),
    @p_fechaPublicacion DATETIME2,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @v_idCancha INT;

    UPDATE dbo.RESENA
    SET
        descripcion = @p_descripcion,
        calificacion = @p_calificacion,
        fechaPublicacion = @p_fechaPublicacion
    WHERE id = @p_id;

    SELECT @v_idCancha = r.idCancha
    FROM dbo.RESENA rs
    INNER JOIN dbo.RESERVA r 
        ON rs.idReserva = r.id
    WHERE rs.id = @p_id;

    UPDATE dbo.CANCHA
    SET promedioCalificacion = (
        SELECT AVG(rs.calificacion)
        FROM dbo.RESENA rs
        INNER JOIN dbo.RESERVA r 
            ON rs.idReserva = r.id
        WHERE r.idCancha = @v_idCancha
    )
    WHERE id = @v_idCancha;
END;
GO

CREATE PROCEDURE dbo.eliminarResena
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @v_idCancha INT;

    SELECT @v_idCancha = r.idCancha
    FROM dbo.RESENA rs
    INNER JOIN dbo.RESERVA r 
        ON rs.idReserva = r.id
    WHERE rs.id = @p_id;

    DELETE FROM dbo.RESENA
    WHERE id = @p_id;

    UPDATE dbo.CANCHA
    SET promedioCalificacion = (
        SELECT COALESCE(AVG(rs.calificacion), 0.00)
        FROM dbo.RESENA rs
        INNER JOIN dbo.RESERVA r 
            ON rs.idReserva = r.id
        WHERE r.idCancha = @v_idCancha
    )
    WHERE id = @v_idCancha;
END;
GO

CREATE PROCEDURE dbo.buscarResenaPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        descripcion,
        calificacion,
        fechaPublicacion,
        idReserva
    FROM dbo.RESENA
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarResenas
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        descripcion,
        calificacion,
        fechaPublicacion,
        idReserva
    FROM dbo.RESENA;
END;
GO

CREATE PROCEDURE dbo.listarResenasPorCancha
    @p_idCancha INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        rs.id,
        rs.descripcion,
        rs.calificacion,
        rs.fechaPublicacion,
        rs.idReserva
    FROM dbo.RESENA rs
    INNER JOIN dbo.RESERVA r 
        ON rs.idReserva = r.id
    WHERE r.idCancha = @p_idCancha
    ORDER BY rs.fechaPublicacion DESC;
END;
GO

CREATE PROCEDURE dbo.listarResenasPorCliente
    @p_idCliente INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        rs.id,
        rs.descripcion,
        rs.calificacion,
        rs.fechaPublicacion,
        rs.idReserva
    FROM dbo.RESENA rs
    INNER JOIN dbo.RESERVA r 
        ON rs.idReserva = r.id
    WHERE r.idCliente = @p_idCliente
    ORDER BY rs.fechaPublicacion DESC;
END;
GO