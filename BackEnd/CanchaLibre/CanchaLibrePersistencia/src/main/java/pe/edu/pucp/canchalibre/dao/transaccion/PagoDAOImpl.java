package pe.edu.pucp.canchalibre.dao.transaccion;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.transaccion.MetodoPago;

import java.sql.*;

public class PagoDAOImpl extends DefaultBaseDAO<Pago> implements PagoDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Pago modelo) throws SQLException {
        String sql = """
                    INSERT INTO Pago (
                        idPago,
                        metodoPago,
                        monto,
                        fechaPago
                    ) VALUES (?, ?, ?, ?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        cmd.setInt(1,modelo.getIdPago());
        cmd.setString(2,modelo.getMetodoPago().name());
        cmd.setDouble(3,modelo.getMonto());
        cmd.setTimestamp(4, java.sql.Timestamp.valueOf(modelo.getFechaPago()));

        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Pago modelo) throws SQLException{
        String sql = """
            UPDATE Pago
            SET     metodoPago = ?,
                    monto = ?,
                    fechaPago = ?
            WHERE idPago = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);

        cmd.setString(1,modelo.getMetodoPago().name());
        cmd.setDouble(2,modelo.getMonto());
        cmd.setTimestamp(3, java.sql.Timestamp.valueOf(modelo.getFechaPago()));
        cmd.setInt(4,modelo.getIdPago());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer idPago) throws SQLException{
        String sql = """
                DELETE FROM Pago WHERE idPago = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,idPago);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer idPago) throws SQLException{
        String sql = """
                SELECT * FROM Pago WHERE idPago = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,idPago);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = """
                SELECT * FROM Pago
                """;
        return conn.prepareStatement(sql);
    }

    @Override
    protected Pago mapearModelo(ResultSet rs) throws SQLException{
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("idPago"));
        pago.setMetodoPago(MetodoPago.valueOf(rs.getString("metodoPago")));
        pago.setMonto(rs.getDouble("monto"));
        pago.setFechaPago(rs.getTimestamp("FechaPago").toLocalDateTime());
        return pago;
    }

}
