package pe.edu.pucp.CanchaLibre.dao.resena;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.resena.Resena;
import pe.edu.pucp.CanchaLibre.modelo.usuario.Cliente;

import java.sql.*;
import java.time.LocalDateTime;

public class ResenaDAOImpl extends DefaultBaseDAO<Resena> implements ResenaDAO {
    @Override
    protected PreparedStatement comandoCrear(Connection conn,
                                             Resena modelo) throws SQLException{
        String sql = """
            INSERT INTO RESENA (
                idResena,
                descripcion,
                calificacion,
                fechaPublicacion,
                idCancha,
                idCliente,
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        cmd.setInt(1,modelo.getIdResena());
        cmd.setString(2,modelo.getDescripcion());
        cmd.setInt(3,modelo.getCalificacion());
        cmd.setObject(4,modelo.getFechaPublicacion());
        cmd.setInt(5,modelo.getCliente().getIdUsuario());
        cmd.setInt(6,modelo.getCancha().getIdCancha());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn, Resena modelo) throws SQLException {
        String sql = """
            UPDATE RESENA
            SET descripcion = ?,
                calificacion = ?,
                fechaPublicacion = ?,
                idCliente = ?,
                idCancha = ?
            WHERE idResena = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1,modelo.getDescripcion());
        cmd.setInt(2,modelo.getCalificacion());
        cmd.setObject(3,modelo.getFechaPublicacion());
        cmd.setInt(4,modelo.getCliente().getIdUsuario());
        cmd.setInt(5,modelo.getCancha().getIdCancha());
        cmd.setInt(6,modelo.getIdResena());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = """
                DELETE FROM RESENA WHERE idResena = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = """
                SELECT * FROM RESENA WHERE idResena = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
                SELECT * FROM RESENA
                """;
        return conn.prepareStatement(sql);
    }

    @Override
    protected Resena mapearModelo(ResultSet rs) throws SQLException {
        Resena resena = new Resena();
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("idUsuario"));
        Cancha cancha = new Cancha();
        cancha.setIdCancha(rs.getInt("idCancha"));

        resena.setIdResena(rs.getInt("idResena"));
        resena.setDescripcion(rs.getString("descripcion"));
        resena.setCalificacion(rs.getInt("calificacion"));
        resena.setFechaPublicacion((LocalDateTime) rs.getObject("fechaPublicacion"));
        resena.setCliente(cliente);
        resena.setCancha(cancha);
        return resena;
    }
}
