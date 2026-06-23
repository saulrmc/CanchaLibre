package pe.edu.pucp.canchalibre.dao;

import pe.edu.pucp.canchalibre.modelo.Persona;

import java.sql.*;
import java.util.List;

public abstract class PersonaBaseDAO<M extends Persona> extends DefaultBaseDAO<M> {
    protected abstract PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                                String nombre) throws SQLException;

    public List<M> buscarPorNombre(String nombres){
        return ejecutarComando(conn -> {
           try (PreparedStatement cmd = comandoBuscarPorNombre(conn, nombres);
                ResultSet rs = cmd.executeQuery()) {

               List<M> resultados = new java.util.ArrayList<>();
               while (rs.next()) {
                   resultados.add(mapearModelo(rs)); // Va acumulando todas las coincidencias
               }
               return resultados;           }
        });
    }
    protected abstract PreparedStatement comandoBuscarPorCuenta(Connection conn,
                                                                String cuenta) throws SQLException;

    public M buscarPorCuenta(String cuenta) {
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