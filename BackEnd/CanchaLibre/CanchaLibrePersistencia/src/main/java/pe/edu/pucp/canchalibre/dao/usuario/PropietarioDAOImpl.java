package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAO;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.sql.*;

import java.util.*;

public class PropietarioDAOImpl extends PersonaBaseDAO<Propietario> implements PropietarioDAO {
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
        cmd.setDouble("p_calificacion",modelo.getCalificacion());
        cmd.setString("p_telefonoOperaciones",modelo.getTelefonoOperaciones());
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Propietario modelo) throws SQLException {
        String sql = "{call modificarPropietario(?, ?, ?, ?, ?, ?, ?)}";
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
        cmd.setString("p_telefonoOperaciones",modelo.getTelefonoOperaciones());
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

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                       String nombres) throws SQLException{
        String sql = "{call buscarPropietarioPorNombre(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_nombres",nombres);
        return cmd;
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
        modelo.setTelefonoOperaciones(rs.getString("telefonoOperaciones"));
        modelo.setActivo(rs.getBoolean("activo"));
        return modelo;
    }

    protected PreparedStatement comandoBuscarPorCuenta(
            Connection conn, String cuenta)
            throws SQLException {

        String sql = "{call buscarPropietarioPorCuenta(?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_cuenta", cuenta);

        return cmd;
    }

    @Override
    public Propietario buscarPorCuenta(String cuenta) {
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

