USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarReserva;
DROP PROCEDURE IF EXISTS dbo.modificarReserva;
DROP PROCEDURE IF EXISTS dbo.eliminarReserva;
DROP PROCEDURE IF EXISTS dbo.cancelarReserva;
DROP PROCEDURE IF EXISTS dbo.buscarReservaPorId;
DROP PROCEDURE IF EXISTS dbo.listarReservas;
DROP PROCEDURE IF EXISTS dbo.listarReservasPorCuenta;
DROP PROCEDURE IF EXISTS dbo.listarReservasPorId;
DROP PROCEDURE IF EXISTS dbo.listarReservasCliente;
GO

CREATE PROCEDURE dbo.insertarReserva
    @p_estado VARCHAR(20),
    @p_idCliente INT,
    @p_idCancha INT,
    @p_fechaCreacion DATETIME2,
    @p_idPago INT,
    @p_activo BIT,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.RESERVA (
        idCliente,
        idCancha,
        estado,
        fechaCreacion,
        idPago,
        activo
    )
    VALUES (
        @p_idCliente,
        @p_idCancha,
        @p_estado,
        @p_fechaCreacion,
        @p_idPago,
        @p_activo
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.modificarReserva
    @p_estado VARCHAR(20),
    @p_idPago INT,
    @p_activo BIT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.RESERVA
    SET
        estado = @p_estado,
        idPago = @p_idPago,
        activo = @p_activo
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarReserva
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.RESERVA
    SET activo = 0
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarReservaPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        estado,
        idCliente,
        idCancha,
        idPago,
        fechaCreacion,
        activo
    FROM dbo.RESERVA
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarReservas
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id,
        estado,
        idCliente,
        idCancha,
        idPago,
        fechaCreacion,
        activo
    FROM dbo.RESERVA
    WHERE activo = 1;
END;
GO

CREATE PROCEDURE dbo.listarReservasPorCuenta
    @p_cuenta VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;

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
    FROM dbo.RESERVA r
    INNER JOIN dbo.CLIENTE c
        ON r.idCliente = c.id
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON c.idCuentaUsuario = cu.id
    INNER JOIN dbo.CANCHA ca
        ON r.idCancha = ca.id
    LEFT JOIN dbo.PAGO p
        ON p.idReserva = r.id
    WHERE cu.userName = @p_cuenta;
END;
GO

CREATE PROCEDURE dbo.listarReservasPorId
    @p_idCliente INT
AS
BEGIN
    SET NOCOUNT ON;

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
    FROM dbo.RESERVA r
    INNER JOIN dbo.CLIENTE c
        ON r.idCliente = c.id
    INNER JOIN dbo.CUENTA_USUARIO cu
        ON c.idCuentaUsuario = cu.id
    INNER JOIN dbo.CANCHA ca
        ON r.idCancha = ca.id
    LEFT JOIN dbo.PAGO p
        ON p.idReserva = r.id
    WHERE r.idCliente = @p_idCliente;
END;
GO

CREATE PROCEDURE dbo.cancelarReserva
    @p_idReserva INT,
    @p_exito BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @v_fechaCreacion DATETIME2;

    SELECT @v_fechaCreacion = fechaCreacion
    FROM dbo.RESERVA
    WHERE id = @p_idReserva;

    IF @v_fechaCreacion IS NULL
    BEGIN
        SET @p_exito = 0;
    END
    ELSE IF DATEDIFF(HOUR, SYSDATETIME(), @v_fechaCreacion) >= 24
    BEGIN
        UPDATE dbo.RESERVA
        SET estado = 'CANCELADA'
        WHERE id = @p_idReserva;

        UPDATE bh
        SET bh.estado = 'DISPONIBLE'
        FROM dbo.BLOQUE_HORARIO bh
        INNER JOIN dbo.DETALLE_RESERVA dr
            ON bh.id = dr.idBloqueHorario
        WHERE dr.idReserva = @p_idReserva;

        SET @p_exito = 1;
    END
    ELSE
    BEGIN
        SET @p_exito = 0;
    END;
END;
GO

CREATE PROCEDURE dbo.listarReservasCliente
    @p_idCliente INT
AS
BEGIN
    SET NOCOUNT ON;

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
    FROM dbo.RESERVA r
    INNER JOIN dbo.CANCHA c
        ON c.id = r.idCancha
    INNER JOIN dbo.DETALLE_RESERVA dr
        ON r.id = dr.idReserva
    INNER JOIN dbo.BLOQUE_HORARIO bh
        ON bh.id = dr.idBloqueHorario
    LEFT JOIN dbo.PAGO p
        ON p.idReserva = r.id
    WHERE r.idCliente = @p_idCliente
    ORDER BY r.fechaCreacion DESC;
END;
GO