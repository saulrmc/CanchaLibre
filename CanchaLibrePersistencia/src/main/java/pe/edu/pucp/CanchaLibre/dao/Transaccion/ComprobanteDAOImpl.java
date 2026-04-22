package pe.edu.pucp.CanchaLibre.dao.Transaccion;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Comprobante;
import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Pago;

import java.sql.*;

public class ComprobanteDAOImpl extends DefaultBaseDAO<Comprobante> implements ComprobanteDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Comprobante modelo) throws SQLException {
        String sql = """
                    INSERT INTO COMPROBANTE (
                        idComprobante,
                        monto,
                        idReserva
                    ) VALUES (?, ?, ?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        Reserva res = modelo.getReserva();
        cmd.setInt(1, modelo.getIdComprobante());
        cmd.setDouble(2, res.getPago().getMonto());
        cmd.setInt(3, res.getIdReserva());
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Comprobante modelo) throws SQLException{
        String sql = """
            UPDATE COMPROBANTE
            SET     monto = ?,
                    idReserva = ?
            WHERE idComprobante = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);

        Reserva res= modelo.getReserva();
        cmd.setDouble(1, res.getPago().getMonto());
        cmd.setInt(2, res.getIdReserva());
        cmd.setInt(3, modelo.getIdComprobante());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = """
                DELETE FROM COMPROBANTE WHERE idComprobante = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = """
                SELECT * FROM COMPROBANTE WHERE idComprobante = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = """
                SELECT * FROM COMPROBANTE
                """;
        return conn.prepareStatement(sql);
    }

    protected Comprobante mapearModelo(ResultSet rs) throws SQLException{
        Comprobante comprobante = new Comprobante();
        Reserva reserva = new Reserva();
        Pago pago = new Pago();
        
        comprobante.setIdComprobante(rs.getInt("idComprobante"));
        reserva.setIdReserva(rs.getInt("idReserva"));
        pago.setMonto(rs.getDouble("monto"));
        
        return comprobante;
    }

}