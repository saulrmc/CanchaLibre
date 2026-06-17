package pe.edu.pucp.CanchaLibre.dao;

import pe.edu.pucp.canchalibre.modelo.Persona;

import java.sql.*;

public abstract class PersonaBaseDAO<M extends Persona> extends DefaultBaseDAO<M> {
    protected abstract PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                                String nombres) throws SQLException;

    public M buscarPorNombre(String nombre){
        return ejecutarComando(conn -> {
           try (PreparedStatement cmd = comandoBuscarPorNombre(conn, nombre);
                ResultSet rs = cmd.executeQuery()) {
               return rs.next() ? mapearModelo(rs) : null;
           }
        });
    }

    protected int setCamposPersona(PreparedStatement cmd, int startIndex,
                                   M modelo) throws SQLException{
        //cmd.setInt(startIndex,modelo.getIdUsuario());
        cmd.setString(startIndex,modelo.getNombre());
        cmd.setString(startIndex+1,modelo.getApellidoPaterno());
        cmd.setString(startIndex+2,modelo.getCorreo());
        cmd.setString(startIndex+3,modelo.getTelefono());
        //cmd.setInt(startIndex+4,modelo.getIntentosFallidos());
//
//        if(modelo.getUltimaSesion() != null){
//            cmd.setTimestamp(startIndex+5,java.sql.Timestamp.valueOf(modelo.getUltimaSesion()));
//        }else{
//            cmd.setNull(startIndex+5, Types.TIMESTAMP);
//        }
        //cmd.setString(startIndex+7,modelo.getRol().name());

        return startIndex+4;
    }

    protected void mapearCamposPersona(ResultSet rs, M modelo) throws SQLException{
        //modelo.setIdUsuario(rs.getInt("idUsuario"));
        modelo.setNombre(rs.getString("nombre"));
        modelo.setApellidoPaterno(rs.getString("apellidoPaterno"));
        //modelo.setContrasena(rs.getString("contrasena"));
        modelo.setCorreo(rs.getString("correo"));
        modelo.setTelefono(rs.getString("telefono"));
        //modelo.setIntentosFallidos(rs.getInt("intentosFallidos"));
//
//        java.sql.Timestamp ts = rs.getTimestamp("ultimaSesion");
//        if (ts != null) {
//            modelo.setUltimaSesion(ts.toLocalDateTime());
//        }
    }

    protected Integer getIdCuentaUsuario(M modelo) {
        if (modelo.getCuentaUsuario() == null) {
            return null;
        }
        return modelo.getCuentaUsuario().getId();
    }

    protected void setCuentaUsuarioNullable(PreparedStatement cmd, int index,
                                            M modelo) throws SQLException {
        setEnteroNullable(cmd, index, getIdCuentaUsuario(modelo));
    }

    protected Integer leerIdCuentaUsuario(ResultSet rs) throws SQLException {
        return leerEnteroNullable(rs, "idCuentaUsuario");
    }
}