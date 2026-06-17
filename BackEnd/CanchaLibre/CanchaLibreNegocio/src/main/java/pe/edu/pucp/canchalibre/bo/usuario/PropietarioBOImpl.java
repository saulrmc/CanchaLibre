package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAO;
import pe.edu.pucp.canchalibre.bo.PersonaBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.util.List;

public class PropietarioBOImpl extends PersonaBOImpl<Propietario> implements PropietarioBO {
    private final PropietarioDAO propietarioDao;

    public PropietarioBOImpl() {this.propietarioDao = new PropietarioDAOImpl();}

    @Override
    public Propietario buscarPorNombre(String nombres){
        return propietarioDao.buscarPorNombre(nombres);
    }

    @Override
    public void guardar(Propietario modelo, Estado estado) {
        validarPersonaBasica(modelo, modelo.getClass().getSimpleName().toLowerCase());
        validarEstado(estado);

        if (estado == Estado.Nuevo) {
            int id = propietarioDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el usuario");
            }
            modelo.setIdUsuario(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getIdUsuario(), "id del usuario");
            if (!propietarioDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el usuario con id: " + modelo.getIdUsuario());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Propietario> listar() {
        return propietarioDao.leerTodos();
    }

    @Override
    public Propietario obtener(int id) {
        validarIdPositivo(id, "id");
        return propietarioDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!propietarioDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el propietario con id: " + id);
        }
    }


}