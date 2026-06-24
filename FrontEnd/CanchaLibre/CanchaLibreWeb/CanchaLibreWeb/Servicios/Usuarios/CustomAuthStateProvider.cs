using System.Security.Claims;
using Microsoft.AspNetCore.Components.Authorization;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

namespace CanchaLibreWeb.Servicios.Usuarios;

public class CustomAuthStateProvider : AuthenticationStateProvider
{
    private readonly ClaimsPrincipal _anonimo = new(new ClaimsIdentity());
    private ClaimsPrincipal _usuarioActual;

    public CustomAuthStateProvider()
    {
        _usuarioActual = _anonimo;
    }

    public override Task<AuthenticationState> GetAuthenticationStateAsync()
    {
        return Task.FromResult(new AuthenticationState(_usuarioActual));
    }

    // AJUSTE: Ahora recibe directamente el DTO de respuesta común y el Rol detectado
    public void MarcarComoAutenticado(UsuarioRespuestaRestDto usuario, string rol)
    {
        // 1. Creamos la identidad del usuario con sus datos básicos
        var identity = new ClaimsIdentity(new[]
        {
            new Claim(ClaimTypes.Name, usuario.Nombres),
            new Claim(ClaimTypes.Email, usuario.Correo),
            new Claim("IdUsuario", usuario.Id.ToString()) // Util para saber qué cliente reserva o qué propietario publica
        }, "CustomAuthType");

        // 2. Inyectamos el rol detectado ("Cliente", "Propietario" o "Admin")
        identity.AddClaim(new Claim(ClaimTypes.Role, rol));

        _usuarioActual = new ClaimsPrincipal(identity);
        
        // 3. Notificamos a Blazor para refrescar los componentes (Navbar, páginas protegidas, etc.)
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_usuarioActual)));
    }

    public void MarcarComoCerrado()
    {
        _usuarioActual = _anonimo;
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_usuarioActual)));
    }
}