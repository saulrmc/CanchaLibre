package pe.edu.pucp.CanchaLibre.dao.cancha;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Deporte;
import pe.edu.pucp.CanchaLibre.modelo.usuario.Propietario;

import java.sql.*;
import java.util.ArrayList;

public class CanchaDAOImpl extends DefaultBaseDAO<Cancha> implements CanchaDAO {
    @Override
    protected PreparedStatement comandoCrear(Connection conn,
                                             Cancha modelo) throws SQLException{
        String sql = """
            INSERT INTO Cancha (
                idCancha,
                nombre,
                descripcion,
                imagenUrl,
                disponible,
                direccion,
                idPropietario
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement cmd = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        cmd.setInt(1,modelo.getIdCancha());
        cmd.setString(2,modelo.getNombre());
        cmd.setString(3,modelo.getDescripcion());
        cmd.setString(4,modelo.getImagenUrl());
        cmd.setBoolean(5,modelo.isDisponible());
        cmd.setString(6,modelo.getDireccion());
        cmd.setInt(7, modelo.getPropietario().getIdUsuario());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Cancha modelo) throws SQLException{
        String sql = """
            UPDATE Cancha
            SET nombre = ?,
                descripcion = ?,
                imagenUrl = ?,
                disponible = ?,
                direccion = ?,
                idPropietario = ?
            WHERE idCancha = ?
            """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1, modelo.getNombre());
        cmd.setString(2, modelo.getDescripcion());
        cmd.setString(3, modelo.getImagenUrl());
        cmd.setBoolean(4, modelo.isDisponible());
        cmd.setString(5, modelo.getDireccion());
        cmd.setInt(6, modelo.getPropietario().getIdUsuario());
        cmd.setInt(7, modelo.getIdCancha());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = """
                DELETE FROM Cancha WHERE idCancha = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = """
                SELECT * FROM Cancha WHERE idCancha = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1,id);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = """
                SELECT * FROM Cancha
                """;
        return conn.prepareStatement(sql);
    }

    protected PreparedStatement comandoLeerDeportesPorCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "SELECT nombreDeporte FROM Cancha_Deporte WHERE idCancha = ?";
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, idCancha);
        return cmd;
    }

    private void llenarDeportes(Connection conn, Cancha cancha) {
        try (PreparedStatement ps = comandoLeerDeportesPorCancha(conn, cancha.getIdCancha());
             ResultSet rs = ps.executeQuery()) {

            if (cancha.getDeportes() == null) {
                cancha.setDeportes(new ArrayList<>());
            }

            while (rs.next()) {
                String nombreEnum = rs.getString("nombreDeporte");
                cancha.getDeportes().add(Deporte.valueOf(nombreEnum));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar deportes: " + e.getMessage());
        }
    }

    @Override
    protected Cancha mapearModelo(ResultSet rs) throws SQLException{
        Cancha cancha = new Cancha();
        cancha.setIdCancha(rs.getInt("id"));
        cancha.setNombre(rs.getString("nombre"));
        cancha.setDescripcion(rs.getString("descripcion"));
        cancha.setImagenUrl(rs.getString("imagenUrl"));
        cancha.setDisponible(rs.getBoolean("disponible"));
        cancha.setDireccion(rs.getString("direccion"));

        Propietario p = new Propietario();
        p.setIdUsuario(rs.getInt("idPropietario")); // DB
        cancha.setPropietario(p);

        Connection conn = rs.getStatement().getConnection();
        llenarDeportes(conn, cancha);

        return cancha;
    }
}
