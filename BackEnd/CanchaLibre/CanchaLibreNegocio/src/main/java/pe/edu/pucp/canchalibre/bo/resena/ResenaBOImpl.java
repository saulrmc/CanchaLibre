package pe.edu.pucp.canchalibre.bo.resena;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.resena.ResenaDAO;
import pe.edu.pucp.canchalibre.dao.resena.ResenaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;

import java.util.List;
import java.util.Objects;

public class ResenaBOImpl extends BaseBO implements ResenaBO {
    private final ResenaDAO resenaDao;

    public ResenaBOImpl() {
        this.resenaDao = new ResenaDAOImpl();
    }

    @Override
    public void guardar(Resena modelo, Estado estado) {
        validarResena(modelo);
        validarEstado(estado);

        if (estado == Estado.NUEVO) {
            modelo.setFechaPublicacion(java.time.LocalDateTime.now());
            int id = this.resenaDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear la reseña");
            }
            modelo.setIdResena(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getIdResena(), "id de la reseña");
            if (!this.resenaDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar la reseña con id: " + modelo.getIdResena());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Resena> listar() {
        return this.resenaDao.leerTodos();
    }

    @Override
    public Resena obtener(int id) {
        validarIdPositivo(id, "id");
        return this.resenaDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.resenaDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar la reseña con id: " + id);
        }
    }

    private void validarResena(Resena modelo) {
        Objects.requireNonNull(modelo, "La estructura de la resena no puede ser nula");

        if (modelo.getCalificacion() < 1.0 || modelo.getCalificacion() > 5.0) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        if (modelo.getDescripcion() != null && modelo.getDescripcion().length() > 500) {
            throw new IllegalArgumentException("El comentario no puede exceder los 500 caracteres.");
        }

        Objects.requireNonNull(modelo.getReserva(), "La reseña debe estar asociada a una reserva válida.");
        validarIdPositivo(modelo.getReserva().getIdReserva(), "id de la reserva");
    }

    @Override
    public List<Resena> listarResenasPorCancha(Integer idCancha){
        return this.resenaDao.listarResenasPorCancha(idCancha);
    }

    @Override
    public List<Resena> listarResenasPorCliente(Integer idCliente){
        return this.resenaDao.listarResenasPorCliente(idCliente);
    }

}