using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Cuentas;

public interface ICuentasUsuarioServiceClient : IServiceClient<UsuarioViewModel>
{
    UsuarioRespuestaRestDto? Login(string username, string password);
    UsuarioViewModel? ObtenerPorUsername(string username);
}
