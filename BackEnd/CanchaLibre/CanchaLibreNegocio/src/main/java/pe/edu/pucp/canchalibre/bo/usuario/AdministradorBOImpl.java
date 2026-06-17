package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAOImpl;
import pe.edu.pucp.canchalibre.bo.PersonaBOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Administrador;

import java.util.List;

public class AdministradorBOImpl extends PersonaBOImpl<Administrador> implements AdministradorBO {
    private final AdministradorDAO administradorDao;

    public AdministradorBOImpl() {this.administradorDao = new AdministradorDAOImpl();}

    @Override
    public Administrador buscarPorNombre(String nombres){
        return this.administradorDao.buscarPorNombre(nombres);
    }

    @Override
    public void guardar(Administrador modelo, Estado estado) {
        validarPersonaBasica(modelo, modelo.getClass().getSimpleName().toLowerCase());
        validarEstado(estado);

        if (estado == Estado.Nuevo) {
            int id = administradorDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el usuario");
            }
            modelo.setIdUsuario(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getIdUsuario(), "id del usuario");
            if (!administradorDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el usuario con id: " + modelo.getIdUsuario());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Administrador> listar() {
        return administradorDao.leerTodos();
    }

    @Override
    public Administrador obtener(int id) {
        validarIdPositivo(id, "id");
        return administradorDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!administradorDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el administrador con id: " + id);
        }
    }

}