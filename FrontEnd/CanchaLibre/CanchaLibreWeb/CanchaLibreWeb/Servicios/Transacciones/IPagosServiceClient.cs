using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

namespace CanchaLibreWeb.Servicios.Transacciones;

public interface IPagosServiceClient
{
    PagoRestDto CrearConReserva(int idReserva, PagoRestDto pago);
    void Actualizar(int idPago, PagoRestDto pago);
}
