package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.usuario.AdministradorBO;
import pe.edu.pucp.canchalibre.bo.usuario.AdministradorBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Administrador;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("v1/administradores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdministradoresResource {
    private final AdministradorBO administradorBO;

    @Context
    private UriInfo uriInfo;

    public AdministradoresResource() {
        administradorBO = new AdministradorBOImpl();
    }

    @GET
    public List<Administrador> listaAdministradores() {
        return administradorBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerAdministradorPorId(@PathParam("id") int idAdministrador) {
        if (idAdministrador <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Administrador administrador = administradorBO.obtener(idAdministrador);
        if (administrador == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El administrador con id:  "
                            + idAdministrador + ", no existe"))
                    .build();
        }

        return Response.ok(administrador).build();
    }

    @POST
    public Response crearAdministrador(Administrador administrador) {
        if (administrador == null ||
                administrador.getCorreo() == null ||
                administrador.getCuentaUsuario().getPassword() == null ||
                administrador.getNombres() == null ||
                administrador.getCuentaUsuario().getPassword().isBlank() ||
                administrador.getCorreo().isBlank() ||
                administrador.getNombres().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la administrador es inválido"))
                    .build();
        }

        administradorBO.guardar(administrador, Estado.NUEVO);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(administrador.getId()))
                .build();

        return Response.created(location)
                .entity(administrador)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarAdministrador(Administrador administrador, @PathParam("id") int idAdministrador) {
        if (idAdministrador < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (administradorBO.obtener(idAdministrador) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El administrador con id:  "
                            + idAdministrador + ", no existe"))
                    .build();
        }

        if (administrador == null ||
                administrador.getCorreo() == null ||
                administrador.getCuentaUsuario().getPassword() == null ||
                administrador.getNombres() == null ||
                administrador.getCuentaUsuario().getPassword().isBlank() ||
                administrador.getCorreo().isBlank() ||
                administrador.getNombres().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el administrador es inválido"))
                    .build();
        }

        administradorBO.guardar(administrador, Estado.MODIFICADO);

        return Response.ok(administrador).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarAdministrador(@PathParam("id") int idAdministrador) {
        if (idAdministrador < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Administrador administrador = administradorBO.obtener(idAdministrador);
        if (administrador == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El administrador con id:  "
                            + idAdministrador + ", no existe"))
                    .build();
        }

        administradorBO.eliminar(idAdministrador);

        return Response.noContent().build();
    }
}
