package pe.edu.pucp.CanchaLibre.dao.reserva;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.CanchaLibre.modelo.reserva.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.usuario.Cliente;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class ReservaDAOImpl extends DefaultBaseDAO<Reserva> implements ReservaDAO {

    @Override
    protected PreparedStatement comandoCrear(Connection conn, Reserva modelo) throws SQLException {
        String sql = """
            INSERT INTO Reserva (
                idReserva,
                fechaHora,
                duracion,
                estado,
                idCancha,
                idCliente
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        cmd.setInt(1,modelo.getIdReserva());
        cmd.setObject(2,modelo.getFechaHora());
        cmd.setObject(3,modelo.getDuracion());
        cmd.setObject(4,modelo.getEstado().name());
        cmd.setInt(5,modelo.getCancha().getIdCancha());
        cmd.setInt(6,modelo.getCliente().getIdUsuario());

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
            WHERE idResena = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setObject(1,modelo.getFechaHora());
        cmd.setObject(2,modelo.getDuracion());
        cmd.setObject(3,modelo.getEstado().name());
        cmd.setInt(4,modelo.getCancha().getIdCancha());
        cmd.setInt(5,modelo.getCliente().getIdUsuario());
        cmd.setInt(6,modelo.getIdReserva());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = """
                DELETE FROM Reserva WHERE idReserva = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = """
                SELECT * FROM Reserva WHERE idReserva = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
                SELECT * FROM Reserva
                """;
        return conn.prepareStatement(sql);
    }

    @Override
    protected Reserva mapearModelo(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("idUsuario"));
        Cancha cancha = new Cancha();
        cancha.setIdCancha(rs.getInt("idCancha"));
        reserva.setIdReserva(rs.getInt("idResena"));
        reserva.setFechaHora((LocalDateTime) rs.getObject("fechaHora"));
        reserva.setDuracion((LocalTime) rs.getObject("duracion"));
        reserva.setEstado((EstadoReserva) rs.getObject("estado"));
        reserva.setCliente(cliente);
        reserva.setCancha(cancha);
        return reserva;
    }
}
