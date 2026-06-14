package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.cancha.CanchaBO;
import pe.edu.pucp.canchalibre.bo.cancha.CanchaBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/v1/canchas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CanchasResource {
    private final CanchaBO canchaBO;

    @Context
    private UriInfo uriInfo;

    public CanchasResource() {
        canchaBO = new CanchaBOImpl();
    }

    @GET
    public List<Cancha> listaCanchas() {
        return canchaBO.listar();
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

        canchaBO.crear(cancha, Estado.Nuevo);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(cancha.getIdCancha()))
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

        canchaBO.crear(cancha, Estado.Modificado);

        return Response.ok(cancha).build();
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

