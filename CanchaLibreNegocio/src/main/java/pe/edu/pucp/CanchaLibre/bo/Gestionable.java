package pe.edu.pucp.CanchaLibre.bo;


import java.util.List;

public interface Gestionable<M> {
    void crear(M modelo);
    void actualizar(M modelo);
    List<M> listar();
    M obtener(int id);
    void eliminar(int id);
}