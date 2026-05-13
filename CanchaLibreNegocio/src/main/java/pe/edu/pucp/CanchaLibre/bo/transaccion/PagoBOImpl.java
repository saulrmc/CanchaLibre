package pe.edu.pucp.CanchaLibre.bo.transaccion;

import pe.edu.pucp.CanchaLibre.bo.BaseBO;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.PagoDAO;
import pe.edu.pucp.CanchaLibre.dao.Transaccion.PagoDAOImpl;
import pe.edu.pucp.CanchaLibre.modelo.transaccion.Pago;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class PagoBOImpl extends BaseBO implements PagoBO {
    private final PagoDAO pagoDao;

    public PagoBOImpl() {
        this.pagoDao = new PagoDAOImpl();
    }

    @Override
    public void crear(Pago modelo) {
        validarPago(modelo);

        int id = this.pagoDao.crear(modelo);
        if (id <= 0) {
            throw new IllegalStateException("No se pudo crear el pago");
        }
        modelo.setId(id);
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

    @Override
    public void actualizar(Pago modelo) {
        validarPago(modelo);

        validarIdPositivo(modelo.getId(), "id del pago");
        if (!this.pagoDao.actualizar(modelo)) {
            throw new IllegalStateException("No se pudo actualizar el pago con id: " + modelo.getId());
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