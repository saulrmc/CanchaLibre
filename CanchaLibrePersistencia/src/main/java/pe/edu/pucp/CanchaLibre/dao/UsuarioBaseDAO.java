package pe.edu.pucp.CanchaLibre.dao;

import pe.edu.pucp.CanchaLibre.modelo.Usuario.Usuario;

import java.sql.*;

public abstract class UsuarioBaseDAO<M extends Usuario> extends DefaultBaseDAO<M> {
    protected abstract PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                                String nombres) throws SQLException;

    public M buscarPorNombre(String nombres){
        return ejecutarComando(conn -> {
           try (PreparedStatement cmd = comandoBuscarPorNombre(conn,nombres);
                ResultSet rs = cmd.executeQuery()) {
               return rs.next() ? mapearModelo(rs) : null;
           }
        });
    }

    protected int setCamposUsuario(PreparedStatement cmd, int startIndex,
                                   M modelo) throws SQLException{
        cmd.setString(startIndex,modelo.getNombres());
        cmd.setString(startIndex+1,modelo.getContrasena());
        cmd.setString(startIndex+2,modelo.getCorreo());
        cmd.setString(startIndex+3,modelo.getTelefono());
        cmd.setInt(startIndex+4,modelo.getIntentosFallidos());

        if(modelo.getUltimaSesion() != null){
            cmd.setTimestamp(startIndex+5,java.sql.Timestamp.valueOf(modelo.getUltimaSesion()));
        }else{
            cmd.setNull(startIndex+5, Types.TIMESTAMP);
        }

        cmd.setString(startIndex+6,modelo.getRol().name());

        return startIndex + 7;
    }

    protected void mapearCamposUsuario(ResultSet rs, M modelo) throws SQLException{
        modelo.setIdUsuario(rs.getInt("idUsuario"));
        modelo.setNombres(rs.getString("nombres"));
        modelo.setContrasena(rs.getString("contrasena"));
        modelo.setCorreo(rs.getString("correo"));
        modelo.setTelefono(rs.getString("telefono"));
        modelo.setIntentosFallidos(rs.getInt("intentosFallidos"));

        java.sql.Timestamp ts = rs.getTimestamp("ultimaSesion");
        if (ts != null) {
            modelo.setUltimaSesion(ts.toLocalDateTime());
        }
    }

//    protected Integer leerIdUsuario(ResultSet rs) throws SQLException{
//        return leerEnteroNullable(rs,"idUsuario");
//    }
}