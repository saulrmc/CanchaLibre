USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarBloqueHorario;
DROP PROCEDURE IF EXISTS dbo.listarBloquesPorCancha;
DROP PROCEDURE IF EXISTS dbo.eliminarBloqueHorario;
DROP PROCEDURE IF EXISTS dbo.modificarBloqueHorario;
DROP PROCEDURE IF EXISTS dbo.buscarBloqueHorarioPorId;
DROP PROCEDURE IF EXISTS dbo.listarBloquesHorario;
DROP PROCEDURE IF EXISTS dbo.insertarBloqueHorarioReserva;
GO

CREATE PROCEDURE dbo.insertarBloqueHorario
    @p_idCancha INT,
    @p_dia INT,
    @p_horaInicio TIME,
    @p_horaFin TIME,
    @p_precio DECIMAL(10,2),
    @p_estado VARCHAR(50),
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.BLOQUE_HORARIO (
        idCancha,
        dia,
        horaInicio,
        horaFin,
        precio,
        estado,
        activo
    )
    VALUES (
        @p_idCancha,
        @p_dia,
        @p_horaInicio,
        @p_horaFin,
        @p_precio,
        @p_estado,
        @p_activo
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.listarBloquesPorCancha
    @p_idCancha INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        id,
        idCancha,
        dia,
        horaInicio,
        horaFin,
        precio,
        estado,
        activo
    FROM dbo.BLOQUE_HORARIO
    WHERE idCancha = @p_idCancha
      AND activo = 1
    ORDER BY dia, horaInicio;
END;
GO

CREATE PROCEDURE dbo.eliminarBloqueHorario
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.BLOQUE_HORARIO
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.modificarBloqueHorario
    @p_dia INT,
    @p_horaInicio TIME,
    @p_horaFin TIME,
    @p_precio DECIMAL(10,2),
    @p_estado VARCHAR(50),
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.BLOQUE_HORARIO
    SET 
        dia = @p_dia,
        horaInicio = @p_horaInicio,
        horaFin = @p_horaFin,
        precio = @p_precio,
        estado = @p_estado,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarBloqueHorarioPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        id,
        idCancha,
        dia,
        horaInicio,
        horaFin,
        precio,
        estado,
        activo
    FROM dbo.BLOQUE_HORARIO
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarBloquesHorario
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        id,
        idCancha,
        dia,
        horaInicio,
        horaFin,
        precio,
        estado,
        activo
    FROM dbo.BLOQUE_HORARIO
    WHERE activo = 1
    ORDER BY idCancha, dia, horaInicio;
END;
GO

CREATE PROCEDURE dbo.insertarBloqueHorarioReserva
    @p_idReserva INT,
    @p_idBloqueHorario INT,
    @p_precioHistorico DECIMAL(10,2),
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.DETALLE_RESERVA (
        idReserva,
        idBloqueHorario,
        precio_historico
    )
    VALUES (
        @p_idReserva,
        @p_idBloqueHorario,
        @p_precioHistorico
    );

    SET @p_id = SCOPE_IDENTITY();

    UPDATE dbo.BLOQUE_HORARIO
    SET estado = 'RESERVADO'
    WHERE id = @p_idBloqueHorario;
END;
GO