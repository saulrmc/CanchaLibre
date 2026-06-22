using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Reservas;
public class ReservasServiceRestClient : BaseRestServiceClient<ReservaViewModel, ReservaRestDto>, IReservasServiceClient
{
    public ReservasServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) { }

    public List<ReservaViewModel> ListarPorCliente(int idCliente)
    {
        // 1. Traemos todas las reservas desde el endpoint de Java (ej. v1/reservas)
        var todasLasReservas = Api.Get<List<ReservaRestDto>>("v1/reservas") ?? new();

        // 2. Filtramos en memoria de C# por el id del cliente logueado
        var reservasDelCliente = todasLasReservas.Where(r => r.IdCliente == idCliente).ToList();

        // 3. Traemos el catálogo de canchas para cruzar nombres e imágenes de manera eficiente
        var todasLasCanchas = Api.Get<List<CanchaRestDto>>("v1/canchas") ?? new();

        // 4. Mapeamos y combinamos los datos para construir el ViewModel que la vista necesita
        return reservasDelCliente.Select(reserva => {
            var canchaAsociada = todasLasCanchas.FirstOrDefault(c => c.idCancha == reserva.IdCancha);

            return new ReservaViewModel
            {
                idReserva = reserva.idReserva,
                fechaHora = reserva.fechaHora,
                estado = Enum.TryParse<EstadoReservaEnum>(reserva.estado, true, out var est) ? est : EstadoReservaEnum.ESPERA,
                duracion = reserva.duracion,
                cancha = canchaAsociada == null ? new CanchaViewModel { nombre = "Cancha no disponible" } : new CanchaViewModel
                {
                    idCancha = canchaAsociada.idCancha,
                    nombre = canchaAsociada.nombre,
                    direccion = canchaAsociada.direccion,
                    imagenUrl = canchaAsociada.imagenUrl
                },
                pago = reserva.Pago == null ? null : new PagoViewModel
                {
                    id = reserva.Pago.id,
                    metodoPago = toMetodoPagoEnum(reserva.Pago.metodoPago),
                    monto = reserva.Pago.monto
                }
            };
        }).ToList();
    }

    private MetodoPagoEnum toMetodoPagoEnum(string metodoPago)
    {
        if (string.IsNullOrWhiteSpace(metodoPago))
        {
            return MetodoPagoEnum.EFECTIVO; // Valor por defecto si viene nulo
        }

        // El parámetro true ignora mayúsculas/minúsculas ("yape" o "YAPE" funcionarán igual)
        if (Enum.TryParse<MetodoPagoEnum>(metodoPago, true, out var resultado))
        {
            return resultado;
        }

        // Opcional: Si el texto no coincide con ningún Enum, puedes retornar un default o manejar el error
        return MetodoPagoEnum.EFECTIVO;
    }

    public void Eliminar(int id)
    {
        // DELETE hacia Java para cancelar la reserva de forma real
        Api.Delete($"v1/reservas/{id}");
    }

    // Métodos abstractos requeridos por la firma base
    public List<ReservaViewModel> Listar() => throw new NotImplementedException();
    public ReservaViewModel? Obtener(int id) => throw new NotImplementedException();
    public void Guardar(ReservaViewModel modelo, Estado estado) => throw new NotImplementedException();
    protected override ReservaViewModel ToViewModel(ReservaRestDto source) => throw new NotImplementedException();
    protected override ReservaRestDto ToRest(ReservaViewModel source) => throw new NotImplementedException();
}