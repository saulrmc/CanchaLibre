package pe.edu.pucp.CanchaLibre.dao.usuario;

import pe.edu.pucp.CanchaLibre.dao.UsuarioBaseDAO;
import pe.edu.pucp.CanchaLibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.CanchaLibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;

import pe.edu.pucp.CanchaLibre.modelo.Usuario.Propietario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;

public class PropietarioDAOImpl extends UsuarioBaseDAO<Propietario> implements PropietarioDAO {
    //on BaseDAO
    protected PreparedStatement comandoCrear(Connection conn,
                                             Propietario modelo) throws SQLException{
        String sql = """
                INSERT INTO Propietario(
                    idPropietario,
                    nombres,
                    contrasena,
                    correo,
                    telefono,
                    intentosFallidos,
                    ultimaSesion,
                    calificacion
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        setCamposPropietario(cmd,modelo);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Propietario modelo) throws SQLException {
        String sql = """
        UPDATE Propietario SET
            nombres = ?,
            contrasena = ?,
            correo = ?,
            telefono = ?,
            intentosFallidos = ?,
            ultimaSesion = ?,
            calificacion = ?
        WHERE idPropietario = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        int nextIndex = setCamposPropietario(cmd, modelo);
        cmd.setInt(nextIndex, modelo.getIdUsuario());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM Propietario WHERE idPropietario = ?";

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = """
            SELECT *
            FROM Propietario
            WHERE idPropietario = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
        SELECT *
        FROM Propietario
        """;

        return conn.prepareStatement(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                    String nombres) throws SQLException{
        String sql = """
                SELECT * FROM Propietario WHERE nombres = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1,nombres);
        return cmd;
    }

    protected Propietario mapearModelo(ResultSet rs) throws SQLException{
        Propietario modelo = new Propietario();
        modelo.setIdUsuario(rs.getInt("idPropietario"));
        mapearCamposUsuario(rs,modelo);
        modelo.setCalificacion(rs.getInt("calificacion"));

        CanchaDAO canchaDAO = new CanchaDAOImpl();
        List<Cancha> listaCanchas = canchaDAO.leerTodos();
		List<Cancha> listaCanchasPropietario=new ArrayList<>();

        if (listaCanchas != null) {
            for (Cancha c : listaCanchas) {
                if (c.getPropietario() != null &&
                        c.getPropietario().getIdUsuario() == modelo.getIdUsuario()) {
                    listaCanchasPropietario.add(c);
                }
            }
        }
		modelo.setCanchas(listaCanchasPropietario);
        return modelo;
    }

    private int setCamposPropietario(PreparedStatement cmd, Propietario modelo) throws SQLException {
        int startIndex=1;
        cmd.setInt(startIndex,modelo.getIdUsuario());
        int idx = setCamposUsuario(cmd,startIndex+1,modelo);
        cmd.setInt(idx, modelo.getCalificacion());
        return idx + 1;
    }

}
