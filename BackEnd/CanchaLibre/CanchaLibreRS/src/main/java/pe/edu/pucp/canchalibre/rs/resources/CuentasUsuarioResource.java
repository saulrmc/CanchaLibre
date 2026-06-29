package pe.edu.pucp.canchalibre.rs.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.cuentas.CuentaUsuarioBO;
import pe.edu.pucp.canchalibre.bo.cuentas.CuentaUsuarioBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.usuario.*;

import java.net.URI;
import java.util.Map;
import java.util.List;

@Path("v1/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuentasUsuarioResource {

    private final CuentaUsuarioBO cuentaUsuarioBO;
    @Context
    private UriInfo uriInfo;

    public CuentasUsuarioResource() {
        cuentaUsuarioBO = new CuentaUsuarioBOImpl();
    }

    @GET
    @Path("{id}")
    public Response obtener(@PathParam("id") int id) {
        CuentaUsuario cuenta = this.cuentaUsuarioBO.obtener(id);

        if (cuenta == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Area: " + id + ", no encontrada"))
                    .build();
        }

        return Response.ok(cuenta).build();
    }

    @GET
    public List<CuentaUsuario> listar() {
        return this.cuentaUsuarioBO.listar();
    }

    @PUT
    @Path("{id}")
    public Response actualizar(@PathParam("id") int id, CuentaUsuario cuenta) {
        if (cuenta == null || cuenta.getUserName()== null ||
                cuenta.getUserName().isBlank() ||
                cuenta.getPassword() == null ||
                cuenta.getPassword().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La cuenta no es valida")
                    .build();
        }

        if (this.cuentaUsuarioBO.obtener(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cuenta: " + id + ", no encontrada")
                    .build();
        }

        this.cuentaUsuarioBO.guardar(cuenta, Estado.MODIFICADO);

        return Response.ok(cuenta).build();
    }
    @DELETE
    @Path("{id}")
    public Response eliminar(@PathParam("id") int id) {
        if (this.cuentaUsuarioBO.obtener(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cuenta: " + id + ", no encontrada")
                    .build();
        }
        this.cuentaUsuarioBO.eliminar(id);

        return Response.noContent().build();
    }

    @POST
    @Path("login")
    public Response login(CuentaUsuario cuenta) {
        Persona persona = this.cuentaUsuarioBO.login(
                cuenta.getUserName(), cuenta.getPassword());

        if (persona == null) {
            return Response.status(401)
                    .entity(Map.of("error", "Credenciales inválidas"))
                    .build();
        }

        return Response.ok(Map.of(
                "id", persona.getId(),
                "nombres", persona.getNombres(),
                "correo", persona.getCorreo(),
                "rol", persona.getCuentaUsuario().getRol().name()
        )).build();
    }

    @PUT
    @Path("{id}/seguridad")
    public Response actualizarDatosSeguridad(@PathParam("id") int id, CuentaUsuario cuenta) {
        if (id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (this.cuentaUsuarioBO.obtener(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cuenta: " + id + ", no encontrada")
                    .build();
        }

        cuenta.setId(id);
        this.cuentaUsuarioBO.actualizarDatosSeguridad(cuenta);

        return Response.ok(cuenta).build();
    }

    @POST
    public Response crear(CuentaUsuario cuenta) {
        if (cuenta == null || cuenta.getUserName()== null ||
                cuenta.getUserName().isBlank() ||
                cuenta.getPassword() == null ||
                cuenta.getPassword().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La cuenta no es valida")
                    .build();
        }

        this.cuentaUsuarioBO.guardar(cuenta, Estado.NUEVO);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(cuenta.getId()))
                .build();

        return Response.created(location)
                .entity(cuenta)
                .build();
    }

}