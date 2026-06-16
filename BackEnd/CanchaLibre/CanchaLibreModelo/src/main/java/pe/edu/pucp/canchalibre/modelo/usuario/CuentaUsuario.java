package pe.edu.pucp.canchalibre.modelo.usuario;

public class CuentaUsuario {
    private String userName;
    private String password;
    private int id;
    private boolean activo;


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "CuentaUsuario{" +
                "id=" + getId() +
                ", activo=" + isActivo() +
                ", userName='" + userName + '\'' +
                ", password='" + (password != null ?
                "*".repeat(password.length()) : null) + '\'' +
                '}';
    }
}