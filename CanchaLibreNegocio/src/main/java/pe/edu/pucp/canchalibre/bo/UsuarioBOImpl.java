package pe.edu.pucp.canchalibre.bo;

import pe.edu.pucp.CanchaLibre.dao.UsuarioDAO;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Usuario;

import java.util.List;
import java.util.Objects;

public abstract class UsuarioBOImpl<M extends Usuario> extends BaseBO implements UsuarioBO<M> {

    protected abstract UsuarioDAO<M, Integer> getDao();

    protected void validarPersonaBasica(M modelo, String nombreEntidad) {
        Objects.requireNonNull(modelo, "El " + nombreEntidad + " es obligatorio");
        validarTextoObligatorio(modelo.getNombres(), "nombre");
        validarTextoObligatorio(modelo.getContrasena(), "contraseña");
        validarTextoObligatorio(modelo.getCorreo(), "correo");
        validarTextoObligatorio(modelo.getTelefono(), "teléfono");
    };

    public boolean login(String username, String password){
        validarTextoObligatorio(username,"username");
        validarTextoObligatorio(password,"password");

        if(getDao() instanceof UsuarioDAO<M, Integer> usuarioDao){
            return usuarioDao.login(username,password);
        }

        return false;
    }

    @Override
    public void crear(M modelo, Estado estado) {
        validarPersonaBasica(modelo, modelo.getClass().getSimpleName().toLowerCase());
        //validarEspecifico(modelo);

        int id = getDao().crear(modelo);
        if (id <= 0) {
            throw new IllegalStateException("No se pudo crear el registro");
        }
        modelo.setIdUsuario(id);
    }

    @Override
    public List<M> listar() {
        return getDao().leerTodos();
    }

    @Override
    public void actualizar(M modelo) {
        validarPersonaBasica(modelo, modelo.getClass().getSimpleName().toLowerCase());
        //validarEspecifico(modelo);
        validarIdPositivo(modelo.getIdUsuario(), "id de " + modelo.getClass().getSimpleName().toLowerCase());

        if (!getDao().actualizar(modelo)) {
            throw new IllegalStateException("No se pudo actualizar el registro con id: " + modelo.getIdUsuario());
        }
    }

    @Override
    public M obtener(int id) {
        validarIdPositivo(id, "id");
        return getDao().leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!getDao().eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el registro con id: " + id);
        }
    }

}