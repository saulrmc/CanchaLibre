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
    public void guardar(M modelo, Estado estado) {
        validarPersonaBasica(modelo, modelo.getClass().getSimpleName().toLowerCase());
        validarEstado(estado);

        if (estado == Estado.Nuevo) {
            int id = this.getDao().crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el usuario");
            }
            modelo.setIdUsuario(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getIdUsuario(), "id del usuario");
            if (!this.getDao().actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el usuario con id: " + modelo.getIdUsuario());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<M> listar() {
        return getDao().leerTodos();
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