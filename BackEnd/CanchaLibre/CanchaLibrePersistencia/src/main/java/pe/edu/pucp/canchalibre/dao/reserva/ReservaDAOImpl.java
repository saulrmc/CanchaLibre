package pe.edu.pucp.canchalibre.dao.reserva;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.transaccion.MetodoPago;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.sql.*;


public class ReservaDAOImpl extends DefaultBaseDAO<Reserva> implements ReservaDAO {

    @Override
    protected PreparedStatement comandoCrear(Connection conn, Reserva modelo) throws SQLException {
        String sql = "{call insertrarReserva()}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_estado",modelo.getEstado().name());
        cmd.setObject("p_fechaHora",modelo.getFechaHora());
        cmd.setInt("p_idCliente",modelo.getCliente().getId());
        cmd.setInt("p_idCancha",modelo.getCancha().getId());
        //if(modelo.getPago())
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn, Reserva modelo) throws SQLException {
        String sql = """
        UPDATE Reserva
        SET fechaHora = ?,
            duracion = ?,
            estado = ?,
            idCancha = ?,
            idCliente = ?
        WHERE idReserva = ?
    """;

        PreparedStatement cmd = conn.prepareStatement(sql);

        cmd.setObject(1, modelo.getFechaHora());
        cmd.setObject(2, modelo.getDuracion());
        cmd.setString(3, modelo.getEstado().name());
        cmd.setInt(4, modelo.getCancha().getIdCancha());
        cmd.setInt(5, modelo.getCliente().getIdUsuario());
        cmd.setInt(6, modelo.getIdReserva());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn, Integer idPago) throws SQLException {
        String sql = """
        DELETE FROM Reserva WHERE idReserva = ?
    """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, idPago);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn, Integer idPago) throws SQLException{
        String sql = """
        SELECT 
            r.idReserva,
            r.fechaHora,
            r.duracion,
            r.estado,
            r.idCancha,
            r.idCliente,
            p.idPago AS idPago,
            p.metodoPago,
            p.monto,
            p.fechaPago
        FROM Reserva r
        LEFT JOIN Pago p ON p.idReserva = r.idReserva
        WHERE r.idReserva = ?
    """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, idPago);


        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
        SELECT 
            r.idReserva,
            r.fechaHora,
            r.duracion,
            r.estado,
            r.idCancha,
            r.idCliente,
            p.idPago AS idPago,
            p.metodoPago,
            p.monto,
            p.fechaPago
        FROM Reserva r
        LEFT JOIN Pago p ON p.idReserva = r.idReserva
    """;
        return conn.prepareStatement(sql);
    }

    @Override
    protected Reserva mapearModelo(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("idCliente"));

        Cancha cancha = new Cancha();
        cancha.setIdCancha(rs.getInt("idCancha"));

        reserva.setIdReserva(rs.getInt("idReserva"));
        reserva.setFechaHora(rs.getTimestamp("fechaHora").toLocalDateTime());
        reserva.setDuracion(rs.getTime("duracion").toLocalTime());
        reserva.setEstado(EstadoReserva.valueOf(rs.getString("estado")));
        reserva.setCliente(cliente);
        reserva.setCancha(cancha);

        int idPago = rs.getInt("idPago");

        if (!rs.wasNull()) {
            Pago pago = new Pago();
            pago.setIdPago(idPago);
            pago.setMonto(rs.getDouble("monto"));

            String metodoPago = rs.getString("metodoPago");
            if (metodoPago != null) {
                pago.setMetodoPago(MetodoPago.valueOf(metodoPago));
            }

            Timestamp fechaPago = rs.getTimestamp("fechaPago");
            if (fechaPago != null) {
                pago.setFechaPago(fechaPago.toLocalDateTime());
            }

            pago.setReserva(reserva);
            reserva.setPago(pago);
        }


        return reserva;
    }
}
