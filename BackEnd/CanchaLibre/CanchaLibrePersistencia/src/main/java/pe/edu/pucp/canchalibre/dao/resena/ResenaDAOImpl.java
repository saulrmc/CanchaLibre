package pe.edu.pucp.canchalibre.dao.resena;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.sql.*;
import java.time.LocalDateTime;

public class ResenaDAOImpl extends DefaultBaseDAO<Resena> implements ResenaDAO {
    @Override
    protected PreparedStatement comandoCrear(Connection conn,
                                             Resena modelo) throws SQLException{
        String sql = "{call insertarResena(?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_descripcion",modelo.getDescripcion());
        cmd.setInt("p_calificacion",modelo.getCalificacion());
        cmd.setDate("p_fechaPublicacion",new java.sql.Date(modelo.getFechaPublicacion().getTime()));
        cmd.setInt("p_idCancha",modelo.getCliente().getIdUsuario());
        cmd.setInt("p_idCliente",modelo.getCancha().getIdCancha());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn, Resena modelo) throws SQLException {
        String sql = "{call modificarResena(?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        String sql = """
            UPDATE RESENA
            SET descripcion = ?,
                calificacion = ?,
                fechaPublicacion = ?,
                idCliente = ?,
                idCancha = ?
            WHERE idResena = ?
            """;

        cmd.setString("p_descripcion",modelo.getDescripcion());
        cmd.setInt("p_calificacion",modelo.getCalificacion());
        cmd.setDate("p_fechaPublicacion",new java.sql.Date(modelo.getFechaPublicacion().getTime()));
        cmd.setInt("p_idCliente",modelo.getCliente().getIdUsuario());
        cmd.setInt("p_idCancha",modelo.getCancha().getIdCancha());
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
