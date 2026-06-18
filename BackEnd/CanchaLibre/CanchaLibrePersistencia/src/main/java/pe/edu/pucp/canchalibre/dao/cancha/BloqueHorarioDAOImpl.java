package pe.edu.pucp.canchalibre.dao.cancha;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;
import pe.edu.pucp.canchalibre.modelo.cancha.DiaSemana;
import pe.edu.pucp.canchalibre.modelo.cancha.EstadoBloque;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BloqueHorarioDAOImpl extends DefaultBaseDAO<BloqueHorario> implements BloqueHorarioDAO {
    private PreparedStatement comandoCrearBloqueHorario(Connection conn,
                                                 Integer idCancha,
                                                 BloqueHorario bloque) throws SQLException{
        String sql = "{call insertarBloqueHorario(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha",idCancha);
        cmd.setInt("p_dia",bloque.getDia().ordinal()+1);
        cmd.setObject("p_horaInicio", bloque.getHoraInicio());
        cmd.setObject("p_horaFin", bloque.getHoraFin());
        cmd.setDouble("p_precio",bloque.getPrecio());
        cmd.setString("p_estado",bloque.getEstado().name());
        cmd.setBoolean("p_activo",bloque.isActivo());
        cmd.registerOutParameter("p_id", Types.INTEGER);
        return cmd;
    }

    private PreparedStatement comandoLeerBloquesPorCancha(Connection conn,
                                                           Integer idCancha) throws SQLException{
        String sql = "{call listarBloquesPorCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha", idCancha);
        return cmd;
    }

    private PreparedStatement comandoEliminarBloqueHorario(Connection conn,
                                                           Integer idBloqueHorario) throws SQLException{
        String sql = "{call eliminarBloqueHorario(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idBloqueHorario);
        return cmd;
    }

    private BloqueHorario mapearBloqueHorario(ResultSet rs) throws SQLException{
        BloqueHorario bloque = new BloqueHorario();
        bloque.setId(rs.getInt("id"));
        bloque.setDia(DiaSemana.values()[rs.getInt("dia") - 1]);        bloque.setHoraInicio(rs.getObject("horaInicio", LocalTime.class));
        bloque.setHoraFin(rs.getObject("horaFin", LocalTime.class));
        bloque.setPrecio(rs.getDouble("precio"));
        bloque.setEstado(EstadoBloque.valueOf(rs.getString("estado")));
        bloque.setActivo(rs.getBoolean("activo"));
        return bloque;
    }

    @Override
    public Integer crear(BloqueHorario modelo){
        throw new UnsupportedOperationException("Use crearBloquesPorCancha(conn, idCancha, bloques) para crear bloques");
    }

    @Override
    public boolean actualizar(BloqueHorario modelo){
        return ejecutarComando(conn -> this.ejecutarComandoActualizar(conn, modelo));
    }

    @Override
    public boolean eliminar(Integer id){
        return ejecutarComando(conn -> this.ejecutarComandoEliminar(conn, id));
    }

    @Override
    public BloqueHorario leer(Integer id){
        return ejecutarComando(conn -> {
            try(PreparedStatement cmd = this.comandoLeer(conn, id);
                ResultSet rs = cmd.executeQuery()) {
                if(!rs.next()) {
                    return null;
                }
                return this.mapearModelo(rs);
            }
        });
    }

    @Override
    public List<BloqueHorario> leerTodos(){
        return ejecutarComando(conn -> {
            try(PreparedStatement cmd = this.comandoLeerTodos(conn);
                ResultSet rs = cmd.executeQuery()){
                List<BloqueHorario> bloques = new ArrayList<>();
                while(rs.next()){
                    bloques.add(this.mapearModelo(rs));
                }
                return bloques;
            }
        });
    }

    @Override
    public void crearBloquesPorCancha(Connection conn,
                                      Integer idCancha,
                                      List<BloqueHorario> bloques) throws SQLException{
        if (bloques == null || bloques.isEmpty()){
            return;
        }

        for(BloqueHorario bloque : bloques){
            try(PreparedStatement cmd = this.comandoCrearBloqueHorario(conn, idCancha, bloque)){
                if(cmd.executeUpdate()==0){
                    throw new SQLException("No se pudo insertar un bloque de horario");
                }
                if(cmd instanceof CallableStatement callableCmd){
                    bloque.setId(callableCmd.getInt("p_id"));
                }
            }
        }
    }

    @Override
    public List<BloqueHorario> leerBloquesPorCancha(Connection conn,
                                                        Integer idCancha) throws SQLException{
        List<BloqueHorario> bloques = new ArrayList<>();
        try(PreparedStatement cmd = this.comandoLeerBloquesPorCancha(conn, idCancha);
            ResultSet rs = cmd.executeQuery()) {
            while (rs.next()) {
                try(PreparedStatement cmdLeer = this.comandoLeer(conn, rs.getInt("id"));
                    ResultSet rsBloque = cmdLeer.executeQuery()){
                    if(rsBloque.next()) {
                        bloques.add(this.mapearModelo(rsBloque));
                    }
                }
            }
        }
        return bloques;
    }

    @Override
    public void eliminarBloquePorCancha(Connection conn,
                                        Integer idCancha) throws SQLException{
        try (PreparedStatement cmd = this.comandoLeerBloquesPorCancha(conn, idCancha);
             ResultSet rs = cmd.executeQuery()) {
            while (rs.next()){
                this.ejecutarComandoEliminar(conn, rs.getInt("id"));
            }
        }
    }

    @Override
    protected PreparedStatement comandoCrear(Connection conn,
                                             BloqueHorario modelo) throws SQLException{
        throw new UnsupportedOperationException("Use crearBloquesPorCancha(conn, idCancha, bloques) para crear bloques");
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn,
                                                  BloqueHorario modelo) throws SQLException{
        String sql = "{call modificarBloqueHorario()}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_dia",modelo.getDia().ordinal()+1);
        cmd.setObject("p_horaInicio", modelo.getHoraInicio());
        cmd.setObject("p_horaFin", modelo.getHoraFin());
        cmd.setDouble("p_precio",modelo.getPrecio());
        cmd.setString("p_estado",modelo.getEstado().name());
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id", Types.INTEGER);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException{
        return this.comandoEliminarBloqueHorario(conn, id);
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException{
        String sql = "{call buscarBloqueHorarioPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = "{call listar BloquesHorario(?)}";
        return conn.prepareCall(sql);
    }

    @Override
    protected BloqueHorario mapearModelo(ResultSet rs) throws SQLException{
        return this.mapearBloqueHorario(rs);
    }
}
