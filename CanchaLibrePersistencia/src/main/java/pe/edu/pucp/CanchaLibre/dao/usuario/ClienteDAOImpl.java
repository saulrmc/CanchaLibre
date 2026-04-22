package pe.edu.pucp.CanchaLibre.dao.usuario;

import pe.edu.pucp.CanchaLibre.dao.UsuarioBaseDAO;
import pe.edu.pucp.CanchaLibre.dao.reserva.ReservaDAO;
import pe.edu.pucp.CanchaLibre.dao.reserva.ReservaDAOImpl;
import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl extends UsuarioBaseDAO<Cliente> implements ClienteDAO {
    //on BaseDAO
    protected PreparedStatement comandoCrear(Connection conn,
                                             Cliente modelo) throws SQLException{
        String sql = """
                INSERT INTO Cliente(
                    idCliente,
                    nombres,
                    contrasena,
                    correo,
                    telefono,
                    intentosFallidos,
                    ultimaSesion,
                    calificacion
                ) VALUES (?,?,?,?,?,?,?,?)
                """;
        PreparedStatement cmd = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        setCamposCliente(cmd,modelo);
        return cmd;
    }

    protected PreparedStatement comandoActualizar(Connection conn, Cliente modelo) throws SQLException {
        String sql = """
        UPDATE Cliente SET
            nombres = ?,
            contrasena = ?,
            correo = ?,
            telefono = ?,
            intentosFallidos = ?,
            ultimaSesion = ?,
            calificacion = ?
        WHERE idCliente = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        int nextIndex = setCamposUsuario(cmd,1,modelo);
        cmd.setInt(nextIndex, modelo.getCalificacion());
        cmd.setInt(nextIndex+1, modelo.getIdUsuario());

        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeer(Connection conn, Integer id) throws SQLException {
        String sql = """
            SELECT *
            FROM Cliente
            WHERE idCliente = ?
        """;

        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setInt(1, id);

        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = """
        SELECT *
        FROM Cliente
        """;

        return conn.prepareStatement(sql);
    }

    @Override
    protected PreparedStatement comandoBuscarPorNombre(Connection conn,
                                                    String nombres) throws SQLException{
        String sql = """
                SELECT * FROM Cliente WHERE nombres = ?
                """;
        PreparedStatement cmd = conn.prepareStatement(sql);
        cmd.setString(1,nombres);
        return cmd;
    }

    protected Cliente mapearModelo(ResultSet rs) throws SQLException{
        Cliente modelo = new Cliente();
        modelo.setIdUsuario(rs.getInt("idCliente"));
        mapearCamposUsuario(rs,modelo);
        modelo.setCalificacion(rs.getInt("calificacion"));

        ReservaDAO reservaDAO = new ReservaDAOImpl();
        List<Reserva> listaReservas = reservaDAO.leerTodos();

        List<Reserva> listaReservasCliente = new ArrayList<>();
        if(listaReservas !=null) {
            for (Reserva r : listaReservas) {
                if (r.getCliente()!=null &&
                    r.getCliente().getIdUsuario() == modelo.getIdUsuario())
                    listaReservasCliente.add(r);
            }
        }
        modelo.setHistorialReservas(listaReservasCliente);

        return modelo;
    }

    private int setCamposCliente(PreparedStatement cmd, Cliente modelo) throws SQLException {
        int startIndex=1;
        cmd.setInt(startIndex,modelo.getIdUsuario());
        int idx = setCamposUsuario(cmd,startIndex+1,modelo);
        cmd.setInt(idx, modelo.getCalificacion());
        return idx + 1;
    }

}
