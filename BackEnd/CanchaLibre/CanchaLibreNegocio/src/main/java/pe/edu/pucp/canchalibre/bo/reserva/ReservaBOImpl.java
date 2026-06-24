package pe.edu.pucp.canchalibre.bo.reserva;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.bo.usuario.PropietarioBO;
import pe.edu.pucp.canchalibre.bo.usuario.PropietarioBOImpl;
import pe.edu.pucp.canchalibre.dao.TransactionsManager;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;
import pe.edu.pucp.canchalibre.modelo.cancha.EstadoBloque;
import pe.edu.pucp.canchalibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReservaBOImpl extends BaseBO implements ReservaBO {
    private final ReservaDAO reservaDao;
    private final PropietarioBO propietarioBo;

    public ReservaBOImpl() {
        this.reservaDao = new ReservaDAOImpl();
        this.propietarioBo = new PropietarioBOImpl();
    }

    @Override
    public void guardar(Reserva modelo, Estado estado) {
        validarReserva(modelo);
        validarEstado(estado);

        TransactionsManager.iniciarTransaccion();
        try {
            if (estado == Estado.NUEVO) {
                modelo.setFechaCreacion(java.time.LocalDateTime.now());
                modelo.setEstado(EstadoReserva.PENDIENTE_PAGO);

                int id = this.reservaDao.crear(modelo);
                modelo.setIdReserva(id);
            }
            else if (estado == Estado.MODIFICADO) {
                validarIdPositivo(modelo.getIdReserva(), "id de la reserva");

                if (modelo.getEstado() == EstadoReserva.CANCELADA) {
                    double montoOriginal = modelo.getPago().getMonto();
                    int idPropietario = modelo.getCancha().getPropietario().getId();

                    LocalDateTime inicioPartido = calcularInicioPartido(modelo);
                    LocalDateTime limiteCancelacionGratuita = inicioPartido.minusHours(24);

                    if (!LocalDateTime.now().isAfter(limiteCancelacionGratuita)) {
                        propietarioBo.actualizarSaldo(idPropietario, -montoOriginal);
                        System.out.println("[GUARDAR BO] Deducción de saldo aplicada correctamente al propietario ID: " + idPropietario);
                    }
                }

                if (!this.reservaDao.actualizar(modelo)) {
                    throw new IllegalStateException("No se pudo actualizar la reserva con id: " + modelo.getIdReserva());
                }
            }else{
                throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
            }
            TransactionsManager.commitTransaccion();
        } catch (Exception ex) {
            TransactionsManager.rollbackTransaccion();
            throw ex;
        }
    }

    @Override
    public List<Reserva> listar() {
        return this.reservaDao.leerTodos();
    }

    @Override
    public Reserva obtener(int id) {
        validarIdPositivo(id, "id de la reserva");
        return this.reservaDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id de la reserva");

        if (!this.reservaDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar la reserva con id: " + id);
        }
    }

    private void validarReserva(Reserva modelo) {
        Objects.requireNonNull(modelo, "La reserva es obligatoria");
        Objects.requireNonNull(modelo.getEstado(), "La reserva necesita un estado");

        switch (modelo.getEstado()) {
            case CONFIRMADA:
                // Regla: Si ya está confirmada, es mandatorio que el pago exista
                Objects.requireNonNull(modelo.getPago(), "Para confirmar una reserva es obligatorio registrar su pago");
                validarIdPositivo(modelo.getPago().getIdPago(), "id del pago");
                break;

            case RECHAZADA:
                if (modelo.getFechaCreacion() != null) {
                    LocalDateTime ahora = LocalDateTime.now();
                    // 5 minutos de gracia (siguiendo el estándar de CuentaUsuario)
                    LocalDateTime tiempoPermitido = modelo.getFechaCreacion().plusMinutes(5);

                    if (ahora.isAfter(tiempoPermitido)) {
                        throw new IllegalArgumentException("La reserva ha sido RECHAZADA: Superó el tiempo límite de 5 minutos para registrar el pago.");
                    }
                }
                break;

            case CANCELADA:
                validarCancelacion(modelo);
                break;

            default:
                // PENDIENTE_PAGO ya validado
                break;
        }
        Objects.requireNonNull(modelo.getCliente(), "La reserva necesita un cliente que la creó");
        validarIdPositivo(modelo.getCliente().getId(), "id del cliente");

        Objects.requireNonNull(modelo.getCancha(), "La reserva necesita una cancha asociada");
        validarIdPositivo(modelo.getCancha().getId(), "id de la cancha");

        if (modelo.getBloquesSeleccionados() == null || modelo.getBloquesSeleccionados().isEmpty()) {
            throw new IllegalArgumentException("La reserva necesita al menos un bloque horario");
        }

        for (BloqueHorario bloque : modelo.getBloquesSeleccionados()) {
            Objects.requireNonNull(bloque, "La reserva no puede tener bloques nulos");
            validarIdPositivo(bloque.getId(), "id del bloque horario");

            Objects.requireNonNull(bloque.getDia(), "El bloque horario necesita un día");
            Objects.requireNonNull(bloque.getHoraInicio(), "El bloque horario necesita hora de inicio");
            Objects.requireNonNull(bloque.getHoraFin(), "El bloque horario necesita hora de fin");
            Objects.requireNonNull(bloque.getEstado(), "El bloque horario necesita estado");

            if (bloque.getEstado() != EstadoBloque.DISPONIBLE) {
                throw new IllegalArgumentException("No se puede reservar un bloque horario que no esté DISPONIBLE");
            }

            if (!bloque.getHoraFin().isAfter(bloque.getHoraInicio())) {
                throw new IllegalArgumentException("La hora fin del bloque debe ser mayor que la hora inicio");
            }

            if (bloque.getPrecio() <= 0) {
                throw new IllegalArgumentException("El precio del bloque horario debe ser mayor que cero");
            }
        }
    }

    @Override
    public List<Reserva> listarReservasPorCuenta(String cuenta){
        return reservaDao.listarReservasPorCuenta(cuenta);
    }

    @Override
    public List<Reserva> listarReservasPorId(int idCliente) {
        return reservaDao.listarReservasPorId(idCliente);
    }

    private LocalDateTime calcularInicioPartido(Reserva modelo) {
        if (modelo.getBloquesSeleccionados() == null || modelo.getBloquesSeleccionados().isEmpty()) {
            throw new IllegalArgumentException("La reserva no contiene bloques horarios para calcular el inicio del partido.");
        }
        BloqueHorario primerBloque = modelo.getBloquesSeleccionados().get(0);
        java.time.DayOfWeek diaJava = java.time.DayOfWeek.valueOf(primerBloque.getDia().name());
        LocalDate fechaReal = LocalDate.now().with(java.time.temporal.TemporalAdjusters.nextOrSame(diaJava));
        return LocalDateTime.of(fechaReal, primerBloque.getHoraInicio());
    }

    private void validarCancelacion(Reserva modelo) {
        if (modelo.getPago() == null) {
            throw new IllegalArgumentException("No se puede cancelar una reserva sin un pago previo.");
        }

        if (modelo.getCliente() == null || modelo.getCliente().getTelefono() == null || modelo.getCliente().getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente debe tener un teléfono registrado para procesar su devolución.");
        }

        double montoOriginal = modelo.getPago().getMonto();
        String numeroDestino = modelo.getCliente().getTelefono();

        LocalDateTime inicioPartido = calcularInicioPartido(modelo);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteCancelacionGratuita = inicioPartido.minusHours(24);

        System.out.println("[AUDITORÍA BO - VALIDACIÓN CANCELACIÓN]");
        System.out.println("-> ID Reserva: " + modelo.getIdReserva());

        if (ahora.isAfter(limiteCancelacionGratuita)) {
            System.out.println("-> Tipo: TARDÍA (Faltan menos de 24 horas).");
            System.out.println("-> Acción: El dinero se queda con el Propietario. CanchaLibre no genera reembolso al cliente.");
        } else {
            final double DEDUCIBLE_APP = 5.00; // Tarifa administrativa fija de la app
            double montoARegresar = montoOriginal - DEDUCIBLE_APP;

            if (montoARegresar <= 0) {
                throw new IllegalArgumentException("El monto de la reserva es menor o igual al deducible de la plataforma (S/. " + DEDUCIBLE_APP + "). No hay saldo remanente.");
            }

            System.out.println("-> Tipo: A TIEMPO (Más de 24 horas de anticipación).");
            System.out.println("-> (-) Deducible retenido por CanchaLibre: S/. " + DEDUCIBLE_APP);
            System.out.println("-> (=) TOTAL NETO A YAPEAR AL CLIENTE: S/. " + montoARegresar);
            System.out.println("-> Destinatario (Teléfono del cliente): " + numeroDestino);
        }
    }
}