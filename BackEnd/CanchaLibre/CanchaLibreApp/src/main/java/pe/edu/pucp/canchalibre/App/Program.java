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
import pe.edu.pucp.canchalibre.modelo.cancha.*;
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

        Reserva reservaContexto = null;

        try {
            // ── 1. CUENTA Y CLIENTE (NEGOCIO) ────────────────────────────────────────
            CuentaUsuario cuentaCliente = new CuentaUsuario();
            cuentaCliente.setUserName("maria_" + sufijo);
            cuentaCliente.setPassword("hash_seguro_123");
            cuentaCliente.setRol(Rol.CLIENTE);
            cuentaCliente.setIntentosFallidos(0);
            cuentaCliente.setActivo(true);

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
            propietario.setTelefono("987604321");
            propietario.setCuentaUsuario(cuentaPropietario);
            propietario.setRUC("20511340281");
            propietario.setSaldo(0.00);
            propietario.setCalificacion(5.0);

            propietarioBO.guardar(propietario, Estado.NUEVO);
            idPropietario = propietario.getId();
            System.out.println("Propietario creado via BO con ID: " + idPropietario);

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

            var bloquesCancha = new ArrayList<BloqueHorario>();
            BloqueHorario bloque1 = new BloqueHorario();
            bloque1.setHoraInicio(java.time.LocalTime.of(18, 0)); // 6:00 PM
            bloque1.setHoraFin(java.time.LocalTime.of(19, 0));    // 7:00 PM
            bloque1.setDia(DiaSemana.LUNES);     // Lunes
            bloque1.setActivo(true);
            bloque1.setPrecio(80.00);
            bloque1.setEstado(EstadoBloque.DISPONIBLE);
            bloquesCancha.add(bloque1);

            BloqueHorario bloque2 = new BloqueHorario();
            bloque2.setHoraInicio(java.time.LocalTime.of(19, 0));
            bloque2.setHoraFin(java.time.LocalTime.of(20, 0));
            bloque2.setDia(DiaSemana.LUNES);
            bloque2.setActivo(true);
            bloque2.setEstado(EstadoBloque.MANTENIMIENTO);
            bloquesCancha.add(bloque2);

            // Asignamos la lista a la cancha para pasar la validación
            cancha.setBloques(bloquesCancha);
            canchaBO.guardar(cancha, Estado.NUEVO); //en estado.Nuevo usara precio base para todos los bloques
            idCancha = cancha.getId();
            System.out.println("Cancha registrada con éxito. Nombre: " + canchaBO.obtener(idCancha).getNombre());

            canchaBO.guardar(cancha, Estado.MODIFICADO);

            // ── 4. RESERVA, PAGO Y COMPROBANTE (FLUJO DE NEGOCIO REAL) ───────────────
            Reserva reserva = new Reserva();
            reserva.setEstado(EstadoReserva.PENDIENTE_PAGO);
            reserva.setCliente(cliente);
            reserva.setCancha(cancha);
            reserva.setPago(null);

            var bloquesParaReservar = new ArrayList<BloqueHorario>();
            BloqueHorario primerBloque = cancha.getBloques().get(0);
            BloqueHorario segundoBloque = cancha.getBloques().get(1);

            bloquesParaReservar.add(primerBloque);
            //bloquesParaReservar.add(segundoBloque);
            // No se puede registrar un bloque que no este DISPONIBLE
            reserva.setBloquesSeleccionados(bloquesParaReservar);

            reservaBO.guardar(reserva, Estado.NUEVO);
            idReserva = reserva.getId();
            reservaContexto = reserva;
            System.out.println("Reserva creada en estado inicial: " + reservaBO.obtener(idReserva).getEstado());

            // Emisión de Comprobante delegando la matemática financiera a SQL
            Comprobante comprobante = new Comprobante();
            comprobante.setSerie("B001");
            comprobante.setMontoBloques(120.00);
            // Inyectamos el ID necesario para el flujo interno del BO -> DAO
            comprobante.setIdReservaTransitorio(idReserva);

            // Al guardar, el BO llama internamente a insertarComprobante(modelo, idReserva)
            comprobanteBO.guardar(comprobante, Estado.NUEVO);
            idComprobante = comprobante.getIdComprobante();
            System.out.println("Comprobante creado y calculado vía BD: Nro " + comprobante.getNumero());

            // Registro del Pago asociado
            Pago pago = new Pago();
            pago.setMetodoPago(MetodoPago.YAPE);
            pago.setFechaPago(LocalDateTime.now());
            // El monto total incluye lo calculado (Monto bloques + comisión base de S/. 5.00)
            pago.setMonto(comprobante.getMontoBloques() + 5.00);
            pago.setIdReservaTransitorio(idReserva);

            pagoBO.guardar(pago, Estado.NUEVO);

            pago.setComprobante(comprobante);
            reserva.setPago(pago);
            pagoBO.guardar(pago,Estado.MODIFICADO);
            idPago = pago.getIdPago();

            // Transición lógica y confirmación final de la reserva
            reserva.setEstado(EstadoReserva.CONFIRMADA);
            reservaBO.guardar(reserva, Estado.MODIFICADO);

            System.out.println("¡Validación y confirmación exitosa vía BO! Estado final: "
                    + reservaBO.obtener(idReserva).getEstado());
            System.out.println("\n--- REPORTE FINAL DE BIENVENIDA DE LA RED ---");
            System.out.println("Total reservas activas: " + reservaBO.listar().size());
            System.out.println("Flujo de negocio completado.");

        } catch (Exception e) {
            System.err.println("Error de negocio detectado en el flujo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ── LIMPIEZA CRONOLÓGICA INVERSA USANDO BO ───────────────────────────────
            System.out.println("\n--- INICIANDO LIMPIEZA DE ENTORNO ---");
            try {
                if (idReserva != null && reservaContexto != null) {
                    reservaContexto.setEstado(EstadoReserva.PENDIENTE_PAGO);
                    reservaContexto.setPago(null);
                    reservaContexto.setActivo(true);
                    reservaBO.guardar(reservaContexto, Estado.MODIFICADO);
                }
                if (idPago != null) { pagoBO.eliminar(idPago); System.out.println("Pago removido."); }
                if (idComprobante != null) { comprobanteBO.eliminar(idComprobante); System.out.println("Comprobante removido."); }
                if (idReserva != null) {reservaBO.eliminar(idReserva); System.out.println("Reserva removida."); }
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