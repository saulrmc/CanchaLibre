using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

namespace CanchaLibreWeb.Servicios.Transacciones;

public interface IComprobantesServiceClient
{
    ComprobanteRestDto CrearConReserva(int idReserva, ComprobanteRestDto comprobante);
}
