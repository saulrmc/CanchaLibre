USE CanchaLibre;

DROP PROCEDURE IF EXISTS insertarComprobante;
DROP PROCEDURE IF EXISTS eliminarComprobante;
DROP PROCEDURE IF EXISTS buscarComprobantePorId;
DROP PROCEDURE IF EXISTS listarComprobantes;

DROP PROCEDURE IF EXISTS insertarPago;
DROP PROCEDURE IF EXISTS modificarPago;
DROP PROCEDURE IF EXISTS eliminarPago;
DROP PROCEDURE IF EXISTS buscarPagoPorId;
DROP PROCEDURE IF EXISTS listarPagos;

DELIMITER //

-- PROCEDIMIENTOS PARA COMPROBANTE

CREATE PROCEDURE insertarComprobante(
    IN  p_idReserva    INT,
    IN  p_serie        VARCHAR(20),
    IN  p_fechaEmision DATETIME,
    IN  p_subtotal     DECIMAL(10,2),
    OUT p_id           INT
)
BEGIN
    DECLARE v_numero INT;
    DECLARE v_comision DECIMAL(10,2) DEFAULT 5.00;
    DECLARE v_valorVenta DECIMAL(10,2);
    DECLARE v_montoIgv DECIMAL(10,2);

    -- Generar el correlativo automático por serie
SELECT COALESCE(MAX(CAST(numero AS UNSIGNED)), 0) + 1
INTO v_numero
FROM COMPROBANTE
WHERE serie = p_serie;

-- Cálculos financieros basados en el subtotal (MontoBloques)
SET v_valorVenta = ROUND((p_subtotal + v_comision) / 1.18, 2);
    SET v_montoIgv = ROUND((p_subtotal + v_comision) - v_valorVenta, 2);

INSERT INTO COMPROBANTE (
    idReserva, serie, numero, fechaEmision, montoBloques, comisionPlataforma, valorVenta, montoIgv
)
VALUES (
           p_idReserva,
           p_serie,
           LPAD(v_numero, 8, '0'),
           p_fechaEmision,
           p_subtotal,
           v_comision,
           v_valorVenta,
           v_montoIgv
       );

SET p_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE eliminarComprobante(
    IN p_id INT
)
BEGIN
DELETE FROM COMPROBANTE
WHERE id = p_id;
END //

CREATE PROCEDURE buscarComprobantePorId(
    IN p_id INT
)
BEGIN
SELECT id AS idComprobante, idReserva, serie, numero, fechaEmision, montoBloques, comisionPlataforma, valorVenta, montoIgv
FROM COMPROBANTE
WHERE id = p_id;
END //

CREATE PROCEDURE listarComprobantes()
BEGIN
SELECT id AS idComprobante, idReserva, serie, numero, fechaEmision, montoBloques, comisionPlataforma, valorVenta, montoIgv
FROM COMPROBANTE;
END //

-- PROCEDIMIENTOS PARA PAGO

CREATE PROCEDURE insertarPago(
    IN  p_idReserva   INT,
    IN  p_metodoPago  VARCHAR(20),
    IN  p_monto       DECIMAL(10,2),
    IN  p_fechaPago   DATETIME,
    OUT p_id          INT
)
BEGIN
INSERT INTO PAGO (idReserva, idComprobante, metodoPago, monto, fechaPago)
VALUES (p_idReserva, NULL, p_metodoPago, p_monto, p_fechaPago);

SET p_id = LAST_INSERT_ID();

    -- Transición de estado de la reserva tras procesar el pago exitosamente
UPDATE RESERVA
SET estado = 'CONFIRMADA'
WHERE id = p_idReserva;
END //

CREATE PROCEDURE modificarPago(
    IN p_metodoPago     VARCHAR(20),
    IN p_monto          DECIMAL(10,2),
    IN p_fechaPago      DATETIME,
    IN p_idComprobante  INT,
    IN p_id             INT
)
BEGIN
UPDATE PAGO
SET metodoPago = p_metodoPago,
    monto = p_monto,
    fechaPago = p_fechaPago,
    idComprobante = p_idComprobante
WHERE id = p_id;
END //

CREATE PROCEDURE eliminarPago(
    IN p_id INT
)
BEGIN
DELETE FROM PAGO
WHERE id = p_id;
END //

CREATE PROCEDURE buscarPagoPorId(
    IN p_id INT
)
BEGIN
SELECT
    p.id AS idPago, p.idReserva, p.idComprobante, p.metodoPago, p.monto, p.fechaPago,
    c.id AS comp_id, c.serie AS comp_serie, c.numero AS comp_numero,
    c.fechaEmision AS comp_fechaEmision, c.montoBloques AS comp_montoBloques,
    c.comisionPlataforma AS comp_comision, c.valorVenta AS comp_valorVenta,
    c.montoIgv AS comp_montoIgv
FROM PAGO p
LEFT JOIN COMPROBANTE c ON p.idComprobante = c.id
WHERE p.id = p_id;
END //

CREATE PROCEDURE listarPagos()
BEGIN
SELECT
    p.id AS idPago, p.idReserva, p.idComprobante, p.metodoPago, p.monto, p.fechaPago,
    c.id AS comp_id, c.serie AS comp_serie, c.numero AS comp_numero,
    c.fechaEmision AS comp_fechaEmision, c.montoBloques AS comp_montoBloques,
    c.comisionPlataforma AS comp_comision, c.valorVenta AS comp_valorVenta,
    c.montoIgv AS comp_montoIgv
FROM PAGO p
LEFT JOIN COMPROBANTE c ON p.idComprobante = c.id;
END //
