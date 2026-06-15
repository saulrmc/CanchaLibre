package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.usuario.PropietarioBO;
import pe.edu.pucp.canchalibre.bo.usuario.PropietarioBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class PropietariosResource {
    private final PropietarioBO propietarioBO;

    @Context
    private UriInfo uriInfo;

    public PropietariosResource() {
        propietarioBO = new PropietarioBOImpl();
    }

    @GET
    public List<Propietario> listaPropietarios() {
        return propietarioBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerPropietarioPorId(@PathParam("id") int idPropietario) {
        if (idPropietario < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Propietario propietario = propietarioBO.obtener(idPropietario);
        if (propietario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El propietario con id:  "
                            + idPropietario + ", no existe"))
                    .build();
        }

        return Response.ok(propietario).build();
    }

    @POST
    public Response crearPropietario(Propietario propietario) {
        if (propietario == null ||
                propietario.getCorreo() == null ||
                propietario.getContrasena() == null ||
                propietario.getNombres() == null ||
                propietario.getContrasena().isBlank() ||
                propietario.getCorreo().isBlank() ||
                propietario.getNombres().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la propietario es inválido"))
                    .build();
        }

        propietarioBO.guardar(propietario, Estado.Nuevo);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(propietario.getIdUsuario()))
                .build();

        return Response.created(location)
                .entity(propietario)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarPropietario(Propietario propietario, @PathParam("id") int idPropietario) {
        if (idPropietario < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (propietarioBO.obtener(idPropietario) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El propietario con id:  "
                            + idPropietario + ", no existe"))
                    .build();
        }

        if (propietario == null ||
                propietario.getCorreo() == null ||
                propietario.getContrasena() == null ||
                propietario.getNombres() == null ||
                propietario.getContrasena().isBlank() ||
                propietario.getCorreo().isBlank() ||
                propietario.getNombres().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el propietario es inválido"))
                    .build();
        }

        propietarioBO.guardar(propietario, Estado.Modificado);

        return Response.ok(propietario).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarPropietario(@PathParam("id") int idPropietario) {
        if (idPropietario < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Propietario propietario = propietarioBO.obtener(idPropietario);
        if (propietario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El propietario con id:  "
                            + idPropietario + ", no existe"))
                    .build();
        }

        propietarioBO.eliminar(idPropietario);

        return Response.noContent().build();
    }
}
