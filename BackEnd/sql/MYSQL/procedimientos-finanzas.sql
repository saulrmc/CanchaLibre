USE CanchaLibre;

DROP PROCEDURE IF EXISTS registrarPago;
DROP PROCEDURE IF EXISTS generarComprobante;

DELIMITER //

-- RF11: Registrar pago vinculado a una reserva (Yape o Plin).
-- Tras registrar el pago actualiza el estado de la reserva a CONFIRMADA.
-- El monto debe calcularse en Java sumando los precios de los bloques
-- del rango reservado antes de llamar a este procedimiento.
CREATE PROCEDURE registrarPago(
    IN  p_metodoPago  ENUM('YAPE','PLIN','EFECTIVO'),
    IN  p_monto       DECIMAL(10,2),
    IN  p_idReserva   INT,
    OUT p_id          INT)
BEGIN
    INSERT INTO PAGO (metodoPago, monto, fechaPago, idReserva)
    VALUES (p_metodoPago, p_monto, NOW(), p_idReserva);
    SET p_id = LAST_INSERT_ID();

    UPDATE RESERVA
    SET estado = 'CONFIRMADA'
    WHERE id = p_idReserva;
END //

-- RF14: Generar comprobante digital tras confirmar el pago.
-- Los montos se calculan en Java con las constantes del modelo:
--   comisionPlataforma = 5.00
--   igv = 0.18
--   montoBloques : suma de precios de bloques reservados
--   valorVenta   : montoBloques + comisionPlataforma
--   montoTotal   : valorVenta + (valorVenta * igv)
-- serie y numero los genera Java antes de llamar al procedimiento.
CREATE PROCEDURE generarComprobante(
    IN  p_serie        VARCHAR(10),
    IN  p_numero       VARCHAR(20),
    IN  p_montoBloques DECIMAL(10,2),
    IN  p_valorVenta   DECIMAL(10,2),
    IN  p_montoTotal   DECIMAL(10,2),
    IN  p_idReserva    INT,
    OUT p_id           INT)
BEGIN
    INSERT INTO COMPROBANTE (serie, numero, fechaEmision, montoBloques, valorVenta, montoTotal, idReserva)
    VALUES (p_serie, p_numero, NOW(), p_montoBloques, p_valorVenta, p_montoTotal, p_idReserva);
    SET p_id = LAST_INSERT_ID();
END //

DELIMITER ;
