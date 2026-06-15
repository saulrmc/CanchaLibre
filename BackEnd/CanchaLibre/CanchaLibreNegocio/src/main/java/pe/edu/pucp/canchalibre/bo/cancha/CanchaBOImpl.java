package pe.edu.pucp.canchalibre.bo.cancha;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.CanchaLibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.CanchaLibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import java.util.List;
import java.util.Objects;

public class CanchaBOImpl extends BaseBO implements CanchaBO {
    private final CanchaDAO canchaDao;

    public CanchaBOImpl() {
        this.canchaDao = new CanchaDAOImpl();
    }

    @Override
    public void guardar(Cancha modelo, Estado estado) {
        validarCancha(modelo);
        validarEstado(estado);

        if (estado == Estado.Nuevo) {
            int id = this.canchaDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear la cancha");
            }
            modelo.setIdCancha(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getIdCancha(), "id de la cancha");
            if (!this.canchaDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar la cancha con id: " + modelo.getIdCancha());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Cancha> listar() {
        return this.canchaDao.leerTodos();
    }

    @Override
    public Cancha obtener(int id) {
        validarIdPositivo(id, "id");
        return this.canchaDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.canchaDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar la cancha con id: " + id);
        }
    }

    private void validarCancha(Cancha modelo) {
        Objects.requireNonNull(modelo, "La cancha es obligatoria");
        if (modelo.getNombre() == null || modelo.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la cancha es obligatorio");
        }
        if (modelo.getDeportes().size() <= 0){
            throw new IllegalArgumentException("La cancha necesita al menos un deporte");
        }
        Objects.requireNonNull(modelo.getPropietario(), "La cancha necesita un propietario");
    }
}