package pe.edu.pucp.CanchaLibre.App;

import pe.edu.pucp.CanchaLibre.dao.Transaccion.ComprobanteDAO;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.ComprobanteDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.PagoDAO;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.PagoDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.CanchaLibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.Reserva.EstadoReserva;
import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Comprobante;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.MetodoPago;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Pago;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Cliente;
import pe.edu.pucp.CanchaLibre.db.utils.TipoDB.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Program {
    public static void main(String[] args) {

        ClienteDAO clienteDAO       = new ClienteDAOImpl();
        PagoDAO pagoDAO             = new PagoDAOImpl();
        ReservaDAO reservaDAO       = new ReservaDAOImpl();
        ComprobanteDAO comprobanteDAO = new ComprobanteDAOImpl();

        Integer idCliente     = null;
        Integer idPago        = null;
        Integer idReserva     = null;
        Integer idComprobante = null;

        try {
            // 1. CLIENTE
            Cliente cliente = new Cliente();
            cliente.setNombres("Maria Garcia");
            cliente.setCorreo("maria.garcia@test.com");
            cliente.setContrasena("clave123");
            cliente.setTelefono("999888777");
            cliente.setCalificacion(5);
            cliente.setIntentosFallidos(0);
            cliente.setUltimaSesion(LocalDateTime.now());

            idCliente = clienteDAO.crear(cliente);
            cliente.setIdUsuario(idCliente);
            System.out.println("Cliente creado: " + clienteDAO.leer(idCliente));

            cliente.setNombres("Maria Garcia Lopez");
            cliente.setCalificacion(4);
            clienteDAO.actualizar(cliente);
            System.out.println("Cliente actualizado: " + clienteDAO.leer(idCliente));

            System.out.println("Buscar por nombre: " + clienteDAO.buscarPorNombre("Maria Garcia Lopez"));

            // 2. PAGO
            Pago pago = new Pago();
            pago.setMetodoPago(MetodoPago.EFECTIVO);
            pago.setMonto(90.00);
            pago.setFechaPago(LocalDateTime.now());

            idPago = pagoDAO.crear(pago);
            pago.setId(idPago);
            System.out.println("Pago creado: " + pagoDAO.leer(idPago));

            pago.setMonto(100.00);
            pagoDAO.actualizar(pago);
            System.out.println("Pago actualizado: " + pagoDAO.leer(idPago));

            // 3. RESERVA
            // Cancha hardcodeada con un id ya existente en la BD
            Cancha cancha = new Cancha();
            cancha.setIdCancha(1);

            Reserva reserva = new Reserva();
            reserva.setFechaHora(LocalDateTime.of(2025, 6, 15, 10, 0));
            reserva.setDuracion(LocalTime.of(1, 30));
            reserva.setEstado(EstadoReserva.ESPERA);
            reserva.setCliente(cliente);
            reserva.setCancha(cancha);
            reserva.setPago(pago);

            idReserva = reservaDAO.crear(reserva);
            reserva.setIdReserva(idReserva);
            System.out.println("Reserva creada: " + reservaDAO.leer(idReserva));

            reserva.setEstado(EstadoReserva.COMPLETADO);
            reserva.setDuracion(LocalTime.of(2, 0));
            reservaDAO.actualizar(reserva);
            System.out.println("Reserva actualizada: " + reservaDAO.leer(idReserva));

            // 4. COMPROBANTE
            Comprobante comprobante = new Comprobante();
            comprobante.setIgv(0.18);
            comprobante.setFechaEmision(LocalDateTime.now());
            comprobante.setReserva(reserva);   // la reserva ya tiene pago con monto

            idComprobante = comprobanteDAO.crear(comprobante);
            comprobante.setIdComprobante(idComprobante);
            System.out.println("Comprobante creado: " + comprobanteDAO.leer(idComprobante));

            comprobante.getReserva().getPago().setMonto(118.00);
            comprobanteDAO.actualizar(comprobante);
            System.out.println("Comprobante actualizado: " + comprobanteDAO.leer(idComprobante));

            System.out.println("Total reservas en BD: " + reservaDAO.leerTodos().size());
            System.out.println("Total clientes en BD: " + clienteDAO.leerTodos().size());
            System.out.println("\nFlujo de prueba completado exitosamente.");

        } finally {
            // LIMPIEZA (orden inverso a la inserción)
            if (idComprobante != null) {
                comprobanteDAO.eliminar(idComprobante);
                System.out.println("Comprobante eliminado.");
            }
            if (idReserva != null) {
                reservaDAO.eliminar(idReserva);
                System.out.println("Reserva eliminada.");
            }
            if (idPago != null) {
                pagoDAO.eliminar(idPago);
                System.out.println("Pago eliminado.");
            }
            if (idCliente != null) {
                clienteDAO.eliminar(idCliente);
                System.out.println("Cliente eliminado.");
            }
            System.out.println("Limpieza final completada.");
        }
    }
}
