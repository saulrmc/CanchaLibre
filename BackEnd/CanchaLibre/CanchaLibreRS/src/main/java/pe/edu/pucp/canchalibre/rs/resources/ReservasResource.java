package pe.edu.pucp.canchalibre.rs.resources;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pe.edu.pucp.canchalibre.bo.reserva.ReservaBO;
import pe.edu.pucp.canchalibre.bo.reserva.ReservaBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Path("/v1/reservas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservasResource {
    private final ReservaBO reservaBO;

    @Context
    private UriInfo uriInfo;

    public ReservasResource() {
        reservaBO = new ReservaBOImpl();
    }

    @GET
    public List<Reserva> listaReservas() {
        return reservaBO.listar();
    }

    @GET
    @Path("{id}")
    public Response obtenerReservaPorId(@PathParam("id") int idReserva) {
        if (idReserva <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Reserva reserva = reservaBO.obtener(idReserva);

        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reserva con id: "
                            + idReserva + ", no existe"))
                    .build();
        }

        return Response.ok(reserva).build();
    }

    @POST
    public Response crearReserva(Reserva reserva) {
        if (reserva == null ||
                reserva.getCliente() == null ||
                reserva.getCancha() == null ||
                reserva.getEstado() == null ||
                reserva.getBloquesSeleccionados() == null ||
                reserva.getBloquesSeleccionados().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para crear la reserva es inválido"))
                    .build();
        }

        reservaBO.guardar(reserva, Estado.NUEVO);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(reserva.getId()))
                .build();

        return Response.created(location)
                .entity(reserva)
                .build();
    }

    @PUT
    @Path("{id}")
    public Response actualizarReserva(Reserva reserva, @PathParam("id") int idReserva) {
        if (idReserva <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        if (reservaBO.obtener(idReserva) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reserva con id: "
                            + idReserva + ", no existe"))
                    .build();
        }

        if (reserva == null ||
                reserva.getCliente() == null ||
                reserva.getCancha() == null ||
                reserva.getEstado() == null ||
                reserva.getBloquesSeleccionados() == null ||
                reserva.getBloquesSeleccionados().isEmpty()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El payload para actualizar la reserva es inválido"))
                    .build();
        }

        reserva.setId(idReserva);

        reservaBO.guardar(reserva, Estado.MODIFICADO);

        return Response.ok(reserva).build();
    }

    @DELETE
    @Path("{id}")
    public Response eliminarReserva(@PathParam("id") int idReserva) {
        if (idReserva <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El ID es inválido"))
                    .build();
        }

        Reserva reserva = reservaBO.obtener(idReserva);

        if (reserva == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "La reserva con id: "
                            + idReserva + ", no existe"))
                    .build();
        }

        reservaBO.eliminar(idReserva);

        return Response.noContent().build();
    }
    @GET
    @Path("cliente/{id}")
    public List<Reserva> ListarPorClienteId(@PathParam("id") int idCliente){
        if(idCliente <= 0){
            return Collections.emptyList();
        }
        return reservaBO.listarReservasPorId(idCliente);
    }

    @GET
    @Path("cliente/username/{userName}")
    public List<Reserva> ListarPorClienteUsername(@PathParam("userName") String userName){
        return reservaBO.listarReservasPorCuenta(userName);
    }
}