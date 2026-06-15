package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.CanchaLibre.dao.UsuarioDAO;
import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.AdministradorDAOImpl;
import pe.edu.pucp.canchalibre.modelo.usuario.Administrador;

public class AdministradorBOImpl extends UsuarioBOImpl<Administrador> implements AdministradorBO {
    private final UsuarioDAO administradorDao;

    public AdministradorBOImpl() {
        this.administradorDao = new AdministradorDAOImpl();
    }

    @Override
    protected UsuarioDAO<Administrador, Integer> getDao(){
        return this.administradorDao;
    }

}