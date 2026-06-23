package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.cancha.BloqueHorarioBO;
import pe.edu.pucp.canchalibre.bo.cancha.BloqueHorarioBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/v1/bloquehorario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BloquesHorariosResource {
    private final BloqueHorarioBO bloqueHorarioBO;

    @Context
    private UriInfo uriInfo;

    public BloquesHorariosResource() {
        bloqueHorarioBO = new BloqueHorarioBOImpl();
    }

    @GET
    public List<BloqueHorario> listaBloquesHorarios() {
        return bloqueHorarioBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerBloqueHorarioPorId(@PathParam("id") int idBloqueHorario) {
        if (idBloqueHorario <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        BloqueHorario reserva = bloqueHorarioBO.obtener(idBloqueHorario);

        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El bloque de horario con id: "
                            + idBloqueHorario + ", no existe"))
                    .build();
        }

        return Response.ok(reserva).build();
    }

    @POST
    public Response crearBloqueHorario(BloqueHorario bloqueHorario) {
        if (bloqueHorario == null ||
                bloqueHorario.getHoraFin() == null ||
                bloqueHorario.getHoraInicio() == null ||
                bloqueHorario.getEstado() == null ||
                bloqueHorario.getPrecio() <= 0 ||
                bloqueHorario.getDia() == null) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la reserva es inválido"))
                    .build();
        }

        bloqueHorarioBO.guardar(bloqueHorario, Estado.NUEVO);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(bloqueHorario.getId()))
                .build();

        return Response.created(location)
                .entity(bloqueHorario)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarBloqueHorario(BloqueHorario bloqueHorario, @PathParam("id") int idBloqueHorario) {
        if (idBloqueHorario <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (bloqueHorarioBO.obtener(idBloqueHorario) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reserva con id: "
                            + idBloqueHorario + ", no existe"))
                    .build();
        }

        if (bloqueHorario == null ||
                bloqueHorario.getHoraInicio() == null ||
                bloqueHorario.getHoraFin() == null ||
                bloqueHorario.getPrecio() <= 0 ||
                bloqueHorario.getDia() == null
        ) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para actualizar la reserva es inválido"))
                    .build();
        }

        bloqueHorario.setId(idBloqueHorario);

        bloqueHorarioBO.guardar(bloqueHorario, Estado.MODIFICADO);

        return Response.ok(bloqueHorario).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarBloqueHorario(@PathParam("id") int idBloqueHorario) {
        if (idBloqueHorario <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        BloqueHorario bloqueHorario = bloqueHorarioBO.obtener(idBloqueHorario);

        if (bloqueHorario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reserva con id: "
                            + idBloqueHorario + ", no existe"))
                    .build();
        }

        bloqueHorarioBO.eliminar(idBloqueHorario);

        return Response.noContent().build();
    }
}