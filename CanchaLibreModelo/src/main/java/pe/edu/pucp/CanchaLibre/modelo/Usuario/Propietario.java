package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Propietario  extends Usuario{
    private String ruc;
    private String nombreNegocio;

    public Propietario(int idUsuario, String nombres,
                       String contrasena, String correo, String telefono,
                       String ruc, String nombreNegocio) {
        super(idUsuario, nombres, contrasena, correo, telefono);
        this.ruc = ruc;
        this.nombreNegocio = nombreNegocio;
        rol = Rol.PROPIETARIO;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }
}