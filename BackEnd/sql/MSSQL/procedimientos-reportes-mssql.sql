USE [CanchaLibre];
GO

DROP PROCEDURE IF EXISTS dbo.reporteOcupacionCanchas;
DROP PROCEDURE IF EXISTS dbo.reporteIngresosTotales;
GO

CREATE PROCEDURE dbo.reporteOcupacionCanchas
    @p_fechaInicio DATE,
    @p_fechaFin DATE
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        c.id AS idCancha,
        c.nombre AS nombreCancha,
        COUNT(DISTINCT r.id) AS totalReservas,
        COALESCE(
            SUM(DATEDIFF(MINUTE, bh.horaInicio, bh.horaFin) / 60.0),
            0
        ) AS horasOcupadas,
        ROUND(
            COUNT(DISTINCT r.id) * 100.0 /
            NULLIF((
                SELECT COUNT(*)
                FROM dbo.BLOQUE_HORARIO bh2
                WHERE bh2.idCancha = c.id
                  AND bh2.activo = 1
            ), 0),
            2
        ) AS porcentajeOcupacion
    FROM dbo.CANCHA c
    LEFT JOIN dbo.RESERVA r 
        ON r.idCancha = c.id
       AND CAST(r.fechaCreacion AS DATE) BETWEEN @p_fechaInicio AND @p_fechaFin
       AND r.estado = 'CONFIRMADA'
    LEFT JOIN dbo.DETALLE_RESERVA dr 
        ON r.id = dr.idReserva
    LEFT JOIN dbo.BLOQUE_HORARIO bh 
        ON bh.id = dr.idBloqueHorario
    WHERE c.activo = 1
    GROUP BY c.id, c.nombre;

    SELECT
        c.id AS idCancha,
        c.nombre AS nombreCancha,
        bh.horaInicio AS horaInicio,
        COUNT(*) AS cantidadReservas
    FROM dbo.RESERVA r
    INNER JOIN dbo.CANCHA c 
        ON c.id = r.idCancha
    INNER JOIN dbo.DETALLE_RESERVA dr 
        ON r.id = dr.idReserva
    INNER JOIN dbo.BLOQUE_HORARIO bh 
        ON bh.id = dr.idBloqueHorario
    WHERE CAST(r.fechaCreacion AS DATE) BETWEEN @p_fechaInicio AND @p_fechaFin
      AND r.estado = 'CONFIRMADA'
    GROUP BY c.id, c.nombre, bh.horaInicio
    ORDER BY c.id, cantidadReservas DESC;
END;
GO

CREATE PROCEDURE dbo.reporteIngresosTotales
    @p_fechaInicio DATE,
    @p_fechaFin DATE
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        COUNT(comp.id) AS totalComprobantes,
        COALESCE(SUM(p.monto), 0) AS montoBrutoTotal,
        COALESCE(SUM(comp.valorVenta), 0) AS valorVentaTotal,
        COALESCE(SUM(comp.montoIgv), 0) AS igvTotal,
        COALESCE(SUM(comp.comisionPlataforma), 0) AS totalComisiones,
        ROUND(
            COALESCE(SUM(comp.valorVenta), 0) - COALESCE(SUM(comp.comisionPlataforma), 0),
            2
        ) AS utilidadNeta
    FROM dbo.COMPROBANTE comp
    INNER JOIN dbo.RESERVA r 
        ON r.id = comp.idReserva
    INNER JOIN dbo.PAGO p 
        ON p.idReserva = r.id
    WHERE CAST(comp.fechaEmision AS DATE) BETWEEN @p_fechaInicio AND @p_fechaFin
      AND r.estado = 'CONFIRMADA';
END;
GO