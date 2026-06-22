package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

import java.sql.*;

public class ClienteDAOImpl extends PersonaBaseDAO<Cliente> implements ClienteDAO {

    @Override
    protected PreparedStatement comandoCrear(Connection conn, Cliente modelo) throws SQLException {
        String sql = "{call insertarCliente(?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString(1, modelo.getNombres());
        cmd.setString(2, modelo.getCorreo());
        cmd.setString(3, modelo.getTelefono());
        cmd.setString(4, modelo.getCuentaUsuario().getPassword());
        cmd.setString(5, modelo.getCuentaUsuario().getUserName());
        cmd.registerOutParameter(6, Types.INTEGER);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn, Cliente modelo) throws SQLException {
        String sql = "{call modificarCliente(?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setInt(1, modelo.getId());
        cmd.setString(2, modelo.getNombres());
        cmd.setString(3, modelo.getCorreo());
        cmd.setString(4, modelo.getTelefono());
        cmd.setString(5, modelo.getCuentaUsuario().getPassword());

        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "{call eliminarCliente(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setInt(1, id);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = "{call obtenerClientePorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setInt(1, id);

        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarClientes()}";
        return conn.prepareCall(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn, String nombres) throws SQLException {
        String sql = "{call buscarClientePorNombre(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString(1, nombres);

        return cmd;
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

    protected PreparedStatement comandoBuscarPorCuenta(Connection conn, String cuenta) throws SQLException {
        String sql = "{call buscarClientePorCuenta(?)}";
        CallableStatement cmd = conn.prepareCall(sql);

        cmd.setString(1, cuenta);

        return cmd;
    }

    @Override
    public Cliente buscarPorCuenta(String cuenta) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoBuscarPorCuenta(conn, cuenta)) {
                ResultSet rs = cmd.executeQuery();

                if (!rs.next()) {
                    return null;
                }

                return this.mapearModelo(rs);
            }
        });
    }
}