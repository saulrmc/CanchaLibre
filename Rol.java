import java.util.List;

public enum Rol {
    //TODO: agregar más permisos
    ADMINISTRADOR(List.of(Permiso.GESTIONAR_USUARIOS)),
    CLIENTE(List.of(Permiso.CANCELAR_RESERVA,  Permiso.RESERVAR_CANCHA)),
    PROPIETARIO(List.of(Permiso.CREAR_CANCHA, Permiso.MODIFICAR_CANCHA)),;
    private final List<Permiso> tiposPermiso; //para que no se pueda cambiar
    //su valor inicial después de la compilación
    Rol(List<Permiso> tiposPermiso) {
        this.tiposPermiso = tiposPermiso;
    }
    public List<Permiso> getTiposPermiso() {
        return tiposPermiso;
    }
}
