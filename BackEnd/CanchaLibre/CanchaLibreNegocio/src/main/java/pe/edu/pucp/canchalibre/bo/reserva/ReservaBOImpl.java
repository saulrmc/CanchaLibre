package pe.edu.pucp.canchalibre.bo.reserva;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservaBOImpl extends BaseBO implements ReservaBO {
    private final ReservaDAO reservaDao;

    public ReservaBOImpl() {
        this.reservaDao = new ReservaDAOImpl();
    }

    @Override
    public void guardar(Reserva modelo, Estado estado) {
        validarReserva(modelo);
        validarEstado(estado);

        if (estado == Estado.NUEVO) {
            int id = this.reservaDao.crear(modelo);

            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear la reserva");
            }

            modelo.setIdReserva(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getIdReserva(), "id de la reserva");

            if (!this.reservaDao.actualizar(modelo)) {
                throw new IllegalStateException(
                        "No se pudo actualizar la reserva con id: " + modelo.getIdReserva()
                );
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
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

            if (!bloque.getHoraFin().isAfter(bloque.getHoraInicio())) {
                throw new IllegalArgumentException("La hora fin del bloque debe ser mayor que la hora inicio");
            }

            if (bloque.getPrecio() <= 0) {
                throw new IllegalArgumentException("El precio del bloque horario debe ser mayor que cero");
            }
        }

    }

    @Override
    public List<Reserva> listarPorCliente(int idCliente) {
        //esto es ineficiente pero es una solución temporal
        List<Reserva> reservas = reservaDao.leerTodos();
        List<Reserva> reservasPorCliente = new ArrayList<>();
        for (Reserva reserva : reservas) {
            if (reserva.getCliente().getId() == idCliente) {
                reservasPorCliente.add(reserva);
            }
        }
        return reservasPorCliente;
    }
}