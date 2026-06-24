using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

namespace CanchaLibreWeb.Servicios.Usuarios;

public interface IAuthServiceClient
{
    // Retorna el usuario encontrado y qué rol tiene asignado
    (UsuarioRespuestaRestDto? Usuario, string? Rol) ValidarCredenciales(string correo, string contrasena);
    bool SolicitarRecuperacion(string correo);
}

public class AuthServiceRestClient : BaseRestServiceClient<object, object>, IAuthServiceClient
{
    public AuthServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) { }

    public (UsuarioRespuestaRestDto? Usuario, string? Rol) ValidarCredenciales(string correo, string contrasena)
    {
        // 1. Buscar en Clientes
        var clientes = Api.Get<List<UsuarioRespuestaRestDto>>("clientes");
        var clienteMatch = clientes?.FirstOrDefault(c => c.Correo.Equals(correo, StringComparison.OrdinalIgnoreCase) 
                                                     && c.cuenta.Password== contrasena);
        if (clienteMatch != null) return (clienteMatch, "Cliente");

        // 2. Buscar en Propietarios (Asumiendo que tu endpoint sigue la misma estructura de nombres)
        var propietarios = Api.Get<List<UsuarioRespuestaRestDto>>("propietarios");
        var propMatch = propietarios?.FirstOrDefault(p => p.Correo.Equals(correo, StringComparison.OrdinalIgnoreCase) 
                                                       && p.cuenta.Password == contrasena);
        if (propMatch != null) return (propMatch, "Propietario");

        // 3. Buscar en Administradores
        var admins = Api.Get<List<UsuarioRespuestaRestDto>>("administradores");
        var adminMatch = admins?.FirstOrDefault(a => a.Correo.Equals(correo, StringComparison.OrdinalIgnoreCase) 
                                                      && a.cuenta.Password == contrasena);
        if (adminMatch != null) return (adminMatch, "Admin");

        // Si no se encontró en ninguna lista
        return (null, null);
    }
    public bool SolicitarRecuperacion(string correo)
    {
        // 1. Buscamos en la lista de Clientes
        var clientes = Api.Get<List<UsuarioRespuestaRestDto>>("clientes");
        if (clientes != null && clientes.Any(c => c.Correo.Equals(correo, StringComparison.OrdinalIgnoreCase)))
        {
            // Aquí idealmente harías un POST a un endpoint de envío de correos, 
            // pero como manejas CRUD puro, al encontrarlo confirmamos que existe.
            return true;
        }

        // 2. Buscamos en la lista de Propietarios
        var propietarios = Api.Get<List<UsuarioRespuestaRestDto>>("propietarios");
        if (propietarios != null && propietarios.Any(p => p.Correo.Equals(correo, StringComparison.OrdinalIgnoreCase)))
        {
            return true;
        }

        // 3. Buscamos en la lista de Administradores
        var admins = Api.Get<List<UsuarioRespuestaRestDto>>("administradores");
        if (admins != null && admins.Any(a => a.Correo.Equals(correo, StringComparison.OrdinalIgnoreCase)))
        {
            return true;
        }

        // Si el correo no está registrado en ningún rol
        return false;
    }

    // Métodos abstractos obligatorios vacíos ya que manejamos consultas directas
    protected override object ToViewModel(object source) => throw new NotImplementedException();
    protected override object ToRest(object source) => throw new NotImplementedException();
}