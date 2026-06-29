package pe.edu.pucp.canchalibre.dao.reserva;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.dao.cancha.BloqueHorarioDAO;
import pe.edu.pucp.canchalibre.dao.cancha.BloqueHorarioDAOImpl;
import pe.edu.pucp.canchalibre.dao.cancha.CanchaDAOImpl;
import pe.edu.pucp.canchalibre.dao.transaccion.PagoDAOImpl;
import pe.edu.pucp.canchalibre.dao.usuario.ClienteDAOImpl;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.reserva.EstadoReserva;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;
import pe.edu.pucp.canchalibre.modelo.transaccion.MetodoPago;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
            modelo.setId(idOrden);
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
                modelo.setBloquesSeleccionados(this.bloqueDao.leerBloquesPorReserva(conn, modelo.getId()));
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
                    modelo.setBloquesSeleccionados(this.bloqueDao.leerBloquesPorReserva(conn, modelo.getId()));
                    modelos.add(modelo);
                }
                return modelos;
            }
        });
    }


    @Override
    protected PreparedStatement comandoCrear(Connection conn, Reserva modelo) throws SQLException {
        String sql = "{call insertarReserva(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_estado",modelo.getEstado().name());
        cmd.setInt("p_idCliente",modelo.getCliente().getId());
        cmd.setInt("p_idCancha",modelo.getCancha().getId());
        cmd.setObject("p_fechaCreacion",modelo.getFechaCreacion());
        if(modelo.getPago()==null){
            cmd.setNull("p_idPago",Types.INTEGER);
        }
        else {
            cmd.setInt("p_idPago",modelo.getPago().getIdPago());
        }
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id",Types.INTEGER);
        return cmd;
    }

    @Override
    protected PreparedStatement comandoActualizar(Connection conn, Reserva modelo) throws SQLException {
        String sql = "{call modificarReserva(?, ?, ?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_estado",modelo.getEstado().name());
        if(modelo.getPago()==null){
            cmd.setNull("p_idPago",Types.INTEGER);
        }
        else {
            cmd.setInt("p_idPago",modelo.getPago().getIdPago());
        }
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.setInt("p_id",modelo.getId());
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
        modelo.setId(rs.getInt("id"));
        modelo.setEstado(EstadoReserva.valueOf(rs.getString("estado")));
        modelo.setCliente(new ClienteDAOImpl().leer(rs.getInt("idCliente")));
        modelo.setCancha(new CanchaDAOImpl().leer(rs.getInt("idCancha")));

        int idPago = rs.getInt("idPago");
        if(!rs.wasNull()){
            modelo.setPago(new PagoDAOImpl().leer(idPago));
        }
        modelo.setActivo(rs.getBoolean("activo"));
        modelo.setFechaCreacion(rs.getObject("fechaCreacion", LocalDateTime.class));
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
                    Reserva modelo = this.mapearModelo(rs);
                    modelo.setBloquesSeleccionados(this.bloqueDao.leerBloquesPorReserva(conn, modelo.getId()));
                    modelos.add(modelo);
                }

                return modelos;
            }
        });
    }

    protected PreparedStatement comandoListarReservasPorId(
            Connection conn, int idCliente) throws SQLException {
        String sql = "{call listarReservasPorId(?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCliente", idCliente);
        return cmd;
    }

    @Override
    public List<Reserva> listarReservasPorId(int idCliente) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = comandoListarReservasPorId(conn, idCliente);
                 ResultSet rs = cmd.executeQuery()) {

                List<Reserva> modelos = new ArrayList<>();
                List<Integer> clienteIds = new ArrayList<>();
                List<Integer> canchaIds = new ArrayList<>();
                List<Integer> pagoIds = new ArrayList<>();

                while (rs.next()) {
                    Reserva r = new Reserva();
                    r.setId(rs.getInt("id"));
                    r.setEstado(EstadoReserva.valueOf(rs.getString("estado")));
                    r.setActivo(rs.getBoolean("activo"));
                    r.setFechaCreacion(rs.getObject("fechaCreacion", LocalDateTime.class));
                    modelos.add(r);
                    clienteIds.add(rs.getInt("idCliente"));
                    canchaIds.add(rs.getInt("idCancha"));
                    int idP = rs.getInt("idPago");
                    pagoIds.add(rs.wasNull() ? null : idP);
                }

                if (modelos.isEmpty()) return modelos;

                Map<Integer, Cliente> clientesMap = cargarClientesBatch(conn, clienteIds);
                Map<Integer, Cancha> canchasMap = cargarCanchasBatch(conn);
                Map<Integer, Pago> pagosMap = cargarPagosBatch(conn);
                Map<Integer, List<BloqueHorario>> bloquesPorReserva = this.bloqueDao.leerBloquesTodasReservas(conn);

                for (int i = 0; i < modelos.size(); i++) {
                    Reserva r = modelos.get(i);
                    r.setCliente(clientesMap.get(clienteIds.get(i)));
                    r.setCancha(canchasMap.get(canchaIds.get(i)));
                    Integer idP = pagoIds.get(i);
                    if (idP != null) {
                        r.setPago(pagosMap.get(idP));
                    }
                    r.setBloquesSeleccionados(bloquesPorReserva.getOrDefault(r.getId(), new ArrayList<>()));
                }

                return modelos;
            }
        });
    }

    private Map<Integer, Cliente> cargarClientesBatch(Connection conn, List<Integer> clienteIds) throws SQLException {
        Set<Integer> ids = new HashSet<>(clienteIds);
        Map<Integer, Cliente> map = new HashMap<>();
        try (PreparedStatement ps = conn.prepareCall("{call listarClientes()}");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                if (!ids.contains(id)) continue;
                Cliente c = new Cliente();
                c.setId(id);
                c.setActivo(rs.getBoolean("activo"));
                c.setNombres(rs.getString("nombres"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                c.setCalificacion(rs.getDouble("calificacion"));
                int idCuenta = rs.getInt("idCuentaUsuario");
                if (!rs.wasNull()) {
                    CuentaUsuario cu = new CuentaUsuario();
                    cu.setId(idCuenta);
                    cu.setUserName(rs.getString("userName"));
                    cu.setPassword(rs.getString("password"));
                    cu.setRol(Rol.valueOf(rs.getString("rol")));
                    cu.setIntentosFallidos(rs.getInt("intentosFallidos"));
                    Timestamp ts = rs.getTimestamp("ultimaSesion");
                    if (ts != null) cu.setUltimaSesion(ts.toLocalDateTime());
                    ts = rs.getTimestamp("fechaBloqueo");
                    if (ts != null) cu.setFechaBloqueo(ts.toLocalDateTime());
                    c.setCuentaUsuario(cu);
                }
                map.put(id, c);
            }
        }
        return map;
    }

    private Map<Integer, Cancha> cargarCanchasBatch(Connection conn) throws SQLException {
        Map<Integer, Cancha> map = new HashMap<>();
        try (PreparedStatement ps = conn.prepareCall("{call listarCanchas()}");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cancha c = new Cancha();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setDireccion(rs.getString("direccion"));
                c.setImagenUrl(rs.getString("imagenUrl"));
                c.setPrecioBase(rs.getDouble("precioBase"));
                c.setPromedioCalificacion(rs.getDouble("promedioCalificacion"));
                c.setActivo(rs.getBoolean("activo"));

                try {
                    Propietario p = new Propietario();
                    p.setId(rs.getInt("prop_id"));
                    p.setNombres(rs.getString("prop_nombres"));
                    p.setCorreo(rs.getString("prop_correo"));
                    p.setTelefono(rs.getString("prop_telefono"));
                    p.setCalificacion(rs.getDouble("prop_calificacion"));
                    p.setRUC(rs.getString("prop_ruc"));
                    p.setSaldo(rs.getDouble("prop_saldo"));
                    p.setActivo(rs.getBoolean("prop_activo"));
                    try {
                        CuentaUsuario cu = new CuentaUsuario();
                        cu.setId(rs.getInt("cuenta_id"));
                        cu.setUserName(rs.getString("cuenta_userName"));
                        p.setCuentaUsuario(cu);
                    } catch (SQLException ignored) {}
                    c.setPropietario(p);
                } catch (SQLException ignored) {}

                map.put(c.getId(), c);
            }
        }
        return map;
    }

    private Map<Integer, Pago> cargarPagosBatch(Connection conn) throws SQLException {
        Map<Integer, Pago> map = new HashMap<>();
        try (PreparedStatement ps = conn.prepareCall("{call listarPagos()}");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pago p = new Pago();
                p.setIdPago(rs.getInt("idPago"));
                p.setMetodoPago(MetodoPago.valueOf(rs.getString("metodoPago")));
                p.setMonto(rs.getDouble("monto"));
                p.setFechaPago(rs.getObject("fechaPago", LocalDateTime.class));
                try {
                    int idComp = rs.getInt("comp_id");
                    if (!rs.wasNull()) {
                        Comprobante comp = new Comprobante();
                        comp.setIdComprobante(idComp);
                        comp.setSerie(rs.getString("comp_serie"));
                        comp.setNumero(rs.getString("comp_numero"));
                        comp.setFechaEmision(rs.getObject("comp_fechaEmision", LocalDateTime.class));
                        comp.setMontoBloques(rs.getDouble("comp_montoBloques"));
                        comp.setValorVenta(rs.getDouble("comp_valorVenta"));
                        comp.setMontoIgv(rs.getDouble("comp_montoIgv"));
                        p.setComprobante(comp);
                    }
                } catch (SQLException ignored) {}
                map.put(p.getIdPago(), p);
            }
        }
        return map;
    }

}
