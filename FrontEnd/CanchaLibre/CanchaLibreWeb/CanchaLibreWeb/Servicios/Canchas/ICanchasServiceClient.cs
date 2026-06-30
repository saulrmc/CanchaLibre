using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Canchas;

public interface ICanchasServiceClient : IServiceClient<CanchaViewModel>
{
    List<CanchaViewModel> ListarPorPropietario(string userName);
}
