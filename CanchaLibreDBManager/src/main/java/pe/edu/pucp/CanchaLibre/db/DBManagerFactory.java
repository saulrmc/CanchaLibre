package pe.edu.pucp.CanchaLibre.db;

/**
 *
 * @author eric
 */
public abstract class DBManagerFactory {
    public abstract DBManager crearDBManager(String host, int puerto, 
                                             String esquema, String usuario, 
                                             String password);
}
