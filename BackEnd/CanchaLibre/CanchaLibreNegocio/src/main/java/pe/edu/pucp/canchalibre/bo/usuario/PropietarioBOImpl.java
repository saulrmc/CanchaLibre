package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.CanchaLibre.dao.UsuarioDAO;
import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

public class PropietarioBOImpl extends UsuarioBOImpl<Propietario> implements PropietarioBO {
    private final UsuarioDAO propietarioDao;

    public PropietarioBOImpl() {
        this.propietarioDao = new PropietarioDAOImpl();
    }

    @Override
    protected UsuarioDAO<Propietario, Integer> getDao(){
        return this.propietarioDao;
    }
}