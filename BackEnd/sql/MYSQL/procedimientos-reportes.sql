USE CanchaLibre;

DROP PROCEDURE IF EXISTS reporteOcupacionCanchas;
DROP PROCEDURE IF EXISTS reporteIngresosTotales;

DELIMITER //

-- RF15: Reporte de ocupación por cancha para el Administrador.
-- Muestra por cada cancha el total de reservas, las horas ocupadas,
-- el porcentaje de ocupación respecto al total de bloques disponibles,
-- y los bloques de mayor y menor demanda (hora de inicio más frecuente).
CREATE PROCEDURE reporteOcupacionCanchas(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE)
BEGIN
    -- Ocupación general por cancha
    SELECT
        c.id                                        AS idCancha,
        c.nombre                                    AS nombreCancha,
        COUNT(r.id)                                 AS totalReservas,
        SUM(TIMESTAMPDIFF(MINUTE, r.fechaHoraInicio, r.fechaHoraFin) / 60.0)
                                                    AS horasOcupadas,
        ROUND(
            COUNT(r.id) * 100.0 /
            NULLIF((
                SELECT COUNT(*)
                FROM BLOQUE_HORARIO bh
                WHERE bh.idCancha = c.id
                  AND bh.activo   = TRUE
            ), 0)
        , 2)                                        AS porcentajeOcupacion
    FROM CANCHA c
    LEFT JOIN RESERVA r ON r.idCancha = c.id
        AND DATE(r.fechaHoraInicio) BETWEEN p_fechaInicio AND p_fechaFin
        AND r.estado IN ('CONFIRMADA', 'COMPLETADA')
    WHERE c.activo = TRUE
    GROUP BY c.id, c.nombre;

    -- Horario de mayor demanda por cancha
    SELECT
        c.id                    AS idCancha,
        c.nombre                AS nombreCancha,
        TIME(r.fechaHoraInicio) AS horaInicio,
        COUNT(*)                AS cantidadReservas
    FROM RESERVA r
    INNER JOIN CANCHA c ON c.id = r.idCancha
    WHERE DATE(r.fechaHoraInicio) BETWEEN p_fechaInicio AND p_fechaFin
      AND r.estado IN ('CONFIRMADA', 'COMPLETADA')
    GROUP BY c.id, c.nombre, TIME(r.fechaHoraInicio)
    ORDER BY c.id, cantidadReservas DESC;
END //

-- RF16: Reporte consolidado de ingresos para el Administrador.
-- Muestra montos brutos, IGV total, comisiones y utilidad neta
-- en un periodo de tiempo.
-- IGV = 18% del valorVenta, comisionPlataforma = 5.00 por comprobante.
CREATE PROCEDURE reporteIngresosTotales(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE)
BEGIN
    SELECT
        COUNT(comp.id)              AS totalComprobantes,
        SUM(comp.montoBloques)      AS sumaMontoBloques,
        SUM(comp.valorVenta)        AS sumaValorVenta,
        -- IGV = montoTotal - valorVenta
        SUM(comp.montoTotal - comp.valorVenta)
                                    AS igvTotal,
        SUM(comp.montoTotal)        AS montoBrutoTotal,
        -- Utilidad neta = valorVenta - comision por comprobante (5.00)
        -- La comision ya está incluida en valorVenta = montoBloques + 5.00
        SUM(comp.valorVenta - comp.montoBloques)
                                    AS totalComisiones,
        SUM(comp.montoBloques)      AS utilidadNeta
    FROM COMPROBANTE comp
    INNER JOIN RESERVA r ON r.id = comp.idReserva
    WHERE DATE(comp.fechaEmision) BETWEEN p_fechaInicio AND p_fechaFin
      AND comp.activo = TRUE;
END //

DELIMITER ;
