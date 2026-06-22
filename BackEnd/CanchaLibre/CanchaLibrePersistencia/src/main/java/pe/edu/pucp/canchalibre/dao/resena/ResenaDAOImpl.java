package pe.edu.pucp.canchalibre.dao.resena;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResenaDAOImpl extends DefaultBaseDAO<Resena> implements ResenaDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Resena modelo) throws SQLException{
        String sql = "{call insertarResena(?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_descripcion",modelo.getDescripcion());
        cmd.setDouble("p_calificacion",modelo.getCalificacion());
        cmd.setObject("p_fechaPublicacion",modelo.getFechaPublicacion());
        cmd.setInt("p_idReserva",modelo.getReserva().getIdReserva());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Resena modelo) throws SQLException {
        String sql = "{call modificarResena(?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_descripcion",modelo.getDescripcion());
        cmd.setDouble("p_calificacion",modelo.getCalificacion());
        cmd.setObject("p_fechaPublicacion",modelo.getFechaPublicacion());
        cmd.setInt("p_id",modelo.getIdResena());
        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "{call eliminarResena(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = "{call buscarResenaPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarResenas()}";
        return conn.prepareStatement(sql);
    }

    protected Resena mapearModelo(ResultSet rs) throws SQLException {
        Resena resena = new Resena();
        resena.setIdResena(rs.getInt("idResena"));
        resena.setDescripcion(rs.getString("descripcion"));
        resena.setCalificacion(rs.getDouble("calificacion"));
        resena.setFechaPublicacion(rs.getObject("fechaPublicacion", LocalDateTime.class));
        resena.setReserva(new ReservaDAOImpl().leer(rs.getInt("idReserva")));
        return resena;
    }

    protected PreparedStatement comandoListarResenasPorCancha(
            Connection conn, Integer idCancha) throws SQLException{
        String sql = "{call listarResenasPorCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha",idCancha);
        return cmd;
    }

    @Override
    public List<Resena> listarResenasPorCancha(Integer idCancha){
        return ejecutarComando(conn ->{
            try (PreparedStatement cmd =
                         this.comandoListarResenasPorCancha(conn, idCancha)) {
                ResultSet rs = cmd.executeQuery();

                List<Resena> modelos = new ArrayList<>();
                while (rs.next()) {
                    modelos.add(this.mapearModelo(rs));
                }

                return modelos;
            }
        });
    }

    protected PreparedStatement comandoListarResenasPorCliente(
            Connection conn, Integer idCliente) throws SQLException{
        String sql = "{call listarResenasPorCliente(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha",idCliente);
        return cmd;
    }

    @Override
    public List<Resena> listarResenasPorCliente(Integer idCliente){
        return ejecutarComando(conn ->{
            try (PreparedStatement cmd =
                         this.comandoListarResenasPorCancha(conn, idCliente)) {
                ResultSet rs = cmd.executeQuery();

                List<Resena> modelos = new ArrayList<>();
                while (rs.next()) {
                    modelos.add(this.mapearModelo(rs));
                }

                return modelos;
            }
        });
    }
}
