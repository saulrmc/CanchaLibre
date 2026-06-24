package pe.edu.pucp.canchalibre.App;

import pe.edu.pucp.canchalibre.bo.cuentas.CuentaUsuarioBO;
import pe.edu.pucp.canchalibre.bo.cuentas.CuentaUsuarioBOImpl;
import pe.edu.pucp.canchalibre.bo.usuario.ClienteBO;
import pe.edu.pucp.canchalibre.bo.usuario.ClienteBOImpl;
import pe.edu.pucp.canchalibre.bo.usuario.PropietarioBO;
import pe.edu.pucp.canchalibre.bo.usuario.PropietarioBOImpl;
import pe.edu.pucp.canchalibre.bo.cancha.CanchaBO;
import pe.edu.pucp.canchalibre.bo.cancha.CanchaBOImpl;
import pe.edu.pucp.canchalibre.bo.reserva.ReservaBO;
import pe.edu.pucp.canchalibre.bo.reserva.ReservaBOImpl;
import pe.edu.pucp.canchalibre.bo.transaccion.PagoBO;
import pe.edu.pucp.canchalibre.bo.transaccion.PagoBOImpl;
import pe.edu.pucp.canchalibre.bo.transaccion.ComprobanteBO;
import pe.edu.pucp.canchalibre.bo.transaccion.ComprobanteBOImpl;

import pe.edu.pucp.canchalibre.modelo.Estado;
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
import java.util.ArrayList;

public class Program {
    public static void main(String[] args) {
        String sufijo = String.valueOf(System.currentTimeMillis());

        // ── INSTANCIACIÓN DE LAS CAPAS DE NEGOCIO (BO) ───────────────────────────
        CuentaUsuarioBO cuentaUsuarioBO = new CuentaUsuarioBOImpl();
        ClienteBO clienteBO = new ClienteBOImpl();
        PropietarioBO propietarioBO = new PropietarioBOImpl();
        CanchaBO canchaBO = new CanchaBOImpl();
        PagoBO pagoBO = new PagoBOImpl();
        ReservaBO reservaBO = new ReservaBOImpl();
        ComprobanteBO comprobanteBO = new ComprobanteBOImpl();

        Integer idCuentaCliente = null;
        Integer idCliente = null;
        Integer idCuentaPropietario = null;
        Integer idPropietario = null;
        Integer idCancha = null;
        Integer idPago = null;
        Integer idReserva = null;
        Integer idComprobante = null;

        try {
            // ── 1. CUENTA Y CLIENTE (NEGOCIO) ────────────────────────────────────────
            CuentaUsuario cuentaCliente = new CuentaUsuario();
            cuentaCliente.setUserName("maria_" + sufijo);
            cuentaCliente.setPassword("hash_seguro_123");
            cuentaCliente.setRol(Rol.CLIENTE);
            cuentaCliente.setIntentosFallidos(0);
            cuentaCliente.setActivo(true);

            // Guardar maneja internamente la creación
            cuentaUsuarioBO.guardar(cuentaCliente, Estado.NUEVO);
            idCuentaCliente = cuentaCliente.getId();
            System.out.println("Cuenta de Cliente creada via BO con ID: " + idCuentaCliente);

            Cliente cliente = new Cliente();
            cliente.setNombres("Maria Garcia");
            cliente.setCorreo("maria." + sufijo + "@test.com");
            cliente.setTelefono("999888777");
            cliente.setCalificacion(4.8);
            cliente.setCuentaUsuario(cuentaCliente);

            clienteBO.guardar(cliente, Estado.NUEVO);
            idCliente = cliente.getId();
            System.out.println("Cliente creado via BO: " + clienteBO.obtener(idCliente).getNombres());

            // ── 2. CUENTA Y PROPIETARIO (NEGOCIO) ────────────────────────────────────
            CuentaUsuario cuentaPropietario = new CuentaUsuario();
            cuentaPropietario.setUserName("roberto_" + sufijo);
            cuentaPropietario.setPassword("owner_pass_2026");
            cuentaPropietario.setRol(Rol.PROPIETARIO);
            cuentaPropietario.setIntentosFallidos(0);
            cuentaPropietario.setActivo(true);

            cuentaUsuarioBO.guardar(cuentaPropietario, Estado.NUEVO);
            idCuentaPropietario = cuentaPropietario.getId();

            Propietario propietario = new Propietario();
            propietario.setNombres("Roberto Carlos Dueño");
            propietario.setCorreo("roberto." + sufijo + "@negocio.com");
            propietario.setTelefono("987654321");
            propietario.setCuentaUsuario(cuentaPropietario);
            propietario.setRUC("20123456789");
            propietario.setSaldo(0.00);
            propietario.setCalificacion(5.0);

            propietarioBO.guardar(propietario, Estado.NUEVO);
            idPropietario = propietario.getId();
            System.out.println("Propietario creado via BO con ID: " + idPropietario);

            // Simulación de abono financiero controlado por la capa BO
            propietarioBO.actualizarSaldo(idPropietario, 90.00);
            System.out.println("Saldo verificado del Propietario tras abono: S/." + propietarioBO.obtener(idPropietario).getSaldo());

            // ── 3. CANCHA (NEGOCIO) ──────────────────────────────────────────────────
            Cancha cancha = new Cancha();
            cancha.setNombre("Estadio Central P10 " + sufijo);
            cancha.setDescripcion("Cancha de césped sintético con iluminación nocturna.");
            cancha.setDireccion("Av. Deporte 123, Lima");
            cancha.setImagenUrl("https://images.test.com/cancha1.jpg");
            cancha.setPropietario(propietario);
            cancha.setActivo(true);
            cancha.setDeportes(java.util.List.of(Deporte.FUTBOL));
            cancha.setEtiquetas(java.util.List.of(Etiqueta.ILUMINACION, Etiqueta.PARKING));
            cancha.setPrecioBase(120.00);
            cancha.setPromedioCalificacion(4.5);

            canchaBO.guardar(cancha, Estado.NUEVO);
            idCancha = cancha.getId();
            System.out.println("Cancha registrada con éxito. Nombre: " + canchaBO.obtener(idCancha).getNombre());

            // Modificación por mantenimiento vía BO
            cancha.setPrecioBase(135.00);
            canchaBO.guardar(cancha, Estado.MODIFICADO);

            // ── 4. RESERVA, PAGO Y COMPROBANTE ───────────────────────────────────────
            Reserva reserva = new Reserva();
            reserva.setEstado(EstadoReserva.PENDIENTE_PAGO);
            reserva.setCliente(cliente);
            reserva.setCancha(cancha);
            reserva.setPago(null);
            reserva.setBloquesSeleccionados(new ArrayList<>()); // Inicialización estructural

            reservaBO.guardar(reserva, Estado.NUEVO);
            idReserva = reserva.getIdReserva();
            System.out.println("Reserva creada en estado inicial: " + reservaBO.obtener(idReserva).getEstado());

            // Emisión de transacciones financieras controladas por BO
            Comprobante comprobante = new Comprobante();
            comprobante.setSerie("B001");
            comprobante.setNumero((sufijo).substring(0, 8));
            comprobante.setFechaEmision(LocalDateTime.now());
            comprobante.setMontoBloques(120.00);

            double totalBruto = comprobante.getMontoBloques() + comprobante.getComisionPlataforma();
            comprobante.setValorVenta(totalBruto / 1.18);
            comprobante.setMontoIgv(totalBruto - comprobante.getValorVenta());

            comprobanteBO.guardar(comprobante, Estado.NUEVO);
            idComprobante = comprobante.getIdComprobante();

            Pago pago = new Pago();
            pago.setMetodoPago(MetodoPago.YAPE);
            pago.setFechaPago(LocalDateTime.now());
            pago.setMonto(totalBruto);
            pago.setComprobante(comprobante);

            pagoBO.guardar(pago, Estado.NUEVO);
            idPago = pago.getIdPago();

            // Transición lógica del negocio: Confirmación de reserva segura
            reserva.setPago(pago);
            reserva.setEstado(EstadoReserva.CONFIRMADA);

            // validarReserva se activa automáticamente aquí dentro para proteger el negocio
            reservaBO.guardar(reserva, Estado.MODIFICADO);
            System.out.println("¡Validación y confirmación exitosa vía BO! Estado final: "
                    + reservaBO.obtener(idReserva).getEstado());

            System.out.println("\n--- REPORTE FINAL DE BIEVENIDA DE LA RED ---");
            System.out.println("Total reservas activas: " + reservaBO.listar().size());
            System.out.println("Flujo de negocio completado.");

        } catch (Exception e) {
            System.err.println("Error de negocio detectado en el flujo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ── LIMPIEZA CRONOLÓGICA INVERSA USANDO BO ───────────────────────────────
            System.out.println("\n--- INICIANDO LIMPIEZA DE ENTORNO ---");
            try {
                if (idComprobante != null) { comprobanteBO.eliminar(idComprobante); System.out.println("Comprobante removido."); }
                if (idPago != null) { pagoBO.eliminar(idPago); System.out.println("Pago removido."); }
                if (idReserva != null) { reservaBO.eliminar(idReserva); System.out.println("Reserva removida."); }
                if (idCancha != null) { canchaBO.eliminar(idCancha); System.out.println("Cancha removida."); }
                if (idPropietario != null) { propietarioBO.eliminar(idPropietario); System.out.println("Propietario removido."); }
                if (idCuentaPropietario != null) { cuentaUsuarioBO.eliminar(idCuentaPropietario); System.out.println("Cuenta Propietario desvinculada."); }
                if (idCliente != null) { clienteBO.eliminar(idCliente); System.out.println("Cliente removido."); }
                if (idCuentaCliente != null) { cuentaUsuarioBO.eliminar(idCuentaCliente); System.out.println("Cuenta Cliente desvinculada."); }
                System.out.println("Limpieza por capas completada de forma segura.");
            } catch (Exception ex) {
                System.err.println("Excepción durante el vaciado de tablas latentes: " + ex.getMessage());
            }
        }
    }
}