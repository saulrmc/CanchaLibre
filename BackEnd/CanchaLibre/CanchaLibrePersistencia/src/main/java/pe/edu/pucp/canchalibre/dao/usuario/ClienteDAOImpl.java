package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

import java.sql.*;

public class ClienteDAOImpl extends PersonaBaseDAO<Cliente> implements ClienteDAO {
//    @Override
//    public Integer crear(Cliente modelo) {
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
                                             Cliente modelo) throws SQLException{
        String sql = "{call insertarCliente(?, ?, ?, ?, ?, ?)}";
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
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Cliente modelo) throws SQLException {
        String sql = "{call modificarCliente(?, ?, ?, ?, ?, ?, ?)}";
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
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.setInt("p_id",modelo.getId());
        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "{call eliminarCliente(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = "{call buscarClientePorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }
    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarClientes()}";
        return conn.prepareCall(sql);
    }

    @Override
    protected Cliente mapearModelo(ResultSet rs) throws SQLException {
        Cliente modelo = new Cliente();

        modelo.setId(rs.getInt("id"));
        modelo.setActivo(rs.getBoolean("activo"));
        modelo.setNombres(rs.getString("nombres"));
        modelo.setCorreo(rs.getString("correo"));
        modelo.setTelefono(rs.getString("telefono"));
        modelo.setCalificacion(rs.getDouble("calificacion"));

        int idCuentaUsuario = rs.getInt("idCuentaUsuario");

        if (!rs.wasNull()) {
            CuentaUsuario cuenta = new CuentaUsuario();
            cuenta.setId(idCuentaUsuario);
            cuenta.setUserName(rs.getString("userName"));
            cuenta.setPassword(rs.getString("password"));
            cuenta.setRol(Rol.valueOf(rs.getString("rol")));
            cuenta.setIntentosFallidos(rs.getInt("intentosFallidos"));

            Timestamp ultimaSesion = rs.getTimestamp("ultimaSesion");
            if (ultimaSesion != null) {
                cuenta.setUltimaSesion(ultimaSesion.toLocalDateTime());
            }

            Timestamp fechaBloqueo = rs.getTimestamp("fechaBloqueo");
            if (fechaBloqueo != null) {
                cuenta.setFechaBloqueo(fechaBloqueo.toLocalDateTime());
            }

            modelo.setCuentaUsuario(cuenta);
        }

        return modelo;
    }


    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn, String nombres) throws SQLException {
        String sql = "{call buscarClientePorNombre(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_nombres", nombres);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoBuscarPorCuenta(Connection conn, String cuenta) throws SQLException {
        String sql = "{call buscarClientePorCuenta(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString("p_userName", cuenta);

        return cmd;
    }

}