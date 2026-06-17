package pe.edu.pucp.canchalibre.App;

import pe.edu.pucp.CanchaLibre.dao.transaccion.ComprobanteDAO;
import pe.edu.pucp.CanchaLibre.dao.transaccion.ComprobanteDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.transaccion.PagoDAO;
import pe.edu.pucp.CanchaLibre.dao.transaccion.PagoDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.CanchaLibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.CanchaLibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.cancha.Deporte;
import pe.edu.pucp.canchalibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;
import pe.edu.pucp.canchalibre.modelo.transaccion.MetodoPago;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        try {
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            PropietarioDAO propietarioDAO = new PropietarioDAOImpl();
            CanchaDAO canchaDAO = new CanchaDAOImpl();
            PagoDAO pagoDAO = new PagoDAOImpl();
            ReservaDAO reservaDAO = new ReservaDAOImpl();
            ComprobanteDAO comprobanteDAO = new ComprobanteDAOImpl();

            Integer idCliente = null;
            Integer idPropietario = null;
            Integer idCancha = null;
            Integer id = null;
            Integer idReserva = null;
            Integer idComprobante = null;

            try {
                // ── 1. CLIENTE ──────────────────────────────────────────────── OK
                Cliente cliente = new Cliente();
                cliente.setNombre("Maria");
                cliente.setApellidoPaterno("Garcia");
                cliente.setCorreo("maria.garcia@test.com");
                //cliente.set("clave123");
                cliente.setTelefono("999888777");
                cliente.setCalificacion(5);
                //cliente.setIntentosFallidos(0);
                //cliente.setUltimaSesion(LocalDateTime.now());

                idCliente = clienteDAO.crear(cliente);
                cliente.setIdUsuario(idCliente);
                System.out.println("Cliente creado: " + clienteDAO.leer(idCliente));

                cliente.setNombre("Maria Garcia");
                cliente.setApellidoPaterno("Lopez");
                cliente.setCalificacion(4);
                clienteDAO.actualizar(cliente);
                System.out.println("Cliente actualizado: " + clienteDAO.leer(idCliente));

                System.out.println("Buscar por nombre: " + clienteDAO.buscarPorNombre("Maria Garcia Lopez"));

//                // ── 2. PROPIETARIO ──────────────────────────────────────────────── OK
                Propietario propietario = new Propietario();
                propietario.setNombre("Roberto");
                propietario.setApellidoPaterno("Tueño");
                propietario.setCorreo("roberto.canchas2@negocio.com");
                //propietario.setContrasena("adminPass2026");
                propietario.setTelefono("987654321");
                //propietario.setIntentosFallidos(0);
                //propietario.setUltimaSesion(LocalDateTime.now());
                propietario.setCalificacion(5);

                idPropietario = propietarioDAO.crear(propietario);
                propietario.setIdUsuario(idPropietario);
                System.out.println("Propietario creado: " + propietarioDAO.leer(idPropietario));

                propietario.setNombre("Roberto Carlos");
                propietario.setApellidoPaterno("Dueño");
                propietario.setCalificacion(4);
                propietarioDAO.actualizar(propietario);
                System.out.println("Propietario actualizado: " + propietarioDAO.leer(idPropietario));

                System.out.println("Buscar por nombre: " + propietarioDAO.buscarPorNombre("Roberto Carlos Dueño"));

                // ── 3. CANCHA ────────────────────────────────────────────────
                Cancha cancha = new Cancha();
                cancha.setNombre("Estadio Central P10");
                cancha.setDescripcion("Cancha de césped sintético con iluminación nocturna profesional.");
                cancha.setDeportes(List.of(Deporte.FUTBOL));
                cancha.setImagenUrl("https://images.test.com/cancha1.jpg");
                cancha.setDisponible(true);
                cancha.setDireccion("Av. Deporte 123, Lima");
                cancha.setPropietario(propietario);

                idCancha = canchaDAO.crear(cancha);
                cancha.setIdCancha(idCancha);
                System.out.println("Cancha creada: " + canchaDAO.leer(idCancha));

                cancha.setNombre("Estadio Central - Renovado");
                cancha.setDisponible(false); // Change status to occupied/maintenance
                cancha.setDescripcion("Cancha cerrada temporalmente por mantenimiento de césped.");
                canchaDAO.actualizar(cancha);
                System.out.println("Cancha actualizada: " + canchaDAO.leer(idCancha));
//              TODO: implement comandoLeerDeportesPorCancha()

//                ── 4. RESERVA ────────────────────────────────────────────────
                Reserva reserva = new Reserva();
                reserva.setFechaHora(LocalDateTime.of(2025, 6, 15, 10, 0));
                reserva.setDuracion(LocalTime.of(1, 30));
                reserva.setEstado(EstadoReserva.ESPERA);
                reserva.setCliente(cliente);
                reserva.setCancha(cancha);

                idReserva = reservaDAO.crear(reserva);
                reserva.setIdReserva(idReserva);

                System.out.println("Reserva creada: " + reservaDAO.leer(idReserva));


//                ── 5. PAGO ───────────────────────────────────────────────────
                Pago pago = new Pago();
                pago.setMetodoPago(MetodoPago.EFECTIVO);
                pago.setMonto(90.00);
                pago.setFechaPago(LocalDateTime.now());
                pago.setReserva(reserva);

                id = pagoDAO.crear(pago);
                pago.setId(id);

                reserva.setPago(pago);

                System.out.println("Pago creado: " + pagoDAO.leer(id));

                pago.setMonto(100.00);
                pagoDAO.actualizar(pago);

                System.out.println("Pago actualizado: " + pagoDAO.leer(id));


//                ── 6. ACTUALIZAR RESERVA ─────────────────────────────────────
                reserva.setEstado(EstadoReserva.COMPLETADO);
                reserva.setDuracion(LocalTime.of(2, 0));

                reservaDAO.actualizar(reserva);

                System.out.println("Reserva actualizada: " + reservaDAO.leer(idReserva));


//                ── 7. COMPROBANTE ────────────────────────────────────────────
                Comprobante comprobante = new Comprobante();
                comprobante.setIgv(0.18);
                comprobante.setFechaEmision(LocalDateTime.now());
                comprobante.setReserva(reserva);

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
                // ── LIMPIEZA (orden inverso a la inserción) ───────────────────
                if (idComprobante != null) {
                    comprobanteDAO.eliminar(idComprobante);
                    System.out.println("Comprobante eliminado.");
                }
                if (idReserva != null) {
                    reservaDAO.eliminar(idReserva);
                    System.out.println("Reserva eliminada.");
                }
                if (id != null) {
                    pagoDAO.eliminar(id);
                    System.out.println("Pago eliminado.");
                }
                if (idCancha != null) {
                    canchaDAO.eliminar(idCancha);
                    System.out.println("Cancha eliminada.");
                }
                if (idPropietario != null) {
                    propietarioDAO.eliminar(idPropietario);
                    System.out.println("Propietario eliminado.");
                }
                if (idCliente != null) {
                    clienteDAO.eliminar(idCliente);
                    System.out.println("Cliente eliminado.");
                }
                System.out.println("Limpieza final completada.");
            }
        } catch (Exception e) {
            System.err.println("Error fatal en la aplicación: " + e.getMessage());
        }
    }
}
