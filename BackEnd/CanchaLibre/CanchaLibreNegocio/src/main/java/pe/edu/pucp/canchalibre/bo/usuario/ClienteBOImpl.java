package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.canchalibre.dao.usuario.ClienteDAO;
import pe.edu.pucp.canchalibre.bo.PersonaBOImpl;
import pe.edu.pucp.canchalibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.util.List;

public class ClienteBOImpl extends PersonaBOImpl<Cliente> implements ClienteBO {
    private final ClienteDAO clienteDao;

    public ClienteBOImpl() {this.clienteDao = new ClienteDAOImpl();}

    @Override
    public List<Cliente> buscarPorNombre(String nombres){
        return this.clienteDao.buscarPorNombre(nombres);
    }

    @Override
    public Cliente buscarPorCuenta(String cuenta){
        return this.clienteDao.buscarPorCuenta(cuenta);
    }

    @Override
    public void guardar(Cliente modelo, Estado estado) {
        validarCliente(modelo);
        validarEstado(estado);

        if (estado == Estado.NUEVO) {
            modelo.setCalificacion(0.0);
            int id = clienteDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el usuario");
            }
            modelo.setId(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getId(), "id del usuario");
            if (!clienteDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el usuario con id: " + modelo.getId());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Cliente> listar() {
        return clienteDao.leerTodos();
    }

    @Override
    public Cliente obtener(int id) {
        validarIdPositivo(id, "id");
        return clienteDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!clienteDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el cliente con id: " + id);
        }
    }

    private void validarCliente(Cliente modelo){
        validarPersonaBasica(modelo,"cliente");
        if (modelo.getCalificacion() < 0) {
            throw new IllegalArgumentException("La calificacion no puede ser negativa");
        }
    }

}