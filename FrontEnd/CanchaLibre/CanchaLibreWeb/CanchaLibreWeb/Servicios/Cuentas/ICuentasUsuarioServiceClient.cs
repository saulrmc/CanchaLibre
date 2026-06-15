using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Cuentas;

public interface ICuentasUsuarioServiceClient : IServiceClient<UsuarioViewModel>
{
    bool Login(string username, string password);
    UsuarioViewModel? ObtenerPorUsername(string username);
}
