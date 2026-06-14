package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.usuario.ClienteBO;
import pe.edu.pucp.canchalibre.bo.usuario.ClienteBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class ClientesResource {
    private final ClienteBO clienteBO;

    @Context
    private UriInfo uriInfo;

    public ClientesResource() {
        clienteBO = new ClienteBOImpl();
    }

    @GET
    public List<Cliente> listaClientes() {
        return clienteBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerClientePorId(@PathParam("id") int idCliente) {
        if (idCliente < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Cliente cliente = clienteBO.obtener(idCliente);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El cliente con id:  "
                            + idCliente + ", no existe"))
                    .build();
        }

        return Response.ok(cliente).build();
    }

    @POST
    public Response crearCliente(Cliente cliente) {
        if (cliente == null ||
            cliente.getCorreo() == null ||
            cliente.getContrasena() == null ||
            cliente.getNombres() == null ||
            cliente.getContrasena().isBlank() ||
            cliente.getCorreo().isBlank() ||
            cliente.getNombres().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la cliente es inválido"))
                    .build();
        }

        clienteBO.crear(cliente, Estado.Nuevo);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(cliente.getIdUsuario()))
                .build();

        return Response.created(location)
                .entity(cliente)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarCliente(Cliente cliente, @PathParam("id") int idCliente) {
        if (idCliente < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (clienteBO.obtener(idCliente) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El cliente con id:  "
                            + idCliente + ", no existe"))
                    .build();
        }

        if (cliente == null ||
            cliente.getCorreo() == null ||
            cliente.getContrasena() == null ||
            cliente.getNombres() == null ||
            cliente.getContrasena().isBlank() ||
            cliente.getCorreo().isBlank() ||
            cliente.getNombres().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el cliente es inválido"))
                    .build();
        }

        clienteBO.crear(cliente, Estado.Modificado);

        return Response.ok(cliente).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarCliente(@PathParam("id") int idCliente) {
        if (idCliente < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Cliente cliente = clienteBO.obtener(idCliente);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El cliente con id:  "
                            + idCliente + ", no existe"))
                    .build();
        }

        clienteBO.eliminar(idCliente);

        return Response.noContent().build();
    }
}
