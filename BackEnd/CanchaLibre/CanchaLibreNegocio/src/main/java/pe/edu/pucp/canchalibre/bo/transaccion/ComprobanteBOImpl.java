package pe.edu.pucp.canchalibre.bo.transaccion;

import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.transaccion.ComprobanteDAO;
import pe.edu.pucp.canchalibre.dao.transaccion.ComprobanteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.Estado;
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

        if (estado == Estado.NUEVO) {
            int id = this.comprobanteDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear el comprobante");
            }
            modelo.setIdComprobante(id);

            Comprobante comprobanteCalculado = this.comprobanteDao.leer(id);
            modelo.setNumero(comprobanteCalculado.getNumero());
            modelo.setValorVenta(comprobanteCalculado.getValorVenta());
            modelo.setMontoIgv(comprobanteCalculado.getMontoIgv());
        }
        else {
            throw new IllegalArgumentException("Operación denegada: Los comprobantes son inmutables por regulación fiscal y no soportan el estado: " + estado);        }
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
        validarTextoObligatorio(modelo.getSerie(),"serie del comprobante");
        if (modelo.getMontoBloques() <= 0) {
            throw new IllegalArgumentException("El monto de bloques debe ser mayor que cero");
        }
        // No se valida fechaEmision, numero, valorVenta ni montoIgv.
    }
}