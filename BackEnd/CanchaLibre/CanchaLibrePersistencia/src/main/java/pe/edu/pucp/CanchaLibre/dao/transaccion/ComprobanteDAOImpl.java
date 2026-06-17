package pe.edu.pucp.CanchaLibre.dao.transaccion;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;

import java.sql.*;

public class ComprobanteDAOImpl extends DefaultBaseDAO<Comprobante> implements ComprobanteDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Comprobante modelo) throws SQLException {
        String sql = """
        INSERT INTO Comprobante (
            igv,
            monto,
            fechaEmision,
            idReserva
        ) VALUES (?, ?, ?, ?)
    """;

        PreparedStatement cmd = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        Reserva res = modelo.getReserva();

        cmd.setDouble(1, modelo.getIgv());
        cmd.setDouble(2, res.getPago().getMonto());
        cmd.setTimestamp(3, Timestamp.valueOf(modelo.getFechaEmision()));
        cmd.setInt(4, res.getIdReserva());
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Comprobante modelo) throws SQLException{
        String sql = """
        UPDATE Comprobante
        SET igv = ?,
            monto = ?,
            fechaEmision = ?,
            idReserva = ?
        WHERE idComprobante = ?
    """;

        PreparedStatement cmd = conn.prepareStatement(sql);

        Reserva res = modelo.getReserva();

        cmd.setDouble(1, modelo.getIgv());
        cmd.setDouble(2, res.getPago().getMonto());
        cmd.setTimestamp(3, Timestamp.valueOf(modelo.getFechaEmision()));
        cmd.setInt(4, res.getIdReserva());
        cmd.setInt(5, modelo.getIdComprobante());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = """
                DELETE FROM Comprobante WHERE idComprobante = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = """
                SELECT * FROM Comprobante WHERE idComprobante = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = """
                SELECT * FROM Comprobante
                """;
        return conn.prepareStatement(sql);
    }

    protected Comprobante mapearModelo(ResultSet rs) throws SQLException{
        Comprobante comprobante = new Comprobante();

        Reserva reserva = new Reserva();
        reserva.setIdReserva(rs.getInt("idReserva"));

        Pago pago = new Pago();
        pago.setMonto(rs.getDouble("monto"));

        reserva.setPago(pago);
        comprobante.setReserva(reserva);

        comprobante.setIdComprobante(rs.getInt("idComprobante"));
        comprobante.setIgv(rs.getDouble("igv"));
        comprobante.setFechaEmision(
                rs.getTimestamp("fechaEmision") != null
                        ? rs.getTimestamp("fechaEmision").toLocalDateTime()
                        : null
        );
        
        return comprobante;
    }

}