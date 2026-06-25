package pe.edu.pucp.canchalibre.bo.cuentas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import pe.edu.pucp.canchalibre.dao.PersonaDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAO;
import pe.edu.pucp.canchalibre.dao.cuentas.CuentaUsuarioDAOImpl;
import pe.edu.pucp.canchalibre.bo.BaseBO;
import pe.edu.pucp.canchalibre.dao.usuario.*;
import pe.edu.pucp.canchalibre.modelo.Estado;
import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

public class CuentaUsuarioBOImpl extends BaseBO implements CuentaUsuarioBO {
    private final CuentaUsuarioDAO cuentaUsuarioDao;
    private final ClienteDAO clienteDao;
    private final PropietarioDAO propietarioDao;
    private final AdministradorDAO administradorDao;

    public CuentaUsuarioBOImpl() {
        this.cuentaUsuarioDao = new CuentaUsuarioDAOImpl();

        this.clienteDao = new ClienteDAOImpl();
        this.propietarioDao = new PropietarioDAOImpl();
        this.administradorDao = new AdministradorDAOImpl();
    }
    @Override
    public Persona buscarPersonaPorUsername(String username) {
        if (username == null || username.isBlank()) return null;
        Persona persona = this.clienteDao.buscarPorCuenta(username);
        if (persona != null) return persona;
        persona = this.propietarioDao.buscarPorCuenta(username);
        if (persona != null) return persona;
        persona = this.administradorDao.buscarPorCuenta(username);
        if (persona == null) {
            CuentaUsuario cu = this.cuentaUsuarioDao.buscarPorUsernameOCorreo(username);
            if (cu != null && !cu.getUserName().equals(username)) {
                persona = buscarPersonaPorUsername(cu.getUserName());
            }
        }
        return persona;
    }

    @Override
    public boolean login(String username, String password) {
        List<PersonaDAO<?>> daosDeSeguridad = List.of(this.clienteDao, this.propietarioDao, this.administradorDao);
        CuentaUsuario cuenta = null;

        for (PersonaDAO<?> dao : daosDeSeguridad) {
            Persona persona = dao.buscarPorCuenta(username);
            if (persona != null) {
                cuenta = persona.getCuentaUsuario();
                validarCuentaUsuario(cuenta);
                break;
            }
        }

        if (cuenta == null) {
            cuenta = this.cuentaUsuarioDao.buscarPorUsernameOCorreo(username);
            if (cuenta != null) validarCuentaUsuario(cuenta);
        }

        if (cuenta == null) {
            System.out.println("[LOGIN FAILED] El nombre de usuario '" + username + "' no existe.");
            return false;
        }

        if (estaBajoCooldown(cuenta)) {return false;}
        return verificarCredenciales(cuenta,password);
    }

    private boolean estaBajoCooldown(CuentaUsuario cuenta) {
        if (cuenta.getFechaBloqueo() == null) {
            return false; // No hay bloqueo registrado
        }

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime tiempoPermitido = cuenta.getFechaBloqueo().plusMinutes(5); // 5 min de cooldown

        if (ahora.isBefore(tiempoPermitido)) {
            long segundosRestantes = java.time.Duration.between(ahora, tiempoPermitido).toSeconds();
            long minutos = segundosRestantes/60, segundos = segundosRestantes%60;
            System.out.printf("[LOGIN FAILED] Cuenta bloqueada temporalmente. Intenta en %02d:%02d minutos.%n",
                    minutos, segundos);
            return true; // Sigue bloqueado
        }

        cuenta.setIntentosFallidos(0);
        cuenta.setFechaBloqueo(null);
        this.cuentaUsuarioDao.actualizarDatosSeguridad(cuenta);
        System.out.println("[BO SEGURIDAD] Cooldown expirado. Cuenta liberada para reintento.");
        return false;
    }

    private boolean verificarCredenciales(CuentaUsuario cuenta, String passwordIngresada) {
        boolean passwordCorrecto = cuenta.getPassword().equals(passwordIngresada);

        if (passwordCorrecto) {
            cuenta.setIntentosFallidos(0);
            cuenta.setFechaBloqueo(null);
            cuenta.setUltimaSesion(LocalDateTime.now());
            cuenta.setActivo(true);

            if (cuenta.getRol() != Rol.ADMINISTRADOR) {
                this.cuentaUsuarioDao.actualizarDatosSeguridad(cuenta);
            }

            System.out.println("[LOGIN SUCCESS] ¡Bienvenido @" + cuenta.getUserName() + "!");
            return true;
        } else {
            System.out.println("[LOGIN FAILED] Contraseña incorrecta para el usuario @" + cuenta.getUserName());

            if (cuenta.getRol() == Rol.ADMINISTRADOR) {
                System.out.println("[BO SEGURIDAD] Intento fallido de ADMINISTRADOR. Exento de penalizaciones.");
            } else {
                cuenta.setIntentosFallidos(cuenta.getIntentosFallidos() + 1);
                cuenta.setUltimaSesion(LocalDateTime.now());
                this.actualizarDatosSeguridad(cuenta);
            }
            return false;
        }
    }

    @Override
    public void actualizarDatosSeguridad(CuentaUsuario cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta de usuario no puede ser nula.");
        }
        validarIdPositivo(cuenta.getId(),"id cuenta");

        // Si los intentos fallidos llegan a 3, bloqueamos la cuenta de manera preventiva
        if(cuenta.getRol()== Rol.ADMINISTRADOR) {
            System.out.println("Advertencia: Intento fallido del ADMIN. No se bloquea por política.");
        }else{
            if (cuenta.getIntentosFallidos() >= 3) {
                cuenta.setActivo(false);
                cuenta.setFechaBloqueo(LocalDateTime.now());

                System.out.println("[BO SEGURIDAD] Límite alcanzado. Cuenta @" + cuenta.getUserName()
                        + " ha sido desactivada temporalmente por 5 min.");
            }
        }
        this.cuentaUsuarioDao.actualizarDatosSeguridad(cuenta);
    }

    @Override
    public List<CuentaUsuario> listar() {
        return this.cuentaUsuarioDao.leerTodos();
    }

    @Override
    public CuentaUsuario obtener(int id) {
        validarIdPositivo(id, "id");
        return this.cuentaUsuarioDao.leer(id);
    }

    @Override
    public void eliminar(int id) {
        validarIdPositivo(id, "id");
        if (!this.cuentaUsuarioDao.eliminar(id)) {
            throw new IllegalStateException("No se pudo eliminar la cuenta de usuario con id: " + id);
        }
    }

    @Override
    public void guardar(CuentaUsuario modelo, Estado estado) {
        validarCuentaUsuario(modelo);
        validarEstado(estado);

        if (estado == Estado.NUEVO) {
            int id = this.cuentaUsuarioDao.crear(modelo);
            if (id <= 0) {
                throw new IllegalStateException("No se pudo crear la cuenta de usuario");
            }
            modelo.setId(id);
        }
        else if (estado == Estado.MODIFICADO) {
            validarIdPositivo(modelo.getId(), "id de la cuenta de usuario");
            if (!this.cuentaUsuarioDao.actualizar(modelo)) {
                throw new IllegalStateException("No se pudo actualizar la cuenta de usuario con id: " + modelo.getId());
            }
        }
        else {
            throw new IllegalArgumentException("Estado no soportado en guardar: " + estado);
        }
    }

    private void validarCuentaUsuario(CuentaUsuario modelo) {
        Objects.requireNonNull(modelo, "La cuenta de usuario es obligatoria");
        validarTextoObligatorio(modelo.getUserName(), "userName");
        validarTextoObligatorio(modelo.getPassword(), "password");
    }
}

