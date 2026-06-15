package pe.edu.pucp.canchalibre.bo.transaccion;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.ComprobanteDAO;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.ComprobanteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;

import java.util.List;
import java.util.Objects;

public class ComprobanteBOImpl extends BaseBO implements ComprobanteBO {
    private final ComprobanteDAO comprobanteDao;

    public ComprobanteBOImpl() {
        this.comprobanteDao = new ComprobanteDAOImpl();
    }

    @Override
    public void guardar(Comprobante modelo, Estado estado) {
        validarComprobante(modelo);
        validarEstado(estado);

        if (estado == Estado.Nuevo) {
            int id = this.comprobanteDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el comprobante");
            }
            modelo.setIdComprobante(id);
        }
        else if (estado == Estado.Modificado) {
            validarIdPositivo(modelo.getIdComprobante(), "id del comprobante");
            if (!this.comprobanteDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el comprobante con id: " + modelo.getIdComprobante());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Comprobante> listar() {
        return this.comprobanteDao.leerTodos();
    }

    @Override
    public Comprobante obtener(int id) {
        validarIdPositivo(id, "id");
        return this.comprobanteDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.comprobanteDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el comprobante con id: " + id);
        }
    }

    private void validarComprobante(Comprobante modelo) {
        Objects.requireNonNull(modelo, "El comprobante es obligatorio");
        if (modelo.getFechaEmision() == null){
            throw new IllegalArgumentException("La fecha de emisión del comprobante es inválida");
        }
        Objects.requireNonNull(modelo.getReserva(), "El comprobante necesita una reserva asociada");
    }
}