package pe.edu.pucp.CanchaLibre.dao.usuario;

import pe.edu.pucp.CanchaLibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.modelo.usuario.Administrador;

import java.sql.*;

public class AdministradorDAOImpl extends PersonaBaseDAO<Administrador> implements AdministradorDAO {
    //on BaseDAO
    protected PreparedStatement comandoCrear(Connection conn,
                                             Administrador modelo) throws SQLException{
        String sql = """
                INSERT INTO Administrador(
                    idAdministrador,
                    nombres,
                    contrasena,
                    correo,
                    telefono,
                    intentosFallidos,
                    ultimaSesion,
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        setCamposAdministrador(cmd,modelo);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Administrador modelo) throws SQLException {
        String sql = """
        UPDATE Administrador SET
            nombres = ?,
            contrasena = ?,
            correo = ?,
            telefono = ?,
            intentosFallidos = ?,
            ultimaSesion = ?,
        WHERE idAdministrador = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        int nextIndex = setCamposAdministrador(cmd, modelo);
        cmd.setInt(nextIndex, modelo.getIdUsuario());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM Administrador WHERE idAdministrador = ?";

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = """
            SELECT *
            FROM Administrador
            WHERE idAdministrador = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
        SELECT *
        FROM Administrador
        """;

        return conn.prepareStatement(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                    String nombres) throws SQLException{
        String sql = """
                SELECT * FROM Administrador WHERE nombres = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1,nombres);
        return cmd;
    }

    protected Administrador mapearModelo(ResultSet rs) throws SQLException{
        Administrador modelo = new Administrador();
        modelo.setIdUsuario(rs.getInt("idAdministrador"));

        mapearCamposPersona(rs,modelo);
        return modelo;
    }

    private int setCamposAdministrador(PreparedStatement cmd, Administrador modelo) throws SQLException {
        int startIndex=1;
        cmd.setInt(startIndex,modelo.getIdUsuario());
        int idx = setCamposPersona(cmd,startIndex+1,modelo);
        //admin sin calificacion
        return idx;
    }

}
