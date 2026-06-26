package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.transaccion.ComprobanteBO;
import pe.edu.pucp.canchalibre.bo.transaccion.ComprobanteBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("v1/comprobantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComprobantesResource {
    private final ComprobanteBO comprobanteBO;

    @Context
    private UriInfo uriInfo;

    public ComprobantesResource() {
        comprobanteBO = new ComprobanteBOImpl();
    }

    @GET
    public List<Comprobante> listaComprobantes() {
        return comprobanteBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerComprobantePorId(@PathParam("id") int idComprobante) {
        if (idComprobante < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Comprobante comprobante = comprobanteBO.obtener(idComprobante);
        if (comprobante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El comprobante con id:  "
                            + idComprobante + ", no existe"))
                    .build();
        }

        return Response.ok(comprobante).build();
    }

    @POST
    public Response crearComprobante(Comprobante comprobante) {
        if (comprobante == null ||
            comprobante.getFechaEmision() == null ) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el comprobante es inválido"))
                    .build();
        }

        comprobanteBO.guardar(comprobante, Estado.NUEVO);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(comprobante.getIdComprobante()))
                .build();

        return Response.created(location)
                .entity(comprobante)
                .build();
    }

    @POST
    @Path("reserva/{idReserva}")
    public Response crearComprobanteConReserva(Comprobante comprobante, @PathParam("idReserva") int idReserva) {
        if (idReserva <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID de reserva es inválido"))
                    .build();
        }

        if (comprobante == null || comprobante.getSerie() == null || comprobante.getSerie().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el comprobante es inválido"))
                    .build();
        }

        int id = comprobanteBO.insertarComprobante(comprobante, idReserva);
        if (id <= 0) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "No se pudo crear el comprobante"))
                    .build();
        }

        comprobante.setIdComprobante(id);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(location)
                .entity(comprobante)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarComprobante(Comprobante comprobante, @PathParam("id") int idComprobante) {
        if (idComprobante < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (comprobanteBO.obtener(idComprobante) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El comprobante con id:  "
                            + idComprobante + ", no existe"))
                    .build();
        }

        if (comprobante == null ||
            comprobante.getFechaEmision() == null ) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el comprobante es inválido"))
                    .build();
        }

        comprobanteBO.guardar(comprobante, Estado.MODIFICADO);

        return Response.ok(comprobante).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarComprobante(@PathParam("id") int idComprobante) {
        if (idComprobante < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Comprobante comprobante = comprobanteBO.obtener(idComprobante);
        if (comprobante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El comprobante con id:  "
                            + idComprobante + ", no existe"))
                    .build();
        }

        comprobanteBO.eliminar(idComprobante);

        return Response.noContent().build();
    }
}
