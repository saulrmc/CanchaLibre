package pe.edu.pucp.CanchaLibre.dao.usuario;

import pe.edu.pucp.CanchaLibre.dao.UsuarioBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDAOImpl extends UsuarioBaseDAO<Cliente> implements ClienteDAO {
    //on BaseDAO
    protected PreparedStatement comandoCrear(Connection conn,
                                             Cliente modelo) throws SQLException{
        String sql = """
                INSERT INTO CLIENTE(
                    idUsuario,
                    nombres,
                    contrasena,
                    correo,
                    telefono,
                    intentosFallidos,
                    ultimaSesion,
                    rol,
                    calificacion
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        setCamposCliente(cmd,modelo);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Cliente modelo) throws SQLException {
        String sql = """
        UPDATE Usuario SET
            nombres = ?,
            contrasena = ?,
            correo = ?,
            telefono = ?,
            intentosFallidos = ?,
            ultimaSesion = ?,
            rol = ?,
            calificacion = ?
        WHERE idUsuario = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        int nextIndex = setCamposCliente(cmd, modelo);
        cmd.setInt(nextIndex, modelo.getIdUsuario());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE idUsuario = ?";

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = """
            SELECT *
            FROM CLIENTE
            WHERE idUsuario = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
        SELECT *
        FROM CLIENTE
        """;

        return conn.prepareStatement(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                    String nombres) throws SQLException{
        String sql = """
                SELECT * FROM CLIENTE WHERE nombres = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1,nombres);
        return cmd;
    }

    protected Cliente mapearModelo(ResultSet rs) throws SQLException{
        Cliente modelo = new Cliente();
        modelo.setIdUsuario(rs.getInt("idUsuario"));

        mapearCamposUsuario(rs,modelo);
        modelo.setCalificacion(rs.getInt("calificacion"));
        //List<Reserva> usará otro method
        return modelo;
    }

    private int setCamposCliente(PreparedStatement cmd, Cliente modelo) throws SQLException {
        int idx = setCamposUsuario(cmd,1,modelo);
        cmd.setInt(idx + 1, modelo.getCalificacion());
        return idx + 2;
    }

}
