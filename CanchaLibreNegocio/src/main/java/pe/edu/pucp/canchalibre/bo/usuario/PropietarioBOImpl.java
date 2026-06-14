package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.util.List;

public class PropietarioBOImpl extends UsuarioBOImpl<Propietario> implements PropietarioBO {
    private final PropietarioDAO propietarioDao;

    public PropietarioBOImpl() {
        this.propietarioDao = new PropietarioDAOImpl();
    }

    @Override
    public void crear(Propietario modelo, Estado estado) {
        validarPropietario(modelo);

        int id = this.propietarioDao.crear(modelo);
        if (id <= 0) {
            throw new IllegalStateException("No se pudo crear el propietario");
        }
        modelo.setIdUsuario(id);
    }

    @Override
    public List<Propietario> listar() {
        return this.propietarioDao.leerTodos();
    }

    @Override
    public Propietario obtener(int id) {
        validarIdPositivo(id, "id");
        return this.propietarioDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.propietarioDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el propietario con id: " + id);
        }
    }

    @Override
    public void actualizar(Propietario modelo) {
        validarPropietario(modelo);

        validarIdPositivo(modelo.getIdUsuario(), "id del propietario");
        if (!this.propietarioDao.actualizar(modelo)) {
            throw new IllegalStateException("No se pudo actualizar el propietario con id: " + modelo.getIdUsuario());
        }
    }

    private void validarPropietario(Propietario modelo) {
        validarPersonaBasica(modelo, "propietario");
    }
}