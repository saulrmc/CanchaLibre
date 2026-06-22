package pe.edu.pucp.canchalibre.bo.cancha;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.cancha.BloqueHorarioDAO;
import pe.edu.pucp.canchalibre.dao.cancha.BloqueHorarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;

import java.util.List;
import java.util.Objects;


public class BloqueHorarioBOImpl extends BaseBO implements BloqueHorarioBO {
    private final BloqueHorarioDAO bloqueHorarioDao;
    public BloqueHorarioBOImpl(){ this.bloqueHorarioDao=new BloqueHorarioDAOImpl();}
    
    @Override
    public void guardar(BloqueHorario modelo, Estado estado) {
        validarBloqueHorario(modelo);
        validarEstado(estado);

        if (estado == Estado.Nuevo) {
            int id = this.bloqueHorarioDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el bloque de horario");
            }
            modelo.setId(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getId(), "id del bloque de horario");
            if (!this.bloqueHorarioDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el bloque de horario con id: " + modelo.getId());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<BloqueHorario> listar() {
        return this.bloqueHorarioDao.leerTodos();
    }

    @Override
    public BloqueHorario obtener(int id) {
        validarIdPositivo(id, "id");
        return this.bloqueHorarioDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.bloqueHorarioDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el bloque de horario con id: " + id);
        }
    }

    private void validarBloqueHorario(BloqueHorario modelo) {
        Objects.requireNonNull(modelo, "El bloque de horario es obligatorio");
        if (modelo.getHoraInicio() == null ||
                modelo.getHoraFin() == null) {
            throw new IllegalArgumentException("La hora de inicio y hora de fin del bloque de horario es obligatorio");
        }
        if (modelo.getPrecio() <= 0){
            throw new IllegalArgumentException("El bloque de horario debe tener un precio mayor a 0");
        }
        if(modelo.getDia() == null){
            throw new IllegalStateException("El día del bloque es obligatorio");
        }
    }
    
}
