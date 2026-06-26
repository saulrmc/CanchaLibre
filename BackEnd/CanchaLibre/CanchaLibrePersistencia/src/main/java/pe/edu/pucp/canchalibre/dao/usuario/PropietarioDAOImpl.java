package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;

import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.sql.*;

public class PropietarioDAOImpl extends PersonaBaseDAO<Propietario> implements PropietarioDAO {
//    @Override
//    public Integer crear(Propietario modelo) {
//        return ejecutarComando(conn -> {
//            try (CallableStatement cmd = (CallableStatement) comandoCrear(conn, modelo)) {
//                cmd.execute();
//                return cmd.getInt("p_id");
//            } catch (SQLException e) {
//                System.err.println("Error SQL: " + e.getMessage());
//                throw new RuntimeException(e);
//            }
//        });
//    }

    protected PreparedStatement comandoCrear(Connection conn,
                                             Propietario modelo) throws SQLException{
        String sql = "{call insertarPropietario(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        Integer idCuentaUsuario = getIdCuentaUsuario(modelo);
        if (idCuentaUsuario == null) {
            cmd.setNull("p_idCuentaUsuario", Types.INTEGER);
        }
        else {
            cmd.setInt("p_idCuentaUsuario", idCuentaUsuario);
        }
        cmd.setString("p_nombres",modelo.getNombres());
        cmd.setString("p_correo",modelo.getCorreo());
        cmd.setString("p_telefono",modelo.getTelefono());
        cmd.setString("p_ruc",modelo.getRUC());
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Propietario modelo) throws SQLException {
        String sql = "{call modificarPropietario(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        Integer idCuentaUsuario = getIdCuentaUsuario(modelo);
        if (idCuentaUsuario == null) {
            cmd.setNull("p_idCuentaUsuario", Types.INTEGER);
        }
        else {
            cmd.setInt("p_idCuentaUsuario", idCuentaUsuario);
        }
        cmd.setString("p_nombres",modelo.getNombres());
        cmd.setString("p_correo",modelo.getCorreo());
        cmd.setString("p_telefono",modelo.getTelefono());
        cmd.setDouble("p_calificacion",modelo.getCalificacion());
        cmd.setString("p_ruc",modelo.getRUC());
        cmd.setDouble("p_saldo",modelo.getSaldo());
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.setInt("p_id",modelo.getId());
        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "{call eliminarPropietario(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = "{call buscarPropietarioPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarPropietarios()}";
        return conn.prepareCall(sql);
    }

    protected Propietario mapearModelo(ResultSet rs) throws SQLException{
        Propietario modelo = new Propietario();
        modelo.setId(rs.getInt("id"));

        Integer idCuentaUsuario = leerIdCuentaUsuario(rs);
        if (idCuentaUsuario != null) {
            modelo.setCuentaUsuario(new CuentaUsuarioDAOImpl().leer(idCuentaUsuario));
        }

        mapearCamposPersona(rs,modelo);
        modelo.setCalificacion(rs.getDouble("calificacion"));
        modelo.setRUC(rs.getString("RUC"));
        modelo.setSaldo(rs.getDouble("saldo"));
        modelo.setActivo(rs.getBoolean("activo"));
        return modelo;
    }

    protected PreparedStatement comandoActualizarSaldo(
            Connection conn,
            int id, Double monto)
            throws SQLException {

        String sql = "{call actualizarSaldo(?, ?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idPropietario", id);
        cmd.setDouble("p_monto", monto);

        return cmd;
    }

    @Override
    public void actualizarSaldo(int idPropietario, double monto){
        ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoActualizarSaldo(conn, idPropietario, monto)) {
                cmd.executeUpdate();
            }
            return null;
        });
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn, String nombres) throws SQLException {
        String sql = "{call buscarPropietarioPorNombre(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_nombres", nombres);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoBuscarPorCuenta(Connection conn, String cuenta) throws SQLException {
        String sql = "{call buscarPropietarioPorCuenta(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_userName", cuenta);

        return cmd;
    }


}

