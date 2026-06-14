package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.canchalibre.bo.UsuarioBOImpl;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAO;
import pe.edu.pucp.CanchaLibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.util.List;

public class ClienteBOImpl extends UsuarioBOImpl<Cliente> implements ClienteBO {
    private final ClienteDAO clienteDao;

    public ClienteBOImpl() {
        this.clienteDao = new ClienteDAOImpl();
    }

    @Override
    public void crear(Cliente modelo, Estado estado) {
        validarCliente(modelo);

        int id = this.clienteDao.crear(modelo);
        if (id <= 0) {
            throw new IllegalStateException("No se pudo crear el cliente");
        }
        modelo.setIdUsuario(id);
    }

    @Override
    public List<Cliente> listar() {
        return this.clienteDao.leerTodos();
    }

    @Override
    public Cliente obtener(int id) {
        validarIdPositivo(id, "id");
        return this.clienteDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.clienteDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el cliente con id: " + id);
        }
    }

    @Override
    public void actualizar(Cliente modelo) {
        validarCliente(modelo);

        validarIdPositivo(modelo.getIdUsuario(), "id del cliente");
        if (!this.clienteDao.actualizar(modelo)) {
            throw new IllegalStateException("No se pudo actualizar el cliente con id: " + modelo.getIdUsuario());
        }
    }

    private void validarCliente(Cliente modelo) {
        validarPersonaBasica(modelo, "cliente");
    }
}