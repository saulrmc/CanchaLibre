package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.sql.*;

import java.util.*;

public class PropietarioDAOImpl extends PersonaBaseDAO<Propietario> implements PropietarioDAO {
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
        int nextIndex = setCamposPersona(cmd,1,modelo);
        cmd.setInt(nextIndex, modelo.getCalificacion());
        cmd.setInt(nextIndex+1, modelo.getIdUsuario());

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
        mapearCamposPersona(rs,modelo);
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
        int idx = setCamposPersona(cmd,startIndex+1,modelo);
        cmd.setInt(idx, modelo.getCalificacion());
        return idx + 1;
    }

}
