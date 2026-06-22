package pe.edu.pucp.canchalibre.dao;

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
        cmd.setString(startIndex,modelo.getNombres());
        cmd.setString(startIndex+1,modelo.getCorreo());
        cmd.setString(startIndex+2,modelo.getTelefono());
        return startIndex+3;
    }

    protected void mapearCamposPersona(ResultSet rs, M modelo) throws SQLException{
        modelo.setNombres(rs.getString("nombres"));
        modelo.setCorreo(rs.getString("correo"));
        modelo.setTelefono(rs.getString("telefono"));
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