package pe.edu.pucp.canchalibre.dao.cancha;

import pe.edu.pucp.canchalibre.dao.DefaultBaseDAO;
import pe.edu.pucp.canchalibre.dao.usuario.PropietarioDAOImpl;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.cancha.Deporte;
import pe.edu.pucp.canchalibre.modelo.cancha.Etiqueta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            return true;
        });
    }

    @Override
    public boolean eliminar(Integer id) {
        return ejecutarComando(conn -> {
            this.bloqueDao.eliminarBloquePorCancha(conn, id);
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
                    Cancha modelo = this.mapearModelo(rs);
                    modelo.setBloques(this.bloqueDao.leerBloquesPorCancha(conn, modelo.getId()));
                    modelos.add(modelo);
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
        if(modelo.getImagenUrl()!=null){
            cmd.setString("p_imagenUrl",modelo.getImagenUrl());
        }else{
            cmd.setNull("p_imagenUrl",Types.VARCHAR);
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
        if(modelo.getImagenUrl()!=null){
            cmd.setString("p_imagenUrl",modelo.getImagenUrl());
        }else{
            cmd.setNull("p_imagenUrl",Types.VARCHAR);
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


    protected PreparedStatement comandoListarDeportesPorCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "{call listarDeportesCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idCancha);
        return cmd;
    }

    protected PreparedStatement comandoListarEtiquetasPorCancha(Connection conn, int idCancha) throws SQLException {
        String sql = "{call listarEtiquetasCancha(?)}";
        CallableStatement cmd = conn.prepareCall(sql);
        cmd.setInt("p_id", idCancha);
        return cmd;
    }

    private void llenarDeportes(Connection conn, Cancha cancha) {
        try (PreparedStatement ps = comandoListarDeportesPorCancha(conn, cancha.getId());
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

    private void llenarEtiquetas(Connection conn, Cancha cancha) {
        try (PreparedStatement ps = comandoListarEtiquetasPorCancha(conn, cancha.getId());
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

    @Override
    protected Cancha mapearModelo(ResultSet rs) throws SQLException{
        Cancha cancha = new Cancha();
        cancha.setId(rs.getInt("id"));
        cancha.setNombre(rs.getString("nombre"));
        cancha.setDescripcion(rs.getString("descripcion"));
        cancha.setDireccion(rs.getString("direccion"));
        cancha.setImagenUrl(rs.getString("imagenUrl"));
        cancha.setPropietario(new PropietarioDAOImpl().leer(rs.getInt("idPropietario")));
        cancha.setPrecioBase(rs.getDouble("precioBase"));
        cancha.setPromedioCalificacion(rs.getDouble("promedioCalificacion"));
        cancha.setActivo(rs.getBoolean("activo"));
        return cancha;
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
