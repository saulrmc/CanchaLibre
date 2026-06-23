package pe.edu.pucp.canchalibre.dao.transaccion;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.transaccion.MetodoPago;

import java.sql.*;
import java.time.LocalDateTime;

public class PagoDAOImpl extends DefaultBaseDAO<Pago> implements PagoDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Pago modelo) throws SQLException {
        String sql = "{call insertarPago(?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_metodoPago",modelo.getMetodoPago().name());
        cmd.setDouble("p_monto",modelo.getMonto());
        cmd.setObject("p_fechaPago",modelo.getFechaPago());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Pago modelo) throws SQLException{
        String sql = "{call modificarPago(?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_metodoPago",modelo.getMetodoPago().name());
        cmd.setDouble("p_monto",modelo.getMonto());
        cmd.setObject("p_fechaPago",modelo.getFechaPago());
        if(modelo.getComprobante()==null){
            cmd.setNull("p_idComprobante",Types.INTEGER);
        }else{
            cmd.setInt("p_idComprobante",modelo.getComprobante().getIdComprobante());
        }
        cmd.setInt("p_id",modelo.getIdPago());
        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = "{call eliminarPago(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = "{call buscarPagoPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = "{call listarPagos()}";
        return conn.prepareStatement(sql);
    }

    protected Pago mapearModelo(ResultSet rs) throws SQLException{
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("idPago"));
        pago.setMetodoPago(MetodoPago.valueOf(rs.getString("metodoPago")));
        pago.setMonto(rs.getDouble("monto"));
        pago.setFechaPago(rs.getObject("fechaPago", LocalDateTime.class));
        int idComprobante = rs.getInt("idComprobante");
        if(!rs.wasNull()){
            pago.setComprobante(new ComprobanteDAOImpl().leer(idComprobante));
        }
        return pago;
    }

}
