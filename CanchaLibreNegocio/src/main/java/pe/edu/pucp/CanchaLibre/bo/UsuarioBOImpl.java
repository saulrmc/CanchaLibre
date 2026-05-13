package pe.edu.pucp.CanchaLibre.bo;

import pe.edu.pucp.CanchaLibre.modelo.usuario.Usuario;

import java.util.Objects;

public abstract class UsuarioBOImpl<M extends Usuario> extends BaseBO implements UsuarioBO<M> {
    protected void validarPersonaBasica(M modelo, String nombreEntidad) {
        Objects.requireNonNull(modelo, "El " + nombreEntidad + " es obligatorio");
        validarTextoObligatorio(modelo.getNombres(), "nombre");
        validarTextoObligatorio(modelo.getContrasena(), "contraseña");
        validarTextoObligatorio(modelo.getCorreo(), "correo");
        validarTextoObligatorio(modelo.getTelefono(), "teléfono");
    }
}