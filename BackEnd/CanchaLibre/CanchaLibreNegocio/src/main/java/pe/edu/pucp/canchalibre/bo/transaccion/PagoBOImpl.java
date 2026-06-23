package pe.edu.pucp.canchalibre.bo.transaccion;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.transaccion.PagoDAO;
import pe.edu.pucp.canchalibre.dao.transaccion.PagoDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;

import java.util.List;
import java.util.Objects;

public class PagoBOImpl extends BaseBO implements PagoBO {
    private final PagoDAO pagoDao;

    public PagoBOImpl() {
        this.pagoDao = new PagoDAOImpl();
    }

    @Override
    public void guardar(Pago modelo, Estado estado) {
        validarPago(modelo);
        validarEstado(estado);

        if (estado == Estado.NUEVO) {
            int id = this.pagoDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el pago");
            }
            modelo.setIdPago(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getIdPago(), "id del pago");
            if (!this.pagoDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el pago con id: " + modelo.getIdPago());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    @Override
    public List<Pago> listar() {
        return this.pagoDao.leerTodos();
    }

    @Override
    public Pago obtener(int id) {
        validarIdPositivo(id, "id");
        return this.pagoDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.pagoDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar el pago con id: " + id);
        }
    }

    private void validarPago(Pago modelo) {
        Objects.requireNonNull(modelo, "El pago es obligatorio");
        if (modelo.getFechaPago() == null){
            throw new IllegalArgumentException("La fecha de pago es inválida");
        }
        if (modelo.getMonto() <= 0){
            throw new IllegalArgumentException("El monto de pago nebe ser no nulo");
        }
        Objects.requireNonNull(modelo.getMetodoPago(), "El método de pago es inválido");
    }
}