using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Reservas;

public interface IReservasServiceClient : IServiceClient<ReservaViewModel>
{
    List<ReservaViewModel> ListarPorCliente(int idCliente);
}