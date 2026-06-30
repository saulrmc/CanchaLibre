package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.cancha.CanchaBO;
import pe.edu.pucp.canchalibre.bo.cancha.CanchaBOImpl;
import pe.edu.pucp.canchalibre.db.DBFactoryProvider;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.rs.dto.IngresoCanchaDTO;

import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/v1/canchas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CanchasResource {
    private final CanchaBO canchaBO;

    private static final String SQL_INGRESOS = ""
            + "SELECT c.id, c.nombre, "
            + "  COUNT(DISTINCT r.id) AS reservas, "
            + "  IFNULL(SUM(CASE WHEN p.monto > 0 THEN p.monto ELSE 0 END), 0) AS ingresos, "
            + "  IFNULL(SUM(TIMESTAMPDIFF(HOUR, bh.horaInicio, bh.horaFin)), 0) AS horasOcupacion "
            + "FROM CANCHA c "
            + "LEFT JOIN RESERVA r ON c.id = r.idCancha AND r.estado = 'CONFIRMADA' "
            + "LEFT JOIN PAGO p ON r.id = p.idReserva "
            + "LEFT JOIN DETALLE_RESERVA dr ON r.id = dr.idReserva "
            + "LEFT JOIN BLOQUE_HORARIO bh ON dr.idBloqueHorario = bh.id "
            + "WHERE c.activo = TRUE "
            + "GROUP BY c.id, c.nombre "
            + "ORDER BY reservas DESC";

    @Context
    private UriInfo uriInfo;

    public CanchasResource() {
        canchaBO = new CanchaBOImpl();
    }

    @GET
    public List<Cancha> listaCanchas() {
        return canchaBO.listar();
    }

    @GET @Path("/con-ingresos")
    public List<IngresoCanchaDTO> listarCanchasConIngresos() {
        List<IngresoCanchaDTO> resultado = new ArrayList<>();
        try (Connection conn = DBFactoryProvider.getManager().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SQL_INGRESOS)) {
            while (rs.next()) {
                IngresoCanchaDTO dto = new IngresoCanchaDTO();
                dto.setId(rs.getInt("id"));
                dto.setNombre(rs.getString("nombre"));
                dto.setReservas(rs.getInt("reservas"));
                dto.setIngresos(rs.getDouble("ingresos"));
                dto.setHorasOcupacion(rs.getInt("horasOcupacion"));
                resultado.add(dto);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error al obtener ingresos por cancha", e);
        }
        return resultado;
    }

    @GET
    @Path("{id}")
    public Response obtenerCanchaPorId(@PathParam("id") int idCancha) {
        if (idCancha < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Cancha cancha = canchaBO.obtener(idCancha);
        if (cancha == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La cancha con id:  "
                            + idCancha + ", no existe"))
                    .build();
        }

        return Response.ok(cancha).build();
    }

    @POST
    public Response crearCancha(Cancha cancha) {
        if (cancha == null ||
                cancha.getNombre() == null ||
                cancha.getNombre().isBlank() ||
                cancha.getNombre().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la cancha es inválido"))
                    .build();
        }

        canchaBO.guardar(cancha, Estado.NUEVO);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(cancha.getId()))
                .build();

        return Response.created(location)
                .entity(cancha)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarCancha(Cancha cancha, @PathParam("id") int idCancha) {
        if (idCancha < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (canchaBO.obtener(idCancha) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La cancha con id:  "
                            + idCancha + ", no existe"))
                    .build();
        }

        if (cancha == null ||
                cancha.getNombre() == null ||
                cancha.getNombre().isBlank() ||
                cancha.getNombre().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la cancha es inválido"))
                    .build();
        }

        canchaBO.guardar(cancha, Estado.MODIFICADO);

        return Response.ok(cancha).build();
    }

    @GET
    @Path("propietario/{userName}")
    public List<Cancha> listarCanchasPorCuenta(@PathParam("userName") String userName) {
        return canchaBO.listarCanchasPorCuenta(userName);
    }

    @DELETE
    @Path("{id}")
    public Response eliminarCancha(@PathParam("id") int idCancha) {
        if (idCancha < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Cancha cancha = canchaBO.obtener(idCancha);
        if (cancha == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La cancha con id:  "
                            + idCancha + ", no existe"))
                    .build();
        }

        canchaBO.eliminar(idCancha);

        return Response.noContent().build();
    }
}

