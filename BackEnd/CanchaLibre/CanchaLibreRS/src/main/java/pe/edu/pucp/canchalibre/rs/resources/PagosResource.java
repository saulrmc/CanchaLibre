package pe.edu.pucp.canchalibre.rs.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.transaccion.PagoBO;
import pe.edu.pucp.canchalibre.bo.transaccion.PagoBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("v1/pagos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagosResource {
    private final PagoBO pagoBO;

    @Context
    private UriInfo uriInfo;

    public PagosResource() {
        pagoBO = new PagoBOImpl();
    }

    @GET
    public List<Pago> listaPagos() {
        return pagoBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerPagoPorId(@PathParam("id") int idPago) {
        if (idPago <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Pago pago = pagoBO.obtener(idPago);
        if (pago == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El pago con id: " + idPago + ", no existe"))
                    .build();
        }

        return Response.ok(pago).build();
    }

    @POST
    public Response crearPago(Pago pago) {
        if (payloadInvalido(pago)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el pago es inválido"))
                    .build();
        }

        pagoBO.guardar(pago, Estado.NUEVO);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(pago.getIdPago()))
                .build();

        return Response.created(location)
                .entity(pago)
                .build();
    }

    @POST
    @Path("reserva/{idReserva}")
    public Response crearPagoConReserva(Pago pago, @PathParam("idReserva") int idReserva) {
        if (idReserva <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID de reserva es inválido"))
                    .build();
        }

        if (pago == null || pago.getMetodoPago() == null || pago.getMonto() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear el pago es inválido"))
                    .build();
        }

        int id = pagoBO.insertarPago(pago, idReserva);
        if (id <= 0) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "No se pudo crear el pago"))
                    .build();
        }

        pago.setIdPago(id);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(location)
                .entity(pago)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarPago(Pago pago, @PathParam("id") int idPago) {
        if (idPago <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (pagoBO.obtener(idPago) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El pago con id: " + idPago + ", no existe"))
                    .build();
        }

        if (payloadInvalido(pago)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para actualizar el pago es inválido"))
                    .build();
        }

        pago.setIdPago(idPago);
        pagoBO.guardar(pago, Estado.MODIFICADO);

        return Response.ok(pago).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarPago(@PathParam("id") int idPago) {
        if (idPago <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Pago pago = pagoBO.obtener(idPago);
        if (pago == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "El pago con id: " + idPago + ", no existe"))
                    .build();
        }

        pagoBO.eliminar(idPago);

        return Response.noContent().build();
    }

    private boolean payloadInvalido(Pago pago) {
        return pago == null ||
                pago.getMetodoPago() == null ||
                pago.getMonto() <= 0;
    }
}
