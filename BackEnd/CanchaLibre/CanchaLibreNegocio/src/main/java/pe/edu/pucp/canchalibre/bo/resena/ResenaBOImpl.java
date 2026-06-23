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
        Objects.requireNonNull(modelo, "La reseña es obligatoria");

        if (modelo.getCalificacion() < 0 || modelo.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 5");
        }

        if (modelo.getFechaPublicacion() == null) {
            throw new IllegalArgumentException("La fecha de publicación es obligatoria");
        }

        Objects.requireNonNull(modelo.getReserva(), "La reseña necesita una reserva asociada");

        validarIdPositivo(modelo.getReserva().getIdReserva(), "id de la reserva");
    }
}