package pe.edu.pucp.canchalibre.bo.usuario;

import pe.edu.pucp.canchalibre.dao.usuario.PropietarioDAO;
import pe.edu.pucp.canchalibre.bo.PersonaBOImpl;
import pe.edu.pucp.canchalibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.util.List;

public class PropietarioBOImpl extends PersonaBOImpl<Propietario> implements PropietarioBO {
    private final PropietarioDAO propietarioDao;

    public PropietarioBOImpl() {this.propietarioDao = new PropietarioDAOImpl();}

    @Override
    public List<Propietario> buscarPorNombre(String nombres){
        return propietarioDao.buscarPorNombre(nombres);
    }

    @Override
    public Propietario buscarPorCuenta(String cuenta){
        return this.propietarioDao.buscarPorCuenta(cuenta);
    }

    @Override
    public void guardar(Propietario modelo, Estado estado) {
        validarPropietario(modelo);
        validarEstado(estado);

        if (estado == Estado.NUEVO) {
            modelo.setCalificacion(0.0);
            modelo.setSaldo(0.0);
            modelo.setActivo(true);

            int id = propietarioDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el usuario");
            }
            modelo.setId(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getId(), "id del usuario");
            if (!propietarioDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el usuario con id: " + modelo.getId());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Propietario> listar() {
        return propietarioDao.leerTodos();
    }

    @Override
    public Propietario obtener(int id) {
        validarIdPositivo(id, "id");
        return propietarioDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!propietarioDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el propietario con id: " + id);
        }
    }

    private void validarPropietario(Propietario modelo){
        validarPersonaBasica(modelo,"cliente");
        if (modelo.getCalificacion() < 0) {
            throw new IllegalArgumentException("La calificacion no puede ser negativa");
        }
        validarTextoObligatorio(modelo.getRUC(),"RUC");
//        if (modelo.getSaldo() < 0) {
//            throw new IllegalArgumentException("El saldo no puede ser negativo");
//        }
        //considera cancelaciones de reservas
    }

    @Override
    public void actualizarSaldo(int idPropietario, double monto){
        validarIdPositivo(idPropietario,"id propietario");
        this.propietarioDao.actualizarSaldo(idPropietario,monto);
    }

}