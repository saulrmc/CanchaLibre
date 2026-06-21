package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.canchalibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl extends PersonaBaseDAO<Cliente> implements ClienteDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Cliente modelo) throws SQLException{
        String sql = "{call insertarCliente(?, ?, ?, ?, ?, ?, ?)}";
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

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarClientes()}";
        return conn.prepareCall(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                    String nombres) throws SQLException{
        String sql = "{call buscarClientePorNombre(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_nombres",nombres);
        return cmd;
    }

    protected Cliente mapearModelo(ResultSet rs) throws SQLException{
        Cliente modelo = new Cliente();
        modelo.setId(rs.getInt("id"));

        Integer idCuentaUsuario = leerIdCuentaUsuario(rs);
        if (idCuentaUsuario != null) {
            modelo.setCuentaUsuario(new CuentaUsuarioDAOImpl().leer(idCuentaUsuario));
        }

        mapearCamposPersona(rs,modelo);
        modelo.setCalificacion(rs.getDouble("calificacion"));
        modelo.setActivo(rs.getBoolean("activo"));
        return modelo;
    }

    protected PreparedStatement comandoBuscarPorCuenta(
            Connection conn, String cuenta)
            throws SQLException {

        String sql = "{call buscarClientePorCuenta(?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_cuenta", cuenta);

        return cmd;
    }

    @Override
    public Cliente buscarPorCuenta(String cuenta) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoBuscarPorCuenta(conn, cuenta)) {
                ResultSet rs = cmd.executeQuery();

                if (!rs.next()) {
                    System.err.println("No se encontro el registro con "
                            + "cuenta: " + cuenta);
                    return null;
                }

                return this.mapearModelo(rs);
            }
        });
    }

}
