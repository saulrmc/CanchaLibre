package pe.edu.pucp.CanchaLibre.bo.usuario;

import pe.edu.pucp.CanchaLibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAOImpl;
import pe.edu.pucp.CanchaLibre.modelo.usuario.Administrador;

import java.util.List;
import java.util.Objects;

public class AdministradorBOImpl extends UsuarioBOImpl<Administrador> implements AdministradorBO {
    private final AdministradorDAO administradorDao;

    public AdministradorBOImpl() {
        this.administradorDao = new AdministradorDAOImpl();
    }

    @Override
    public void crear(Administrador modelo) {
        validarAdministrador(modelo);

        int id = this.administradorDao.crear(modelo);
        if (id <= 0) {
            throw new IllegalStateException("No se pudo crear el administrador");
        }
        modelo.setIdUsuario(id);
    }

    @Override
    public List<Administrador> listar() {
        return this.administradorDao.leerTodos();
    }

    @Override
    public Administrador obtener(int id) {
        validarIdPositivo(id, "id");
        return this.administradorDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.administradorDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el administrador con id: " + id);
        }
    }

    @Override
    public void actualizar(Administrador modelo) {
        validarAdministrador(modelo);

        validarIdPositivo(modelo.getIdUsuario(), "id del administrador");
        if (!this.administradorDao.actualizar(modelo)) {
            throw new IllegalStateException("No se pudo actualizar el administrador con id: " + modelo.getIdUsuario());
        }
    }

    private void validarAdministrador(Administrador modelo) {
        validarPersonaBasica(modelo, "administrador");
    }
}