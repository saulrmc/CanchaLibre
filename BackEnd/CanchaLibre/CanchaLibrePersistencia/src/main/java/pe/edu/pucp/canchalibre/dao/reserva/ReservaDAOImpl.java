package pe.edu.pucp.canchalibre.dao.reserva;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.dao.cancha.BloqueHorarioDAO;
import pe.edu.pucp.canchalibre.dao.cancha.BloqueHorarioDAOImpl;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.dao.transaccion.PagoDAOImpl;
import pe.edu.pucp.canchalibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReservaDAOImpl extends DefaultBaseDAO<Reserva> implements ReservaDAO {
    private final BloqueHorarioDAO bloqueDao;

    public ReservaDAOImpl(){
        this.bloqueDao = new BloqueHorarioDAOImpl();
    }

    @Override
    public Integer crear(Reserva modelo) {
        return ejecutarComando(conn -> {
            Integer idOrden = this.ejecutarComandoCrear(conn, modelo);
            if (idOrden == null) {
                return null;
            }
            modelo.setIdReserva(idOrden);
            this.bloqueDao.crearBloquesPorReserva(conn, idOrden, modelo.getBloquesSeleccionados());
            return idOrden;
        });
    }

    @Override
    public boolean actualizar(Reserva modelo) {
        return ejecutarComando(conn -> {
            if (!this.ejecutarComandoActualizar(conn, modelo)) {
                return false;
            }
            // Si el cliente cambia de bloques
            // se debe cancelar la reserva y crear una nueva
            return true;
        });
    }

    @Override
    public boolean eliminar(Integer id) {
        return ejecutarComando(conn -> {
            // Se quedan intactos para saber
            // qué horarios estuvieron ocupados o reservados.
            return this.ejecutarComandoEliminar(conn, id);
        });
    }

    @Override
    public Reserva leer(Integer id) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoLeer(conn, id);
                 ResultSet rs = cmd.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("No se encontro el registro con id: " + id);
                    return null;
                }

                Reserva modelo = this.mapearModelo(rs);
                modelo.setBloquesSeleccionados(this.bloqueDao.leerBloquesPorReserva(conn, modelo.getIdReserva()));
                return modelo;
            }
        });
    }

    @Override
    public List<Reserva> leerTodos() {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoLeerTodos(conn);
                 ResultSet rs = cmd.executeQuery()) {
                List<Reserva> modelos = new ArrayList<>();
                while (rs.next()) {
                    Reserva modelo = this.mapearModelo(rs);
                    modelo.setBloquesSeleccionados(this.bloqueDao.leerBloquesPorReserva(conn, modelo.getIdReserva()));
                    modelos.add(modelo);
                }
                return modelos;
            }
        });
    }


    @Override
    protected PreparedStatement comandoCrear(Connection conn, Reserva modelo) throws SQLException {
        String sql = "{call insertarReserva(?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_estado",modelo.getEstado().name());
        cmd.setInt("p_idCliente",modelo.getCliente().getId());
        cmd.setInt("p_idCancha",modelo.getCancha().getId());
        if(modelo.getPago()==null){
            cmd.setNull("p_idPago",Types.INTEGER);
        }
        else {
            cmd.setInt("p_idPago",modelo.getPago().getIdPago());
        }
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn, Reserva modelo) throws SQLException {
        String sql = "{call modificarReserva(?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_estado",modelo.getEstado().name());
        cmd.setInt("p_idCliente",modelo.getCliente().getId());
        cmd.setInt("p_idCancha",modelo.getCancha().getId());
        if(modelo.getPago()==null){
            cmd.setNull("p_idPago",Types.INTEGER);
        }
        else {
            cmd.setInt("p_idPago",modelo.getPago().getIdPago());
        }
        cmd.setInt("p_id",modelo.getIdReserva());
        return cmd;
    }

    @Override
    protected PreparedStatement comandoEliminar(Connection conn, Integer idPago) throws SQLException {
        String sql = "{call eliminarReserva(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idPago);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeer(Connection conn, Integer idPago) throws SQLException{
        String sql = "{call buscarReservaPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idPago);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException {
        String sql = "{call listarReservas()}";
        return conn.prepareCall(sql);
    }

    @Override
    protected Reserva mapearModelo(ResultSet rs) throws SQLException {
        Reserva modelo = new Reserva();
        modelo.setIdReserva(rs.getInt("idReserva"));
        modelo.setEstado(EstadoReserva.valueOf(rs.getString("estado")));
        modelo.setCliente(new ClienteDAOImpl().leer(rs.getInt("idCliente")));
        modelo.setCancha(new CanchaDAOImpl().leer(rs.getInt("idCancha")));

        int idPago = rs.getInt("idPago");
        if(!rs.wasNull()){
            modelo.setPago(new PagoDAOImpl().leer(idPago));
        }

        return modelo;
    }

    protected PreparedStatement comandoListarReservasPorCuenta(
            Connection conn, String cuenta) throws SQLException {
        String sql = "{call listarReservasPorCuenta(?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_cuenta", cuenta);
        return cmd;
    }

    @Override
    public List<Reserva> listarReservasPorCuenta(String cuenta) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd =
                         this.comandoListarReservasPorCuenta(conn, cuenta)) {
                ResultSet rs = cmd.executeQuery();

                List<Reserva> modelos = new ArrayList<>();
                while (rs.next()) {
                    modelos.add(this.mapearModelo(rs));
                }

                return modelos;
            }
        });
    }

}
