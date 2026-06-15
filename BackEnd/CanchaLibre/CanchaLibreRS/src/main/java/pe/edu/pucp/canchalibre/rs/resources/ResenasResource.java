package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.resena.ResenaBO;
import pe.edu.pucp.canchalibre.bo.resena.ResenaBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class ResenasResource {
    private final ResenaBO resenaBO;

    @Context
    private UriInfo uriInfo;

    public ResenasResource() {
        resenaBO = new ResenaBOImpl();
    }

    @GET
    public List<Resena> listaResenas() {
        return resenaBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerResenaPorId(@PathParam("id") int idResena) {
        if (idResena < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Resena resena = resenaBO.obtener(idResena);
        if (resena == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reseña con id:  "
                            + idResena + ", no existe"))
                    .build();
        }

        return Response.ok(resena).build();
    }

    @POST
    public Response crearResena(Resena resena) {
        if (resena == null ||
            resena.getDescripcion() == null ||
            resena.getCalificacion() < 0 ||
            resena.getFechaPublicacion() == null) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la reseña es inválido"))
                    .build();
        }

        resenaBO.guardar(resena, Estado.Nuevo);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(resena.getIdResena()))
                .build();

        return Response.created(location)
                .entity(resena)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarResena(Resena resena, @PathParam("id") int idResena) {
        if (idResena < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (resenaBO.obtener(idResena) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reseña con id:  "
                            + idResena + ", no existe"))
                    .build();
        }

        if (resena == null ||
            resena.getDescripcion() == null ||
            resena.getCalificacion() < 0 ||
            resena.getFechaPublicacion() == null) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la reseña es inválido"))
                    .build();
        }

        resenaBO.guardar(resena, Estado.Modificado);

        return Response.ok(resena).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarResena(@PathParam("id") int idResena) {
        if (idResena < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Resena resena = resenaBO.obtener(idResena);
        if (resena == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reseña con id:  "
                            + idResena + ", no existe"))
                    .build();
        }

        resenaBO.eliminar(idResena);

        return Response.noContent().build();
    }
}
