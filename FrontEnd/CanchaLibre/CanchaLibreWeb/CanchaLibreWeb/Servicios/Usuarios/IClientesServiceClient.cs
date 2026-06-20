using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Usuarios;

public interface IClientesServiceClient : IServiceClient<ClienteViewModel> {
    ClienteViewModel? BuscarPorNombre(string nombre);
}