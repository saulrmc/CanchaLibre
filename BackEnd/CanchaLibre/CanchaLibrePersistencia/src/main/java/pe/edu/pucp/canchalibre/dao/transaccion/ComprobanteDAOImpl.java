package pe.edu.pucp.canchalibre.dao.transaccion;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;

import java.sql.*;
import java.time.LocalDateTime;

public class ComprobanteDAOImpl extends DefaultBaseDAO<Comprobante> implements ComprobanteDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Comprobante modelo) throws SQLException {
        String sql = "{call insertarComprobante(?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_serie",modelo.getSerie());
        cmd.setDouble("p_subtotal",modelo.getMontoBloques());
        //numero,fechaEmision,valorVenta calculados en sql
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Comprobante modelo) throws SQLException{
        throw new UnsupportedOperationException("Error: Los comprobantes de venta no pueden ser modificados por regulación fiscal");
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = "{call eliminarComprobante(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = "{call buscarComprobantePorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = "{call listarComprobantes()}";
        return conn.prepareCall(sql);
    }

    protected Comprobante mapearModelo(ResultSet rs) throws SQLException{
        Comprobante comprobante = new Comprobante();

        comprobante.setId(rs.getInt("id"));
        comprobante.setSerie(rs.getString("serie"));
        comprobante.setNumero(rs.getString("numero"));
        comprobante.setFechaEmision(rs.getObject("fechaEmision", LocalDateTime.class));
        comprobante.setMontoBloques(rs.getDouble("montoBloques"));
        comprobante.setValorVenta(rs.getDouble("valorVenta"));

        return comprobante;
    }

}