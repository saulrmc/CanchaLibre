USE CanchaLibre;

DROP PROCEDURE IF EXISTS reporteOcupacionCanchas;
DROP PROCEDURE IF EXISTS reporteIngresosTotales;

DELIMITER //

-- RF15: Reporte de ocupación por cancha para el Administrador.
-- Muestra por cada cancha el total de reservas, las horas ocupadas,
-- el porcentaje de ocupación respecto al total de bloques disponibles,
-- y los bloques de mayor demanda por hora de inicio.
CREATE PROCEDURE reporteOcupacionCanchas(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE
)
BEGIN
    -- Ocupación general por cancha
    SELECT
        c.id AS idCancha,
        c.nombre AS nombreCancha,
        COUNT(r.id) AS totalReservas,
        COALESCE(
            SUM(TIMESTAMPDIFF(MINUTE, bh.horaInicio, bh.horaFin) / 60.0),
            0
        ) AS horasOcupadas,
        ROUND(
            COUNT(r.id) * 100.0 /
            NULLIF((
                SELECT COUNT(*)
                FROM BloqueHorario bh2
                WHERE bh2.idCancha = c.id
                  AND bh2.activo = TRUE
            ), 0),
            2
        ) AS porcentajeOcupacion
    FROM Cancha c
    LEFT JOIN Reserva r ON r.idCancha = c.id
        AND DATE(r.fechaReserva) BETWEEN p_fechaInicio AND p_fechaFin
        AND r.estado IN ('PAGADO', 'COMPLETADO')
    LEFT JOIN BloqueHorario bh ON bh.id = r.idBloqueHorario
    WHERE c.activo = TRUE
    GROUP BY c.id, c.nombre;

    -- Horario de mayor demanda por cancha
    SELECT
        c.id AS idCancha,
        c.nombre AS nombreCancha,
        bh.horaInicio AS horaInicio,
        COUNT(*) AS cantidadReservas
    FROM Reserva r
    INNER JOIN Cancha c ON c.id = r.idCancha
    INNER JOIN BloqueHorario bh ON bh.id = r.idBloqueHorario
    WHERE DATE(r.fechaReserva) BETWEEN p_fechaInicio AND p_fechaFin
      AND r.estado IN ('PAGADO', 'COMPLETADO')
    GROUP BY c.id, c.nombre, bh.horaInicio
    ORDER BY c.id, cantidadReservas DESC;
END //

-- RF16: Reporte consolidado de ingresos para el Administrador.
-- Muestra ingresos brutos pagados en un periodo de tiempo.
-- En el DDL actual los montos finales están en Pago.monto.
-- Comprobante solo guarda serie, numero, fechaEmision e idReserva.
CREATE PROCEDURE reporteIngresosTotales(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE
)
BEGIN
    SELECT
        COUNT(comp.idComprobante) AS totalComprobantes,
        COALESCE(SUM(p.monto), 0) AS montoBrutoTotal,
        ROUND(COALESCE(SUM(p.monto), 0) / 1.18, 2) AS valorVentaTotal,
        ROUND(COALESCE(SUM(p.monto), 0) - (COALESCE(SUM(p.monto), 0) / 1.18), 2) AS igvTotal,
        ROUND(COUNT(comp.idComprobante) * 5.00, 2) AS totalComisiones,
        ROUND((COALESCE(SUM(p.monto), 0) / 1.18) - (COUNT(comp.idComprobante) * 5.00), 2) AS utilidadNeta
    FROM Comprobante comp
    INNER JOIN Reserva r ON r.id = comp.idReserva
    INNER JOIN Pago p ON p.idComprobante = comp.idComprobante
    WHERE DATE(comp.fechaEmision) BETWEEN p_fechaInicio AND p_fechaFin
      AND r.estado IN ('PAGADO', 'COMPLETADO');
END //

DELIMITER ;
