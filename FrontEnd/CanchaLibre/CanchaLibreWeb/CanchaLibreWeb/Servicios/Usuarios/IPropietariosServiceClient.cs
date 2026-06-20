using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Usuarios;

public interface IPropietariosServiceClient : IServiceClient<PropietarioViewModel> {
    PropietarioViewModel? BuscarPorNombre(string nombre);
}