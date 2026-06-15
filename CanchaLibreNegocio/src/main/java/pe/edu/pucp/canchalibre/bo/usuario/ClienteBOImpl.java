package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.CanchaLibre.dao.BaseDAO;
import pe.edu.pucp.CanchaLibre.dao.UsuarioDAO;
import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.util.List;

public class ClienteBOImpl extends UsuarioBOImpl<Cliente> implements ClienteBO {
    private final UsuarioDAO clienteDao;

    public ClienteBOImpl() {
        this.clienteDao = new ClienteDAOImpl();
    }

    @Override
    protected UsuarioDAO<Cliente, Integer> getDao(){
        return this.clienteDao;
    }
}