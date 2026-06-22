package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaBaseDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.usuario.Administrador;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.sql.*;

public class AdministradorDAOImpl extends PersonaBaseDAO<Administrador> implements AdministradorDAO {
    protected PreparedStatement comandoCrear(Connection conn,
                                             Administrador modelo) throws SQLException{
        String sql = "{call insertarAdministrador(?, ?, ?, ?, ?, ?)}";
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

    protected PreparedStatement comandoActualizar(Connection conn, Administrador modelo) throws SQLException {
        String sql = "{call modificarAdministrador(?, ?, ?, ?, ?, ?)}";
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
        cmd.setInt("p_id",modelo.getId());
        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "{call eliminarAdministrador(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = "{call buscarAdministradorPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarAdministradores()}";
        return conn.prepareCall(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                       String nombres) throws SQLException{
        String sql = "{call buscarAdministradorPorNombre(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_nombres",nombres);
        return cmd;
    }

    protected Administrador mapearModelo(ResultSet rs) throws SQLException{
        Administrador modelo = new Administrador();
        modelo.setId(rs.getInt("id"));

        Integer idCuentaUsuario = leerIdCuentaUsuario(rs);
        if (idCuentaUsuario != null) {
            modelo.setCuentaUsuario(new CuentaUsuarioDAOImpl().leer(idCuentaUsuario));
        }

        mapearCamposPersona(rs,modelo);
        modelo.setActivo(rs.getBoolean("activo"));
        return modelo;
    }

}
