USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.insertarComprobante;
DROP PROCEDURE IF EXISTS dbo.eliminarComprobante;
DROP PROCEDURE IF EXISTS dbo.buscarComprobantePorId;
DROP PROCEDURE IF EXISTS dbo.listarComprobantes;

DROP PROCEDURE IF EXISTS dbo.insertarPago;
DROP PROCEDURE IF EXISTS dbo.modificarPago;
DROP PROCEDURE IF EXISTS dbo.eliminarPago;
DROP PROCEDURE IF EXISTS dbo.buscarPagoPorId;
DROP PROCEDURE IF EXISTS dbo.listarPagos;
GO

CREATE PROCEDURE dbo.insertarComprobante
    @p_idReserva INT,
    @p_serie VARCHAR(20),
    @p_fechaEmision DATETIME2,
    @p_subtotal DECIMAL(10,2),
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @v_numero INT;
    DECLARE @v_comision DECIMAL(10,2) = 5.00;
    DECLARE @v_valorVenta DECIMAL(10,2);
    DECLARE @v_montoIgv DECIMAL(10,2);
    DECLARE @v_numeroTexto VARCHAR(20);

    SELECT @v_numero = COALESCE(MAX(TRY_CAST(numero AS INT)), 0) + 1
    FROM dbo.COMPROBANTE
    WHERE serie = @p_serie;

    SET @v_valorVenta = ROUND((@p_subtotal + @v_comision) / 1.18, 2);
    SET @v_montoIgv = ROUND((@p_subtotal + @v_comision) - @v_valorVenta, 2);

    SET @v_numeroTexto = RIGHT(REPLICATE('0', 8) + CAST(@v_numero AS VARCHAR(20)), 8);

    INSERT INTO dbo.COMPROBANTE (
        idReserva,
        serie,
        numero,
        fechaEmision,
        montoBloques,
        comisionPlataforma,
        valorVenta,
        montoIgv
    )
    VALUES (
        @p_idReserva,
        @p_serie,
        @v_numeroTexto,
        @p_fechaEmision,
        @p_subtotal,
        @v_comision,
        @v_valorVenta,
        @v_montoIgv
    );

    SET @p_id = SCOPE_IDENTITY();
END;
GO

CREATE PROCEDURE dbo.eliminarComprobante
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM dbo.COMPROBANTE
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarComprobantePorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id AS idComprobante,
        idReserva,
        serie,
        numero,
        fechaEmision,
        montoBloques,
        comisionPlataforma,
        valorVenta,
        montoIgv
    FROM dbo.COMPROBANTE
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarComprobantes
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id AS idComprobante,
        idReserva,
        serie,
        numero,
        fechaEmision,
        montoBloques,
        comisionPlataforma,
        valorVenta,
        montoIgv
    FROM dbo.COMPROBANTE;
END;
GO

CREATE PROCEDURE dbo.insertarPago
    @p_idReserva INT,
    @p_metodoPago VARCHAR(20),
    @p_monto DECIMAL(10,2),
    @p_fechaPago DATETIME2,
    @p_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.PAGO (
        idReserva,
        idComprobante,
        metodoPago,
        monto,
        fechaPago
    )
    VALUES (
        @p_idReserva,
        NULL,
        @p_metodoPago,
        @p_monto,
        @p_fechaPago
    );

    SET @p_id = SCOPE_IDENTITY();

    UPDATE dbo.RESERVA
    SET
        estado = 'CONFIRMADA',
        idPago = @p_id
    WHERE id = @p_idReserva;
END;
GO

CREATE PROCEDURE dbo.modificarPago
    @p_metodoPago VARCHAR(20),
    @p_monto DECIMAL(10,2),
    @p_fechaPago DATETIME2,
    @p_idComprobante INT,
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE dbo.PAGO
    SET
        metodoPago = @p_metodoPago,
        monto = @p_monto,
        fechaPago = @p_fechaPago,
        idComprobante = @p_idComprobante
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.eliminarPago
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    DELETE FROM dbo.PAGO
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.buscarPagoPorId
    @p_id INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id AS idPago,
        idReserva,
        idComprobante,
        metodoPago,
        monto,
        fechaPago
    FROM dbo.PAGO
    WHERE id = @p_id;
END;
GO

CREATE PROCEDURE dbo.listarPagos
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        id AS idPago,
        idReserva,
        idComprobante,
        metodoPago,
        monto,
        fechaPago
    FROM dbo.PAGO;
END;
GO