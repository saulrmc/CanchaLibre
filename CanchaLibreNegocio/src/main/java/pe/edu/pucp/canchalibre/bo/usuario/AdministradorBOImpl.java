package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Administrador;

import java.util.List;

public class AdministradorBOImpl extends UsuarioBOImpl<Administrador> implements AdministradorBO {
    private final AdministradorDAO administradorDao;

    public AdministradorBOImpl() {
        this.administradorDao = new AdministradorDAOImpl();
    }

    @Override
    protected BaseDAO<Administrador, Integer> getDao(){
        return this.administradorDao;
    }

}