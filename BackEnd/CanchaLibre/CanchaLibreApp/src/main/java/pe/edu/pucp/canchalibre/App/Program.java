package pe.edu.pucp.canchalibre.App;

import pe.edu.pucp.canchalibre.dao.transaccion.ComprobanteDAO;
import pe.edu.pucp.canchalibre.dao.transaccion.ComprobanteDAOImpl;
import pe.edu.pucp.canchalibre.dao.transaccion.PagoDAO;
import pe.edu.pucp.canchalibre.dao.transaccion.PagoDAOImpl;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.canchalibre.dao.usuario.ClienteDAO;
import pe.edu.pucp.canchalibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.canchalibre.dao.usuario.PropietarioDAO;
import pe.edu.pucp.canchalibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;

import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.cancha.Deporte;
import pe.edu.pucp.canchalibre.modelo.cancha.Etiqueta;
import pe.edu.pucp.canchalibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;
import pe.edu.pucp.canchalibre.modelo.transaccion.MetodoPago;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

import java.time.LocalDateTime;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        try {
            CuentaUsuarioDAO cuentaUsuarioDAO = new CuentaUsuarioDAOImpl();
            ClienteDAO clienteDAO = new ClienteDAOImpl();
            PropietarioDAO propietarioDAO = new PropietarioDAOImpl();
            CanchaDAO canchaDAO = new CanchaDAOImpl();
            PagoDAO pagoDAO = new PagoDAOImpl();
            ReservaDAO reservaDAO = new ReservaDAOImpl();
            ComprobanteDAO comprobanteDAO = new ComprobanteDAOImpl();

            Integer idCuentaCliente = null;
            Integer idCliente = null;
            Integer idCuentaPropietario = null;
            Integer idPropietario = null;
            Integer idCancha = null;
            Integer idPago = null;
            Integer idReserva = null;
            Integer idComprobante = null;

            try {
                // ── 1. CUENTA Y CLIENTE, SEGURIDAD ───────────────────────────────────────
                CuentaUsuario cuentaCliente = new CuentaUsuario();
                cuentaCliente.setUserName("maria_pichanga");
                cuentaCliente.setPassword("hash_seguro_123");
                cuentaCliente.setRol(Rol.CLIENTE);
                cuentaCliente.setIntentosFallidos(0);
                cuentaCliente.setUltimaSesion(null);
                cuentaCliente.setFechaBloqueo(null);
                cuentaCliente.setActivo(true); // Atributo base de Registro/Cuenta

                cuentaUsuarioDAO.crear(cuentaCliente);
                idCuentaCliente = cuentaCliente.getId(); // Se recupera el ID generado por el SP
                System.out.println("Cuenta de Cliente creada con ID: " + idCuentaCliente);

                Cliente cliente = new Cliente();
                cliente.setNombres("Maria Garcia");
                cliente.setCorreo("maria.garcia@test.com");
                cliente.setTelefono("999888777");
                cliente.setCalificacion(4.8);
                cliente.setCuentaUsuario(cuentaCliente); // Vinculamos el objeto cuenta completo

                clienteDAO.crear(cliente);
                idCliente = cliente.getId();
                System.out.println("Cliente creado de la BD: " + clienteDAO.leer(idCliente));

                Cliente clienteBuscado = clienteDAO.buscarPorCuenta("maria_pichanga");
                System.out.println("buscarPorCuenta funciona. Nombre recuperado: " + clienteBuscado.getNombres());

                cuentaCliente.setIntentosFallidos(cuentaCliente.getIntentosFallidos() + 1); // Sube a 1
                cuentaCliente.setUltimaSesion(LocalDateTime.now()); // Registramos el momento exacto del fallo
                cuentaUsuarioDAO.actualizarDatosSeguridad(cuentaCliente);
                Cliente clienteVerificado = clienteDAO.buscarPorCuenta("maria_pichanga");
                CuentaUsuario cuentaVerificada = clienteVerificado.getCuentaUsuario();

                System.out.println("actualizarDatosSeguridad funciona. Intentos fallidos reales en BD: "
                        + cuentaVerificada.getIntentosFallidos());

                // ── 2. CUENTA Y PROPIETARIO, SEGURIDAD ───────────────────────────────────
                CuentaUsuario cuentaPropietario = new CuentaUsuario();
                cuentaPropietario.setUserName("roberto_canchas");
                cuentaPropietario.setPassword("owner_pass_2026");
                cuentaPropietario.setRol(Rol.PROPIETARIO);
                cuentaPropietario.setIntentosFallidos(0);
                cuentaPropietario.setUltimaSesion(null);
                cuentaPropietario.setFechaBloqueo(null);
                cuentaPropietario.setActivo(true);

                cuentaUsuarioDAO.crear(cuentaPropietario);
                idCuentaPropietario = cuentaPropietario.getId();
                System.out.println("Cuenta de Propietario creada con ID: " + idCuentaPropietario);

                Propietario propietario = new Propietario();
                propietario.setNombres("Roberto Carlos Dueño");
                propietario.setCorreo("roberto.canchas@negocio.com");
                propietario.setTelefono("987654321");
                propietario.setCuentaUsuario(cuentaPropietario);

                propietario.setRUC("20123456789");        // Atributo String RUC
                propietario.setSaldo(0.00);               // Saldo inicial en double
                propietario.setCalificacion(5.0);         // Calificación double

                propietarioDAO.crear(propietario);
                idPropietario = propietario.getId();
                System.out.println("Propietario creado de la BD: " + propietarioDAO.leer(idPropietario));

                Propietario propBuscado = propietarioDAO.buscarPorCuenta("roberto_canchas");
                System.out.println("buscarPorCuenta de PropietarioDAO funciona. RUC recuperado: " + propBuscado.getRUC());

                // Simulamos que entró el dinero de una reserva (monto: 90.00)
                double nuevoSaldo = propietarioDAO.actualizarSaldo(null, idPropietario, 90.00);
                System.out.println("actualizarSaldo funciona. Nuevo saldo retornado por el SP: S/." + nuevoSaldo);

                Propietario propVerificado = propietarioDAO.leer(idPropietario);
                System.out.println("Saldo real verificado en la tabla propietario: S/." + propVerificado.getSaldo());

                // ── 3. CANCHA ─────────────────────────────────────────────────
                Cancha cancha = new Cancha();
                cancha.setNombre("Estadio Central P10");
                cancha.setDescripcion("Cancha de césped sintético con iluminación nocturna profesional.");
                cancha.setDireccion("Av. Deporte 123, Lima");
                cancha.setImagenUrl("https://images.test.com/cancha1.jpg");
                cancha.setPropietario(propietario); // Amarrado al propietario de la Etapa 2
                cancha.setActivo(true); // Heredado de Registro

                cancha.setDeportes(List.of(Deporte.FUTBOL));
                cancha.setEtiquetas(List.of(Etiqueta.ILUMINACION, Etiqueta.PARKING));
                cancha.setPrecioBase(120.00);
                cancha.setPromedioCalificacion(4.5);

                canchaDAO.crear(cancha);
                idCancha = cancha.getId();
                System.out.println("Cancha creada de la BD: " + canchaDAO.leer(idCancha));

                // Probar actualización de campos nuevos (Mantenimiento)
                cancha.setNombre("Estadio Central - Remodelado");
                cancha.setPrecioBase(135.00); // Subió el precio por la mejora
                canchaDAO.actualizar(cancha);
                System.out.println("Cancha actualizada en BD. Nuevo precio base: S/." + canchaDAO.leer(idCancha).getPrecioBase());

                List<Cancha> canchasDelDueno = canchaDAO.listarCanchasPorCuenta("roberto_canchas");
                System.out.println("listarCanchasPorCuenta funciona. Total canchas de Roberto: " + canchasDelDueno.size());

                // ── 4. RESERVA ────────────────────────────────────────────────
                Reserva reserva = new Reserva();
                reserva.setEstado(EstadoReserva.PENDIENTE_PAGO);
                reserva.setCliente(cliente); // Amarrado al cliente de la Etapa 1
                reserva.setCancha(cancha);   // Amarrado a la cancha de la Etapa 3
                reserva.setPago(null);       // Aún no se ha pagado en este momento del flujo

                // NOTA DE DISEÑO:
                // Los bloquesSeleccionados se guardarán masivamente en lote en la BD
                // usando tu BloqueHorarioDAO.crearBloquesPorReserva(conn, idReserva, lista)
                // en la capa de Service. Aquí inicializamos la lista vacía para cumplir el modelo.
                reserva.setBloquesSeleccionados(new java.util.ArrayList<>());

                reservaDAO.crear(reserva);
                idReserva = reserva.getIdReserva(); // Se recupera el ID generado por el IDENTITY de SQL
                System.out.println("Reserva creada de la BD: " + reservaDAO.leer(idReserva));

                List<Reserva> historialCliente = reservaDAO.listarReservasPorCuenta("maria_pichanga");
                System.out.println("listarReservasPorCuenta funciona. Total pichangas de María: " + historialCliente.size());

                // ── 5. PAGO Y COMPROBANTE, VALIDACIÓN DE RESERVA ──────────────────────
                Pago pago = new Pago();
                pago.setMetodoPago(MetodoPago.YAPE);
                pago.setFechaPago(LocalDateTime.now());
                pago.setComprobante(null); // Nace en null antes de la emisión de la boleta

                Comprobante comprobante = new Comprobante();
                comprobante.setSerie("B001");
                comprobante.setNumero("00000045");
                comprobante.setFechaEmision(LocalDateTime.now());
                comprobante.setMontoBloques(120.00); // Subtotal por el alquiler de la cancha

                // Cálculos limpios usando tus métodos expuestos
                double totalBruto = comprobante.getMontoBloques() + comprobante.getComisionPlataforma();
                double valorVentaCalculado = totalBruto / 1.18;
                double igvCalculado = totalBruto - valorVentaCalculado;
                comprobante.setValorVenta(valorVentaCalculado);
                comprobante.setMontoIgv(igvCalculado);

                pago.setMonto(totalBruto);

                pagoDAO.crear(pago);
                idPago = pago.getIdPago();
                System.out.println("Pago creado de la BD con ID: " + idPago);

                comprobanteDAO.crear(comprobante);
                idComprobante = comprobante.getIdComprobante();
                System.out.println("Comprobante creado de la BD: " + idComprobante);

                pago.setComprobante(comprobante); // El pago ya tiene su comprobante asociado
                reserva.setPago(pago);            // La reserva ya tiene su pago asociado

                if (reserva.getPago() != null && reserva.getPago().getComprobante() != null) {
                    reserva.setEstado(EstadoReserva.CONFIRMADA);
                    reservaDAO.actualizar(reserva);
                    System.out.println("¡Validación aprobada! Estado de reserva actualizado en BD a: "
                            + reservaDAO.leer(idReserva).getEstado());
                } else {
                    System.err.println("Alerta de consistencia: No se puede confirmar la reserva sin un comprobante emitido.");
                }

                // Reportes finales de verificación de estado
                System.out.println("\n--- VERIFICACIÓN DE TOTALES ---");
                System.out.println("Total reservas en BD: " + reservaDAO.leerTodos().size());
                System.out.println("Total clientes en BD: " + clienteDAO.leerTodos().size());
                System.out.println("Total canchas en BD: " + canchaDAO.leerTodos().size());
                System.out.println("\nFlujo de prueba completado exitosamente.");

            } finally {
                // ── LIMPIEZA ATÓMICA EN ORDEN INVERSO ─────────────────────────
                System.out.println("\n--- INICIANDO LIMPIEZA DE PRUEBAS ---");
                if (idComprobante != null) { comprobanteDAO.eliminar(idComprobante); System.out.println("Comprobante eliminado."); }
                if (idPago != null) { pagoDAO.eliminar(idPago); System.out.println("Pago eliminado."); }
                if (idReserva != null) { reservaDAO.eliminar(idReserva); System.out.println("Reserva eliminada."); }
                if (idCancha != null) { canchaDAO.eliminar(idCancha); System.out.println("Cancha eliminada."); }
                if (idPropietario != null) { propietarioDAO.eliminar(idPropietario); System.out.println("Propietario eliminado."); }
                if (idCuentaPropietario != null) { cuentaUsuarioDAO.eliminar(idCuentaPropietario); System.out.println("Cuenta Propietario eliminada."); }
                if (idCliente != null) { clienteDAO.eliminar(idCliente); System.out.println("Cliente eliminado."); }
                if (idCuentaCliente != null) { cuentaUsuarioDAO.eliminar(idCuentaCliente); System.out.println("Cuenta Cliente eliminada."); }
                System.out.println("Limpieza final completada de forma segura.");
            }
        } catch (Exception e) {
            System.err.println("Error fatal detectado en la prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}