package pe.edu.pucp.CanchaLibre.dao.cancha;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;

import java.sql.*;

public class CanchaDAOImpl {

}

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = """
                SELECT * FROM CANCHA WHERE idCancha = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = """
                SELECT * FROM CANCHA
                """;
        return conn.prepareStatement(sql);
    }

    protected Cancha mapearModelo(ResultSet rs) throws SQLException{
        Cancha cancha = new Cancha();
        cancha.setIdCancha(rs.getInt("id"));
        cancha.setNombre(rs.getString("nombre"));
        cancha.setDescripcion(rs.getString("descripcion"));
        cancha.setImagenUrl(rs.getString("imagenUrl"));
        cancha.setDisponible(rs.getBoolean("disponible"));
        cancha.setDireccion(rs.getString("direccion"));
        //List<Deporte> tiene relacion 1 a muchos
        return cancha;
    }
}