package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.util.List;

public class PropietarioBOImpl extends UsuarioBOImpl<Propietario> implements PropietarioBO {
    private final PropietarioDAO propietarioDao;

    public PropietarioBOImpl() {
        this.propietarioDao = new PropietarioDAOImpl();
    }

    @Override
    protected BaseDAO<Propietario, Integer> getDao(){
        return this.propietarioDao;
    }
}