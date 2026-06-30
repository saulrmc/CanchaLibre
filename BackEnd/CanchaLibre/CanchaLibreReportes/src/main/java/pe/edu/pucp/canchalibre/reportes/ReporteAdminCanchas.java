package pe.edu.pucp.canchalibre.reportes;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pe.edu.pucp.canchalibre.db.DBFactoryProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "ServletAdminCanchas", urlPatterns = {"/reportes/canchas"})
public class ReporteAdminCanchas extends HttpServlet {

    private final String NOMBRE_REPORTE = "reportes/ReporteAdminCanchas.jasper";
    private final String NOMBRE_LOGO = "imagenes/Logo.png";
    private static final String SQL = ""
            + "SELECT c.nombre AS Sede, c.direccion AS Direccion, "
            + "  COUNT(DISTINCT r.id) AS Reservas, "
            + "  IFNULL(SUM(CASE WHEN p.monto > 0 THEN p.monto ELSE 0 END), 0) AS Ingresos, "
            + "  IFNULL(SUM(TIMESTAMPDIFF(HOUR, bh.horaInicio, bh.horaFin)), 0) AS `Horas de ocupación` "
            + "FROM CANCHA c "
            + "LEFT JOIN RESERVA r ON c.id = r.idCancha AND r.estado = 'CONFIRMADA' "
            + "LEFT JOIN PAGO p ON r.id = p.idReserva "
            + "LEFT JOIN DETALLE_RESERVA dr ON r.id = dr.idReserva "
            + "LEFT JOIN BLOQUE_HORARIO bh ON dr.idBloqueHorario = bh.id "
            + "WHERE c.activo = TRUE "
            + "GROUP BY c.id, c.nombre, c.direccion "
            + "ORDER BY Reservas DESC";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf");
        InputStream reporte = getClass().getClassLoader().getResourceAsStream(NOMBRE_REPORTE);
        if (reporte == null) {
            throw new FileNotFoundException("No se encontro el reporte compilado: " + NOMBRE_REPORTE);
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("LOGO_STREAM", NOMBRE_LOGO);

        try (Connection conn = DBFactoryProvider.getManager().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SQL)) {

            List<Map<String, Object>> data = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Sede", rs.getString("Sede"));
                row.put("Direccion", rs.getString("Direccion"));
                row.put("Reservas", rs.getLong("Reservas"));
                row.put("Ingresos", rs.getBigDecimal("Ingresos"));
                row.put("Horas de ocupación", rs.getBigDecimal("Horas de ocupación"));
                data.add(row);
            }

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);
            JasperPrint jp = JasperFillManager.fillReport(reporte, parametros, ds);
            JasperExportManager.exportReportToPdfStream(jp, response.getOutputStream());

        } catch (SQLException | ClassNotFoundException | JRException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar el reporte de canchas: " + ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet autocontenido para generar el reporte en PDF de CanchaLibre";
    }
}
