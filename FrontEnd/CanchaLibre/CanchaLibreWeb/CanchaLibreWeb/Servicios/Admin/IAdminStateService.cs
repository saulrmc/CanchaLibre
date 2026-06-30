using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Admin;

public interface IAdminStateService
{
    List<ClienteViewModel> Clientes { get; }
    List<PropietarioViewModel> Propietarios { get; }
    List<CanchaViewModel> Canchas { get; }
    List<ReservaViewModel> Reservas { get; }
    bool DatosCargados { get; }
    string MensajeError { get; }
    Task InicializarAsync();
    void ForzarRecarga();
    string ReportesPdfBaseUrl { get; }
}
