package pe.edu.pucp.canchalibre.bo.resena;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.CanchaLibre.dao.resena.ResenaDAO;
import pe.edu.pucp.CanchaLibre.dao.resena.ResenaDAOImpl;
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

        if (estado == Estado.Nuevo) {
            int id = this.resenaDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear la reseña");
            }
            modelo.setIdResena(id);
        }
        else if (estado == Estado.Modificado) {
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
        if (modelo.getCalificacion() < 0){
            throw new IllegalArgumentException("La calificacion en la reseña debe ser positiva");
        }
        Objects.requireNonNull(modelo.getCliente(), "La resena necesita un cliente que la publicó");
        Objects.requireNonNull(modelo.getCancha(), "La resena necesita una cancha asociada");
    }
}