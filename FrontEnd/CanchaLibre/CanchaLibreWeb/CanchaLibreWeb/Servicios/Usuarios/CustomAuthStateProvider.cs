using System.Security.Claims;
using Microsoft.AspNetCore.Components.Authorization;

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

    public void MarcarComoAutenticado(int id, string nombres, string correo, string rol, string userName = "")
    {
        var identity = new ClaimsIdentity(new[]
        {
            new Claim(ClaimTypes.Name, nombres),
            new Claim(ClaimTypes.Email, correo),
            new Claim("IdUsuario", id.ToString()),
            new Claim("Username", userName)
        }, "CustomAuthType");

        identity.AddClaim(new Claim(ClaimTypes.Role, rol));

        _usuarioActual = new ClaimsPrincipal(identity);

        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_usuarioActual)));
    }

    public void MarcarComoCerrado()
    {
        _usuarioActual = _anonimo;
        NotifyAuthenticationStateChanged(Task.FromResult(new AuthenticationState(_usuarioActual)));
    }
}
