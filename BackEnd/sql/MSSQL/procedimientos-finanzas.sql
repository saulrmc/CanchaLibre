USE canchalibre;
GO

IF OBJECT_ID('dbo.registrarPagoCliente', 'P') IS NOT NULL DROP PROCEDURE dbo.registrarPagoCliente;
GO
IF OBJECT_ID('dbo.generarComprobanteDigital', 'P') IS NOT NULL DROP PROCEDURE dbo.generarComprobanteDigital;
GO
IF OBJECT_ID('dbo.reporteIngresosTotales', 'P') IS NOT NULL DROP PROCEDURE dbo.reporteIngresosTotales;
GO

-- RF11: Registrar pago Yape/Plin
CREATE PROCEDURE dbo.registrarPagoCliente
    @p_metodoPago NVARCHAR(50),
    @p_monto DECIMAL(10,2),
    @p_idReserva INT,
    @p_idPago INT OUTPUT
AS
BEGIN
    INSERT INTO Pago (metodoPago, monto, fechaPago, idReserva)
    VALUES (@p_metodoPago, @p_monto, GETDATE(), @p_idReserva);
    
    SET @p_idPago = SCOPE_IDENTITY();
    UPDATE Reserva SET estado = 'Pagado' WHERE idReserva = @p_idReserva;
END
GO

-- RF14: Generar comprobante con IGV
CREATE PROCEDURE dbo.generarComprobanteDigital
    @p_idReserva INT,
    @p_idComp INT OUTPUT
AS
BEGIN
    INSERT INTO Comprobante (idReserva, monto, tipoReserva)
    SELECT idReserva, (monto * 1.18), 1
    FROM Pago WHERE idReserva = @p_idReserva;
    
    SET @p_idComp = SCOPE_IDENTITY();
END
GO

-- RF16: Reporte consolidado de ingresos
CREATE PROCEDURE dbo.reporteIngresosTotales
    @p_fechaInicio DATE,
    @p_fechaFin DATE
AS
BEGIN
    SELECT 
        SUM(monto) as MontoBruto,
        SUM(monto * 0.18) as IGV_Total,
        SUM(monto - (monto * 0.18)) as UtilidadNeta
    FROM Pago
    WHERE CAST(fechaPago AS DATE) BETWEEN @p_fechaInicio AND @p_fechaFin;
END
GO