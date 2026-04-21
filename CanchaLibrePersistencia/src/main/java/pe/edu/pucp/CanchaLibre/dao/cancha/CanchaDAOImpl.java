package pe.edu.pucp.CanchaLibre.dao.cancha;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;

import java.sql.*;

public class CanchaDAOImpl extends DefaultBaseDAO<Cancha> implements CanchaDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Cancha modelo) throws SQLException{
        String sql = """
            INSERT INTO CANCHA (
                idCancha,
                nombre,
                descripcion,
                imagenUrl,
                disponible,
                direccion
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        cmd.setInt(1,modelo.getIdCancha());
        cmd.setString(2,modelo.getNombre());
        cmd.setString(3,modelo.getDescripcion());
        cmd.setString(4,modelo.getImagenUrl());
        cmd.setBoolean(5,modelo.isDisponible());
        cmd.setString(6,modelo.getDireccion());

        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Cancha modelo) throws SQLException{
        String sql = """
            UPDATE CANCHA
            SET nombre = ?,
                descripcion = ?,
                imagenUrl = ?,
                disponible = ?,
                direccion = ?
            WHERE idCancha = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1, modelo.getNombre());
        cmd.setString(2, modelo.getDescripcion());
        cmd.setString(3, modelo.getImagenUrl());
        cmd.setBoolean(4, modelo.isDisponible());
        cmd.setString(5, modelo.getDireccion());
        cmd.setInt(6, modelo.getIdCancha());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = """
                DELETE FROM CANCHA WHERE idCancha = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
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