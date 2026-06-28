package pe.edu.pucp.canchalibre.bo.cancha;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.TransactionsManager;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.*;

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

        TransactionsManager.iniciarTransaccion();
        try {
            if (estado == Estado.NUEVO) {
                int id = this.canchaDao.crear(modelo);
                if (id <= 0) {
                    throw new IllegalStateException("No se pudo crear la cancha");
                }
                modelo.setId(id);
            } else if (estado == Estado.MODIFICADO) {
                validarIdPositivo(modelo.getId(), "id de la cancha");
                if (!this.canchaDao.actualizar(modelo)) {
                    throw new IllegalStateException("No se pudo actualizar la cancha con id: " + modelo.getId());
                }
            } else {
                throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
            }
            TransactionsManager.commitTransaccion();
        } catch (Exception ex){
            TransactionsManager.rollbackTransaccion();
            throw ex;
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
        Objects.requireNonNull(modelo.getPropietario(), "El propietario en la cancha es obligatorio");
        validarIdPositivo(modelo.getPropietario().getId(), "id de propietario");

        if (modelo.getPrecioBase() < 0) {
            throw new IllegalArgumentException("El precio base de la cancha no puede ser negativo");
        }

        Objects.requireNonNull(modelo.getDeportes(), "Los deportes de la cancha son obligatorios");
        if (modelo.getDeportes().isEmpty()) {
            throw new IllegalArgumentException("La cancha debe tener al menos un deporte asociado");
        }
        for (Deporte deporte : modelo.getDeportes()) {
            Objects.requireNonNull(deporte, "Cada deporte asociado es obligatorio");
        }

        if (modelo.getEtiquetas() != null) {
            for (Etiqueta etiqueta : modelo.getEtiquetas()) {
                Objects.requireNonNull(etiqueta, "Cada etiqueta asociada no puede ser nula");
            }
        }

        Objects.requireNonNull(modelo.getBloques(), "Los bloques de horario son obligatorios");
        if (modelo.getBloques().isEmpty()) {
            throw new IllegalArgumentException("La cancha debe tener al menos un bloque horario");
        }

        for (BloqueHorario bloque : modelo.getBloques()) {
            Objects.requireNonNull(bloque, "Cada bloque de horario de la cancha es obligatorio");

            Objects.requireNonNull(bloque.getDia(), "El día de la semana del bloque es obligatorio");
            Objects.requireNonNull(bloque.getHoraInicio(), "La hora de inicio del bloque es obligatoria");
            Objects.requireNonNull(bloque.getHoraFin(), "La hora de fin del bloque es obligatoria");
            Objects.requireNonNull(bloque.getEstado(), "El estado del bloque es obligatorio");

            if (!bloque.getHoraInicio().isBefore(bloque.getHoraFin())) {
                throw new IllegalArgumentException("La hora de inicio de un bloque debe ser anterior a la hora de fin");
            }

            if (bloque.getEstado() == EstadoBloque.BLOQUEADO || bloque.getEstado() == EstadoBloque.MANTENIMIENTO) {
                bloque.setPrecio(0.0);
            } else if (bloque.getEstado()==EstadoBloque.DISPONIBLE) {
                bloque.setPrecio(modelo.getPrecioBase());
            }
        }

    }

    @Override
    public List<Cancha> listarCanchasPorCuenta(String cuenta){
        return this.canchaDao.listarCanchasPorCuenta(cuenta);
    }

    @Override
    public List<Cancha> listarCanchasPorDistrito(String distritoOficial){return this.canchaDao.listarCanchasPorDistrito(distritoOficial);}
}