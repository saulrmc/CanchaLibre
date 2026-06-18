package pe.edu.pucp.canchalibre.bo.reserva;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.time.LocalTime;
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

        if (estado == Estado.Nuevo) {
            int id = this.reservaDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear la reserva");
            }
            modelo.setIdReserva(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getIdReserva(), "id de la reserva");
            if (!this.reservaDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar la reserva con id: " + modelo.getIdReserva());
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
        validarIdPositivo(id, "id");
        return this.reservaDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.reservaDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar la reserva con id: " + id);
        }
    }

    private void validarReserva(Reserva modelo) {
        Objects.requireNonNull(modelo, "La reserva es obligatoria");
        if (modelo.getFechaHora() == null){
            throw new IllegalArgumentException("La fecha de la reserva es inválida");
        }
        if (!modelo.getDuracion().isAfter(LocalTime.parse("00:00:00"))){
            throw new IllegalArgumentException("La duración de la reserva no puede ser nula");
        }
        Objects.requireNonNull(modelo.getCliente(), "La reserva necesita un cliente que la creó");
        Objects.requireNonNull(modelo.getCancha(), "La reserva necesita una cancha asociada");
        Objects.requireNonNull(modelo.getPago(), "La reserva necesita un pago asociado");
    }
}