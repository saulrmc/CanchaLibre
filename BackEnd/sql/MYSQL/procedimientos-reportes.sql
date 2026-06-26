USE CanchaLibre;

DROP PROCEDURE IF EXISTS reporteOcupacionCanchas;
DROP PROCEDURE IF EXISTS reporteIngresosTotales;

DELIMITER //

CREATE PROCEDURE reporteOcupacionCanchas(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE
)
BEGIN
SELECT
    c.id AS idCancha,
    c.nombre AS nombreCancha,
    COUNT(DISTINCT r.id) AS totalReservas,
    COALESCE(
            SUM(TIMESTAMPDIFF(MINUTE, bh.horaInicio, bh.horaFin) / 60.0),
            0
    ) AS horasOcupadas,
    ROUND(
            COUNT(DISTINCT r.id) * 100.0 /
            NULLIF((
                       SELECT COUNT(*)
                       FROM BLOQUE_HORARIO bh2
                       WHERE bh2.idCancha = c.id
                         AND bh2.activo = TRUE
                   ), 0),
            2
    ) AS porcentajeOcupacion
FROM CANCHA c
         LEFT JOIN RESERVA r ON r.idCancha = c.id
    AND DATE(r.fechaCreacion) BETWEEN p_fechaInicio AND p_fechaFin
    AND r.estado = 'CONFIRMADA'
    LEFT JOIN DETALLE_RESERVA dr ON r.id = dr.idReserva
    LEFT JOIN BLOQUE_HORARIO bh ON bh.id = dr.idBloqueHorario
WHERE c.activo = TRUE
GROUP BY c.id, c.nombre;

SELECT
    c.id AS idCancha,
    c.nombre AS nombreCancha,
    bh.horaInicio AS horaInicio,
    COUNT(*) AS cantidadReservas
FROM RESERVA r
         INNER JOIN CANCHA c ON c.id = r.idCancha
         INNER JOIN DETALLE_RESERVA dr ON r.id = dr.idReserva
         INNER JOIN BLOQUE_HORARIO bh ON bh.id = dr.idBloqueHorario
WHERE DATE(r.fechaCreacion) BETWEEN p_fechaInicio AND p_fechaFin
  AND r.estado = 'CONFIRMADA'
GROUP BY c.id, c.nombre, bh.horaInicio
ORDER BY c.id, cantidadReservas DESC;
END //

CREATE PROCEDURE reporteIngresosTotales(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE
)
BEGIN
SELECT
    COUNT(comp.id) AS totalComprobantes,
    COALESCE(SUM(p.monto), 0) AS montoBrutoTotal,
    COALESCE(SUM(comp.valorVenta), 0) AS valorVentaTotal,
    COALESCE(SUM(comp.montoIgv), 0) AS igvTotal,
    COALESCE(SUM(comp.comisionPlataforma), 0) AS totalComisiones,
    ROUND(COALESCE(SUM(comp.valorVenta), 0) - COALESCE(SUM(comp.comisionPlataforma), 0), 2) AS utilidadNeta
FROM COMPROBANTE comp
         INNER JOIN RESERVA r ON r.id = comp.idReserva
         INNER JOIN PAGO p ON p.idReserva = r.id
WHERE DATE(comp.fechaEmision) BETWEEN p_fechaInicio AND p_fechaFin
  AND r.estado = 'CONFIRMADA';
END //

DELIMITER ;