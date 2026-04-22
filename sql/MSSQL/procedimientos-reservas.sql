USE canchalibre;
GO

IF OBJECT_ID('dbo.insertarReserva', 'P') IS NOT NULL DROP PROCEDURE dbo.insertarReserva;
GO
IF OBJECT_ID('dbo.cancelarReserva', 'P') IS NOT NULL DROP PROCEDURE dbo.cancelarReserva;
GO

-- RF03: Insertar nueva reserva
CREATE PROCEDURE dbo.insertarReserva
    @p_fechaHora DATETIME,
    @p_duracion INT,
    @p_idCancha INT,
    @p_idCliente INT,
    @p_id INT OUTPUT
AS
BEGIN
    INSERT INTO Reserva (fechaHora, duracion, estado, idCancha, idCliente)
    VALUES (@p_fechaHora, @p_duracion, 'EnEspera', @p_idCancha, @p_idCliente);
    SET @p_id = SCOPE_IDENTITY();
END
GO

-- RF10: Cancelar reserva con validación de tiempo (DATEDIFF)
CREATE PROCEDURE dbo.cancelarReserva
    @p_idReserva INT,
    @p_exito BIT OUTPUT
AS
BEGIN
    DECLARE @v_fecha DATETIME;
    SELECT @v_fecha = fechaHora FROM Reserva WHERE idReserva = @p_idReserva;

    -- Validar si faltan 2 horas o más
    IF DATEDIFF(HOUR, GETDATE(), @v_fecha) >= 2
    BEGIN
        UPDATE Reserva SET estado = 'Cancelado' WHERE idReserva = @p_idReserva;
        SET @p_exito = 1;
    END
    ELSE
    BEGIN
        SET @p_exito = 0;
    END
END
GO