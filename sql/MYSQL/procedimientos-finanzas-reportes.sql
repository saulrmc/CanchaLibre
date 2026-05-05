USE CanchaLibre;

DROP PROCEDURE IF EXISTS registrarPagoCliente;
DROP PROCEDURE IF EXISTS generarComprobanteDigital;
DROP PROCEDURE IF EXISTS reporteIngresosTotales;

DELIMITER //
-- RF11: Registrar pago (Yape/Plin) vinculado al Cliente
CREATE PROCEDURE registrarPagoCliente(
    IN p_metodoPago VARCHAR(50), 
    IN p_monto DECIMAL(10,2),
    IN p_idReserva INT,
    OUT p_idPago INT)
BEGIN
    INSERT INTO Pago (metodoPago, monto, fechaPago, idReserva)
    VALUES (p_metodoPago, p_monto, NOW(), p_idReserva);
    
    SET p_idPago = LAST_INSERT_ID();
    -- Actualiza estado a Pagado automáticamente
    UPDATE Reserva SET estado = 'Pagado' WHERE idReserva = p_idReserva;
END //

-- RF14: Generar comprobante con IGV (18%)
CREATE PROCEDURE generarComprobanteDigital(IN p_idReserva INT, OUT p_idComp INT)
BEGIN
    INSERT INTO Comprobante (idReserva, monto, tipoReserva)
    SELECT idReserva, (monto * 1.18), 1 -- 1 indica tipo Digital/Confirmada
    FROM Pago WHERE idReserva = p_idReserva;
    
    SET p_idComp = LAST_INSERT_ID();
END //

-- RF16: Reporte consolidado para el Administrador
CREATE PROCEDURE reporteIngresosTotales(IN p_fechaInicio DATE, IN p_fechaFin DATE)
BEGIN
    SELECT 
        SUM(monto) as MontoBruto,
        SUM(monto * 0.18) as IGV_Total,
        SUM(monto - (monto * 0.18)) as UtilidadNeta
    FROM Pago
    WHERE fechaPago BETWEEN p_fechaInicio AND p_fechaFin;
END //

DELIMITER ;