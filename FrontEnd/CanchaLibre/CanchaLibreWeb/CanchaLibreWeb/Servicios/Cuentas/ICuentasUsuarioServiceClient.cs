using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Cuentas;

public interface ICuentasUsuarioServiceClient : IServiceClient<CuentaUsuarioViewModel>
{
    bool? Login(string username, string password);
    CuentaUsuarioViewModel? ObtenerPorUsername(string username);
    CuentaUsuarioViewModel? CrearConRetorno(CuentaUsuarioViewModel modelo);
}
