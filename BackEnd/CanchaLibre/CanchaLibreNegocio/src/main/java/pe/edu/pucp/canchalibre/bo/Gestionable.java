package pe.edu.pucp.canchalibre.bo;


import pe.edu.pucp.canchalibre.modelo.Estado;

import java.util.List;

public interface Gestionable<M> {
    void guardar(M modelo, Estado estado);
    List<M> listar();
    M obtener(int id);
    void eliminar(int id);
}