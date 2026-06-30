package pe.edu.pucp.canchalibre.dao.cancha;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.cancha.Deporte;
import pe.edu.pucp.canchalibre.modelo.cancha.Etiqueta;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanchaDAOImpl extends DefaultBaseDAO<Cancha> implements CanchaDAO {
    private final BloqueHorarioDAO bloqueDao;
    public CanchaDAOImpl(){this.bloqueDao = new BloqueHorarioDAOImpl();}

    @Override
    public Integer crear(Cancha modelo) {
        return ejecutarComando(conn -> {
            Integer idOrden = this.ejecutarComandoCrear(conn, modelo);
            if (idOrden == null) {
                return null;
            }
            modelo.setId(idOrden);
this.bloqueDao.crearBloquesPorCancha(conn, idOrden, modelo.getBloques());
            if (modelo.getDeportes() != null && !modelo.getDeportes().isEmpty()) {
                this.insertarDeportesCancha(conn, idOrden, modelo.getDeportes());
            }
            if (modelo.getEtiquetas() != null && !modelo.getEtiquetas().isEmpty()) {
                this.insertarEtiquetasCancha(conn, idOrden, modelo.getEtiquetas());
            }
            return idOrden;
        });
    }

    @Override
    public boolean actualizar(Cancha modelo) {
        return ejecutarComando(conn -> {
            if (!this.ejecutarComandoActualizar(conn, modelo)) {
                return false;
            }

            this.bloqueDao.eliminarBloquePorCancha(conn, modelo.getId());
            this.bloqueDao.crearBloquesPorCancha(conn, modelo.getId(), modelo.getBloques());
            this.eliminarDeportesCancha(conn, modelo.getId());
            if (modelo.getDeportes() != null && !modelo.getDeportes().isEmpty()) {
                this.insertarDeportesCancha(conn, modelo.getId(), modelo.getDeportes());
            }

            this.eliminarEtiquetasCancha(conn, modelo.getId());
            if (modelo.getEtiquetas() != null && !modelo.getEtiquetas().isEmpty()) {
                this.insertarEtiquetasCancha(conn, modelo.getId(), modelo.getEtiquetas());
            }
            return true;
        });
    }

    @Override
    public boolean eliminar(Integer id) {
        return ejecutarComando(conn -> {
            this.bloqueDao.eliminarBloquePorCancha(conn, id);
            this.eliminarDeportesCancha(conn, id);
            this.eliminarEtiquetasCancha(conn, id);
            return this.ejecutarComandoEliminar(conn, id);
        });
    }

    @Override
    public Cancha leer(Integer id) {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoLeer(conn, id);
                 ResultSet rs = cmd.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("No se encontro el registro con id: " + id);
                    return null;
                }

                Cancha modelo = this.mapearModelo(rs);
                this.llenarDeportes(conn, modelo);
                this.llenarEtiquetas(conn, modelo);
                modelo.setBloques(this.bloqueDao.leerBloquesPorCancha(conn, modelo.getId()));
                return modelo;
            }
        });
    }

    @Override
    public List<Cancha> leerTodos() {
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd = this.comandoLeerTodos(conn);
                 ResultSet rs = cmd.executeQuery()) {
                List<Cancha> modelos = new ArrayList<>();
                while (rs.next()) {
                    modelos.add(this.mapearModelo(rs));
                }

                if (modelos.isEmpty()) {
                    return modelos;
                }

                Map<Integer, List<Deporte>> deportesCancha = new HashMap<>();
                try (PreparedStatement ps = conn.prepareCall("{call listarDeportesTodasCanchas()}");
                     ResultSet rsDeportes = ps.executeQuery()) {
                    while (rsDeportes.next()) {
                        int idCancha = rsDeportes.getInt("idCancha");
                        String nombreEnum = rsDeportes.getString("deporte");
                        deportesCancha.computeIfAbsent(idCancha, k -> new ArrayList<>())
                            .add(Deporte.valueOf(nombreEnum));
                    }
                }

                Map<Integer, List<Etiqueta>> etiquetasCancha = new HashMap<>();
                try (PreparedStatement ps = conn.prepareCall("{call listarEtiquetasTodasCanchas()}");
                     ResultSet rsEtiquetas = ps.executeQuery()) {
                    while (rsEtiquetas.next()) {
                        int idCancha = rsEtiquetas.getInt("idCancha");
                        String nombreEnum = rsEtiquetas.getString("etiqueta");
                        etiquetasCancha.computeIfAbsent(idCancha, k -> new ArrayList<>())
                            .add(Etiqueta.valueOf(nombreEnum));
                    }
                }

                Map<Integer, List<BloqueHorario>> bloquesCancha = this.bloqueDao.leerBloquesTodasCanchas(conn);

                for (Cancha cancha : modelos) {
                    int id = cancha.getId();
                    cancha.setDeportes(deportesCancha.getOrDefault(id, new ArrayList<>()));
                    cancha.setEtiquetas(etiquetasCancha.getOrDefault(id, new ArrayList<>()));
                    cancha.setBloques(bloquesCancha.getOrDefault(id, new ArrayList<>()));
                }

                return modelos;
            }
        });
    }

    protected PreparedStatement comandoCrear(Connection conn,
                                             Cancha modelo) throws SQLException{

        String sql = "{call insertarCancha(?, ?, ?, ?, ?, ?, ?, ?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_nombre",modelo.getNombre());
        cmd.setString("p_descripcion",modelo.getDescripcion());
        cmd.setString("p_direccion",modelo.getDireccion());
        if (modelo.getImagenUrl() != null) {
            cmd.setString("p_imagenUrl", modelo.getImagenUrl());
        } else {
            cmd.setNull("p_imagenUrl", java.sql.Types.LONGVARCHAR);
        }
        cmd.setInt("p_idPropietario", modelo.getPropietario().getId());
        cmd.setDouble("p_precioBase",modelo.getPrecioBase());
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.registerOutParameter("p_id", Types.INTEGER);
        return cmd;
    }


    protected PreparedStatement comandoActualizar(Connection conn,
                                                  Cancha modelo) throws SQLException{
        String sql = "{call modificarCancha(?, ?, ?, ?, ?, ?, ?, ?)}";

        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_nombre",modelo.getNombre());
        cmd.setString("p_descripcion",modelo.getDescripcion());
        cmd.setString("p_direccion",modelo.getDireccion());
        if (modelo.getImagenUrl() != null) {
            cmd.setString("p_imagenUrl", modelo.getImagenUrl());
        } else {
            cmd.setNull("p_imagenUrl", java.sql.Types.LONGVARCHAR);
        }
        cmd.setInt("p_idPropietario", modelo.getPropietario().getId());
        cmd.setDouble("p_precioBase",modelo.getPrecioBase());
        cmd.setBoolean("p_activo",modelo.isActivo());
        cmd.setInt("p_id", modelo.getId());
        return cmd;
    }

    protected PreparedStatement comandoEliminar(Connection conn,
                                                Integer id) throws SQLException{
        String sql = "{call eliminarCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }


    protected PreparedStatement comandoLeer(Connection conn,
                                            Integer id) throws SQLException{
        String sql = "{call buscarCanchaPorId(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id",id);
        return cmd;
    }

    protected PreparedStatement comandoLeerTodos(Connection conn) throws SQLException{
        String sql = "{call listarCanchas()}";
        return conn.prepareCall(sql);
    }

    // DEPORTES ----------------------------------------------------------------------------------------------

    protected PreparedStatement comandoListarDeportesCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "{call listarDeportesCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idCancha);
        return cmd;
    }

    private void llenarDeportes(Connection conn, Cancha cancha) {
        try (PreparedStatement ps = comandoListarDeportesCancha(conn, cancha.getId());
             ResultSet rs = ps.executeQuery()) {

            if (cancha.getDeportes() == null) {
                cancha.setDeportes(new ArrayList<>());
            }

            while (rs.next()) {
                String nombreEnum = rs.getString("deporte");
                cancha.getDeportes().add(Deporte.valueOf(nombreEnum));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar deportes: " + e.getMessage());
        }
    }

    protected PreparedStatement comandoEliminarDeportesCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "{call eliminarDeportesCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha", idCancha);
        return cmd;
    }

    private void eliminarDeportesCancha(Connection conn, Integer idCancha) throws SQLException {
        try (PreparedStatement cmd = this.comandoEliminarDeportesCancha(conn, idCancha)) {
            cmd.executeUpdate();
        }
    }

    protected PreparedStatement comandoInsertarDeportesCancha(Connection conn, int idCancha, Deporte deporte) throws SQLException {
        String sql = "{call insertarDeportesCancha(?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha", idCancha);
        cmd.setString("p_deporte", deporte.name());
        return cmd;
    }

    private void insertarDeportesCancha(Connection conn, Integer idCancha, List<Deporte> deportes) throws SQLException {
        for (Deporte deporte : deportes) {
            try (PreparedStatement cmd = this.comandoInsertarDeportesCancha(conn, idCancha, deporte)) {
                cmd.executeUpdate();
            }
        }
    }

    // ETIQUETAS ----------------------------------------------------------------------------------------------

    protected PreparedStatement comandoListarEtiquetasCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "{call listarEtiquetasCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idCancha);
        return cmd;
    }

    private void llenarEtiquetas(Connection conn, Cancha cancha) {
        try (PreparedStatement ps = comandoListarEtiquetasCancha(conn, cancha.getId());
             ResultSet rs = ps.executeQuery()) {

            if (cancha.getEtiquetas() == null) {
                cancha.setEtiquetas(new ArrayList<>());
            }

            while (rs.next()) {
                String nombreEnum = rs.getString("etiqueta");
                cancha.getEtiquetas().add(Etiqueta.valueOf(nombreEnum));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar etiquetas: " + e.getMessage());
        }
    }

    protected PreparedStatement comandoEliminarEtiquetasCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "{call eliminarEtiquetasCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha", idCancha);
        return cmd;
    }

    private void eliminarEtiquetasCancha(Connection conn, Integer idCancha) throws SQLException {
        try (PreparedStatement cmd = this.comandoEliminarEtiquetasCancha(conn, idCancha)) {
            cmd.executeUpdate();
        }
    }

    protected PreparedStatement comandoInsertarEtiquetasCancha(Connection conn, int idCancha, Etiqueta etiqueta) throws SQLException {
        String sql = "{call insertarEtiquetasCancha(?, ?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_idCancha", idCancha);
        cmd.setString("p_etiqueta", etiqueta.name());
        return cmd;
    }

    private void insertarEtiquetasCancha(Connection conn, Integer idCancha, List<Etiqueta> etiquetas) throws SQLException {
        for (Etiqueta etiqueta : etiquetas) {
            try (PreparedStatement cmd = this.comandoInsertarEtiquetasCancha(conn, idCancha, etiqueta)) {
                cmd.executeUpdate();
            }
        }
    }

    @Override
    protected Cancha mapearModelo(ResultSet rs) throws SQLException{
        Cancha cancha = new Cancha();
        cancha.setId(rs.getInt("id"));
        cancha.setNombre(rs.getString("nombre"));
        cancha.setDescripcion(rs.getString("descripcion"));
        cancha.setDireccion(rs.getString("direccion"));
        cancha.setImagenUrl(rs.getString("imagenUrl"));
        cancha.setPropietario(leerPropietario(rs));
        cancha.setPrecioBase(rs.getDouble("precioBase"));
        cancha.setPromedioCalificacion(rs.getDouble("promedioCalificacion"));
        cancha.setActivo(rs.getBoolean("activo"));
        return cancha;
    }

    private Propietario leerPropietario(ResultSet rs) throws SQLException {
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
            } catch (SQLException e) {
                // cuenta_userName column not present, skip
            }

            return p;
        } catch (SQLException e) {
            // JOIN columns not present in this result set, fall back to individual load
            return new PropietarioDAOImpl().leer(rs.getInt("idPropietario"));
        }
    }

    protected PreparedStatement comandoListarCanchasPorCuenta(Connection conn,
                                                              String cuenta) throws SQLException{
        String sql = "{call listarCanchasPorCuenta(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_cuenta", cuenta);
        return cmd;
    }

    @Override
    public List<Cancha> listarCanchasPorCuenta(String cuenta){
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd =
                         this.comandoListarCanchasPorCuenta(conn, cuenta)) {
                ResultSet rs = cmd.executeQuery();

                List<Cancha> modelos = new ArrayList<>();
                while (rs.next()) {
                    modelos.add(this.mapearModelo(rs));
                }

                if (modelos.isEmpty()) {
                    return modelos;
                }

                Map<Integer, List<Deporte>> deportesCancha = new HashMap<>();
                try (PreparedStatement ps = conn.prepareCall("{call listarDeportesTodasCanchas()}");
                     ResultSet rsDeportes = ps.executeQuery()) {
                    while (rsDeportes.next()) {
                        int idCancha = rsDeportes.getInt("idCancha");
                        String nombreEnum = rsDeportes.getString("deporte");
                        deportesCancha.computeIfAbsent(idCancha, k -> new ArrayList<>())
                            .add(Deporte.valueOf(nombreEnum));
                    }
                }

                Map<Integer, List<Etiqueta>> etiquetasCancha = new HashMap<>();
                try (PreparedStatement ps = conn.prepareCall("{call listarEtiquetasTodasCanchas()}");
                     ResultSet rsEtiquetas = ps.executeQuery()) {
                    while (rsEtiquetas.next()) {
                        int idCancha = rsEtiquetas.getInt("idCancha");
                        String nombreEnum = rsEtiquetas.getString("etiqueta");
                        etiquetasCancha.computeIfAbsent(idCancha, k -> new ArrayList<>())
                            .add(Etiqueta.valueOf(nombreEnum));
                    }
                }

                for (Cancha cancha : modelos) {
                    int id = cancha.getId();
                    cancha.setDeportes(deportesCancha.getOrDefault(id, new ArrayList<>()));
                    cancha.setEtiquetas(etiquetasCancha.getOrDefault(id, new ArrayList<>()));
                }

                return modelos;
            }
        });
    }

    protected PreparedStatement comandoListarCanchasPorDistrito(Connection conn,
                                                              String cuenta) throws SQLException{
        String sql = "{call listarCanchasPorDistrito(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setString("p_distrito", cuenta);
        return cmd;
    }

    @Override
    public List<Cancha> listarCanchasPorDistrito(String distritoOficial){
        return ejecutarComando(conn -> {
            try (PreparedStatement cmd =
                         this.comandoListarCanchasPorDistrito(conn, distritoOficial)) {
                ResultSet rs = cmd.executeQuery();

                List<Cancha> modelos = new ArrayList<>();
                while (rs.next()) {
                    Cancha cancha = this.mapearModelo(rs);
                    this.llenarDeportes(conn, cancha);
                    this.llenarEtiquetas(conn, cancha);
                    modelos.add(cancha);
                }

                return modelos;
            }
        });
    }
}
