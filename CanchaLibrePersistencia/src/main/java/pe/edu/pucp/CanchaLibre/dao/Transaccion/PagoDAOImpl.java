package pe.edu.pucp.CanchaLibre.dao.Transaccion;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Pago;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.MetodoPago;

import java.sql.*;

public class PagoDAOImpl extends DefaultBaseDAO<Pago> implements PagoDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Pago modelo) throws SQLException {
        String sql = """
                    INSERT INTO PAGO (
                        id,
                        metodoPago,
                        monto,
                        fechaPago
                    ) VALUES (?, ?, ?, ?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        cmd.setInt(1,modelo.getId());
        cmd.setString(2,modelo.getMetodoPago().name());
        cmd.setDouble(3,modelo.getMonto());
        cmd.setTimestamp(4, java.sql.Timestamp.valueOf(modelo.getFechaPago()));

        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Pago modelo) throws SQLException{
        String sql = """
            UPDATE PAGO
            SET     metodoPago = ?,
                    monto = ?,
                    fechaPago = ?
            WHERE id = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);

        cmd.setString(1,modelo.getMetodoPago().name());
        cmd.setDouble(2,modelo.getMonto());
        cmd.setTimestamp(3, java.sql.Timestamp.valueOf(modelo.getFechaPago()));
        cmd.setInt(4,modelo.getId());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = """
                DELETE FROM PAGO WHERE id = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = """
                SELECT * FROM PAGO WHERE id = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = """
                SELECT * FROM PAGO
                """;
        return conn.prepareStatement(sql);
    }

    protected Pago mapearModelo(ResultSet rs) throws SQLException{
        Pago pago = new Pago();
        pago.setId(rs.getInt("id"));
        pago.setMetodoPago(MetodoPago.valueOf(rs.getString("metodoPago")));
        pago.setMonto(rs.getDouble("monto"));
        pago.setFechaPago(rs.getTimestamp("FechaPago").toLocalDateTime());
        return pago;
    }

}
