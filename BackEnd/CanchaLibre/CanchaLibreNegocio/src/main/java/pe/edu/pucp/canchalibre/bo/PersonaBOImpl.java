package pe.edu.pucp.canchalibre.bo;

import pe.edu.pucp.canchalibre.modelo.Persona;

import java.util.Objects;

public abstract class PersonaBOImpl<M extends Persona> extends BaseBO implements PersonaBO<M> {
    protected void validarPersonaBasica(M modelo, String nombreEntidad) {
        Objects.requireNonNull(modelo, "El " + nombreEntidad + " es obligatorio");
        validarTextoObligatorio(modelo.getNombres(), "nombre");
        //validarTextoObligatorio(modelo.getApellidoPaterno(), "apellido paterno");
        validarTextoObligatorio(modelo.getCorreo(), "correo");
        validarTextoObligatorio(modelo.getTelefono(), "teléfono");
    }
}