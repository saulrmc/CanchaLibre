package pe.edu.pucp.CanchaLibre.dao.cuentas;

import pe.edu.pucp.CanchaLibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class CuentaUsuarioDAOImpl extends DefaultBaseDAO<CuentaUsuario> implements CuentaUsuarioDAO {
    @Override
    public boolean login(String username, String password) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoLogin(conn, username, password)) {
                if (cmd instanceof CallableStatement callableCmd) {
                    callableCmd.execute();
                    return callableCmd.getBoolean("p_valido");
                }
                return false;
            }
        });
    }

    protected PreparedStatement comandoCrear(Connection conn,
                                             CuentaUsuario modelo) throws SQLException {
        String sql = "{call insertarCuentaUsuario(?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_userName", modelo.getUserName());
        cmd.setString("p_password", modelo.getPassword());
        if (modelo.getRol() != null) {
            cmd.setString("p_rol", modelo.getRol().name());
        } else {
            throw new SQLException("No se puede crear una cuenta de usuario sin asignar un rol.");
        }
        if (modelo.getFechaBloqueo() != null) {
            cmd.setTimestamp("p_fechaBloqueo", java.sql.Timestamp.valueOf(modelo.getFechaBloqueo()));
        } else {
            cmd.setNull("p_fechaBloqueo", Types.TIMESTAMP);
        }
        cmd.setBoolean("p_activo", modelo.isActivo());
        cmd.registerOutParameter("p_id", Types.INTEGER);
        return cmd;
    }
    protected PreparedStatement comandoActualizar(Connection conn,
                                                  CuentaUsuario modelo) throws SQLException {
        String sql = "{call modificarCuentaUsuario(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_userName", modelo.getUserName());
        cmd.setString("p_password", modelo.getPassword());
        if (modelo.getRol() != null) {
            cmd.setString("p_rol", modelo.getRol().name());
        } else {
            cmd.setNull("p_rol", Types.VARCHAR);
        }
        cmd.setInt("p_intentosFallidos",modelo.getIntentosFallidos());
        if (modelo.getUltimaSesion() != null) {
            cmd.setTimestamp("p_ultimaSesion", java.sql.Timestamp.valueOf(modelo.getUltimaSesion()));
        } else{
            cmd.setNull("p_ultimaSesion", Types.TIMESTAMP);
        }
        if (modelo.getFechaBloqueo() != null) {
            cmd.setTimestamp("p_fechaBloqueo", java.sql.Timestamp.valueOf(modelo.getFechaBloqueo()));
        } else {
            cmd.setNull("p_fechaBloqueo", Types.TIMESTAMP);
        }
        cmd.setBoolean("p_activo", modelo.isActivo());
        cmd.setInt("p_id", modelo.getId());
        return cmd;
    }


    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException {
        String sql = "{call eliminarCuentaUsuario(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException {
        String sql = "{call buscarCuentaUsuarioPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarCuentaUsuarios()}";
        return conn.prepareCall(sql);
    }

    protected PreparedStatement comandoLogin(Connection conn,
                                             String username,
                                             String password) throws SQLException {
        String sql = "{call loginUsuario(?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_username", username);
        cmd.setString("p_password", password);
        cmd.registerOutParameter("p_valido", Types.BOOLEAN);
        return cmd;
    }

    protected CuentaUsuario mapearModelo(ResultSet rs) throws SQLException {
        CuentaUsuario modelo = new CuentaUsuario();
        modelo.setId(rs.getInt("id"));
        modelo.setUserName(rs.getString("userName"));
        modelo.setPassword(rs.getString("password"));
        String rolStr = rs.getString("rol");
        if (rolStr != null) {
            modelo.setRol(Rol.valueOf(rolStr));
        }
        java.sql.Timestamp tsUltimaSesion = rs.getTimestamp("ultimaSesion");
        if (tsUltimaSesion != null) {
            modelo.setUltimaSesion(tsUltimaSesion.toLocalDateTime());
        }
        java.sql.Timestamp tsFechaBloqueo = rs.getTimestamp("fechaBloqueo");
        if (tsFechaBloqueo != null) {
            modelo.setFechaBloqueo(tsFechaBloqueo.toLocalDateTime());
        }
        modelo.setActivo(rs.getBoolean("activo"));
        return modelo;
    }
}
