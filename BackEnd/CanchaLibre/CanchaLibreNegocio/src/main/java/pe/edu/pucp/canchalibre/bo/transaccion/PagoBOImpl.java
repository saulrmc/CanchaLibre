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
            modelo.setFechaPago(java.time.LocalDateTime.now());
            modelo.setComprobante(null);

            int id = this.pagoDao.insertarPago(modelo,modelo.getIdReservaTransitorio());
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el pago");
            }
            modelo.setIdPago(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getIdPago(), "id del pago");

            Pago pagoOriginal = this.pagoDao.leer(modelo.getIdPago());
            if (pagoOriginal == null) {
                throw new IllegalArgumentException("El pago que intenta modificar no existe");
            }
            if (pagoOriginal.getMonto() != modelo.getMonto()) {
                throw new IllegalArgumentException("Operación denegada: El monto de un pago asentado no puede ser alterado");
            }

            if (modelo.getComprobante() != null) {
                if (modelo.getComprobante().getIdComprobante() <= 0) {
                    throw new IllegalArgumentException("El comprobante que intenta asociar al pago debe tener un ID válido");
                }
            }

            if (!this.pagoDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar el pago con id: " + modelo.getIdPago());
            }

            Pago pagoActualizado = this.pagoDao.leer(modelo.getIdPago());
            if(pagoActualizado!=null){
                modelo.setComprobante(pagoActualizado.getComprobante());
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
        if (modelo.getMonto() <= 0){
            throw new IllegalArgumentException("El monto de pago nebe ser no nulo");
        }
        Objects.requireNonNull(modelo.getMetodoPago(), "El método de pago es obligatorio");
    }

    @Override
    public int insertarPago(Pago modelo, int idReserva) {
        validarIdPositivo(idReserva, "id reserva");
        return this.pagoDao.insertarPago(modelo, idReserva);
    }
}