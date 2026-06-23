using CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
public sealed class ReservaRestDto {
    public int idReserva {get; set;}
    public String estado {get; set;} = String.Empty;
    public ClienteRestDto? cliente {get; set;}
    public CanchaRestDto? cancha {get; set;}
    public PagoRestDto? pago {get; set;}
    public List<BloqueHorarioRestDto>? bloques {get; set;}
}