package pe.edu.pucp.canchalibre.rs.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.canchalibre.bo.cuentas.CuentaUsuarioBO;
import pe.edu.pucp.canchalibre.bo.cuentas.CuentaUsuarioBOImpl;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;

import java.util.Map;

@Path("v1/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuentasUsuarioResource {

    private final CuentaUsuarioBO cuentaUsuarioBO;

    public CuentasUsuarioResource() {
        cuentaUsuarioBO = new CuentaUsuarioBOImpl();
    }

    @POST
    @Path("login")
    public Response login(CuentaUsuario cuenta) {
        if (cuenta == null ||
                cuenta.getUserName() == null ||
                cuenta.getPassword() == null ||
                cuenta.getUserName().isBlank() ||
                cuenta.getPassword().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Credenciales inválidas"))
                    .build();
        }

        boolean ok = cuentaUsuarioBO.login(cuenta.getUserName(), cuenta.getPassword());

        if (!ok) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "Usuario o contraseña incorrectos"))
                    .build();
        }

        return Response.ok(Map.of("mensaje", "Login correcto")).build();
    }
}