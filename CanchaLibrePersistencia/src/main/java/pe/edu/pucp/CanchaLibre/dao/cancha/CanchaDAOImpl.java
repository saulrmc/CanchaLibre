package pe.edu.pucp.CanchaLibre.dao.cancha;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Deporte;
import pe.edu.pucp.CanchaLibre.modelo.cancha.Etiqueta;
import pe.edu.pucp.CanchaLibre.modelo.usuario.Propietario;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
        int idProp = modelo.getPropietario().getIdUsuario();
        cmd.setInt(7, idProp);

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

        //String Deportestxt = modelo.getDeportes().stream().map(Deporte::name).collect(Collectors.joining(","));

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1, modelo.getNombre());
        cmd.setString(2, modelo.getDescripcion());
        cmd.setString(3, modelo.getImagenUrl());
        cmd.setBoolean(4, modelo.isDisponible());
        cmd.setString(5, modelo.getDireccion());
        int idProp = modelo.getPropietario().getIdUsuario();
        cmd.setInt(6, idProp);
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
        String sql = "SELECT deporte FROM cancha_deportes WHERE idCancha = ?";
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, idCancha);
        return cmd;
    }

    protected PreparedStatement comandoLeerEtiquetasPorCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "SELECT etiqueta FROM cancha_deportes WHERE idCancha = ?";
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
                String nombreEnum = rs.getString("deporte");
                cancha.getDeportes().add(Deporte.valueOf(nombreEnum));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar deportes: " + e.getMessage());
        }
    }
    private void llenarEtiquetas(Connection conn, Cancha cancha) {
        try (PreparedStatement ps = comandoLeerEtiquetasPorCancha(conn, cancha.getIdCancha());
             ResultSet rs = ps.executeQuery()) {

            if (cancha.getEtiquetas() == null) {
                cancha.setEtiquetas(new ArrayList<>());
            }

            while (rs.next()) {
                String nombreEnum = rs.getString("etiqueta");
                cancha.getEtiquetas().add(Etiqueta.valueOf(nombreEnum));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar etiquetas: " + e.getMessage());
        }
    }

    @Override
    protected Cancha mapearModelo(ResultSet rs) throws SQLException{
        Cancha cancha = new Cancha();
        cancha.setIdCancha(rs.getInt("idCancha"));
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
        llenarEtiquetas(conn, cancha);

        return cancha;
    }
}
