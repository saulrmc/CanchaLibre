package pe.edu.pucp.canchalibre.reportes;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import pe.edu.pucp.canchalibre.db.DBFactoryProvider;

@WebServlet(name = "ServletAdminUsuarios", urlPatterns = {"/reportes/usuarios"})
public class ReporteAdminUsuarios extends HttpServlet {

    private final String NOMBRE_REPORTE = "reportes/ReporteAdminUsuarios.jasper";
    private final String NOMBRE_LOGO = "imagenes/Logo.png";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf");
        InputStream reporte = getClass().getClassLoader().getResourceAsStream(NOMBRE_REPORTE);
        if (reporte == null) {
            throw new FileNotFoundException("No se encontro el reporte compilado: " + NOMBRE_REPORTE);
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("LOGO_STREAM", NOMBRE_LOGO);

        try (Connection conn = DBFactoryProvider.getManager().getConnection()) {
            JasperPrint jp = JasperFillManager.fillReport(reporte, parametros, conn);
            JasperExportManager.exportReportToPdfStream(jp, response.getOutputStream());
        } catch (SQLException | ClassNotFoundException | JRException ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar el reporte de usuarios: " + ex.getMessage());
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
        return "Servlet autocontenido para generar las boletas de venta en PDF de CanchaLibre";
    }
}