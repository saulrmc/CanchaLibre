using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Reservas;
public class ReservasServiceRestClient : BaseRestServiceClient<ReservaViewModel, ReservaRestDto>, IReservasServiceClient
{
    public ReservasServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) { }
    private const string ResourcePath = "reservas";
    public List<ReservaViewModel> ListarPorCliente(int idCliente)
    {
        // 1. Traemos todas las reservas desde el endpoint de Java (ej. v1/reservas)
        var todasLasReservas = Api.Get<List<ReservaRestDto>>(ResourcePath) ?? new();

        // 2. Filtramos en memoria de C# por el id del cliente logueado
        var reservasDelCliente = todasLasReservas.Where(r => r.cliente.Id == idCliente).ToList();

        // 4. Mapeamos y combinamos los datos para construir el ViewModel que la vista necesita
        return reservasDelCliente.Select(reserva => {
            var canchaAsociada = todasLasCanchas.FirstOrDefault(c => c.idCancha == reserva.cancha.idCancha);

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
        Api.Delete($"{ResourcePath}/{id}");
    }

    // Métodos abstractos requeridos por la firma base
    public List<ReservaViewModel> Listar()
    {
        var payload = Api.Get<List<ReservaRestDto>>(ResourcePath);

        var response = new List<ReservaViewModel>(payload.Count);
        foreach (var item in payload)
        {
            response.Add(ToViewModel(item));
        }
        return response;
    }    
    public ReservaViewModel? Obtener(int id)
    {
        try {
            var payload = Api.Get<ReservaRestDto>($"{ResourcePath}/{id}");
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }
    public void Guardar(ReservaViewModel modelo, Estado estado)
    {
        
    }
    protected override ReservaViewModel ToViewModel(ReservaRestDto source)
    {
        return new ReservaViewModel
        {
            bloques = ParseBloques(source.bloques),
            cancha = ParseCancha(source.cancha),
            cliente = ParseCliente(source.cliente),
            estado = ParseEnum<EstadoReservaEnum>(source.estado, EstadoReservaEnum.ESPERA),
            idReserva = source.idReserva,
            pago = ParsePago(source.pago)
        };
    }

    private PagoViewModel ParsePago(PagoRestDto? pago)
    {
        return new PagoViewModel
        {
            comprobante = ParseComprobante(pago.comprobante),
            fechaPago = pago.fechaPago,
            idPago = pago.idPago,
            metodoPago = ParseEnum<MetodoPagoEnum>(pago.metodoPago, MetodoPagoEnum.YAPE),
            monto = pago.monto
        };
    }

    private ComprobanteViewModel ParseComprobante(ComprobanteRestDto? comprobante)
    {
        return new ComprobanteViewModel
        {
            FechaEmision = comprobante.FechaEmision,
            idComprobante = comprobante.idComprobante,
            numero = comprobante.numero,
            serie = comprobante.serie
        };
    }

    private ClienteViewModel ParseCliente(ClienteRestDto? cliente)
    {
        return new ClienteViewModel
        {
            Activo = cliente.Activo,
            Calificacion = cliente.Calificacion,
            Correo = cliente.Correo,
            cuenta = ParseCuenta(cliente.cuenta),
            Id = cliente.Id,
            Nombres = cliente.Nombres,
            Telefono = cliente.Telefono
        };
    }

    private CuentaUsuarioViewModel ParseCuenta(CuentaUsuarioRestDto? cuenta)
    {
        return new CuentaUsuarioViewModel
        {
            Activo = cuenta.Activo,
            fechaBloqueo = cuenta.fechaBloqueo,
            Id = cuenta.Id,
            IntentosFallidos = cuenta.IntentosFallidos,
            Password = cuenta.Password,
            Rol = ParseEnum<RolEnum>(cuenta.Rol, RolEnum.NO_ADMITIDO),
            UltimaSesion = cuenta.UltimaSesion,
            UserName = cuenta.UserName
        };
    }

    private CanchaViewModel ParseCancha(CanchaRestDto? cancha)
    {
        return new CanchaViewModel
        {
            activo = cancha.activo,
            bloques = ParseBloques(cancha.bloques),
            deportes = ParseDeportes(cancha.deportes),
            descripcion = cancha.descripcion,
            direccion = cancha.direccion,
            disponible = cancha.disponible,
            idCancha = cancha.idCancha,
            etiquetas = ParseEtiquetas(cancha.etiquetas),
            imagenUrl = cancha.imagenUrl,
            nombre = cancha.nombre,
            precioBase = cancha.precioBase,
            promedioCalificacion = cancha.promedioCalificacion,
            propietario = ParsePropietario(cancha.propietario)
        };
    }

    private ViewModels.PropietarioViewModel ParsePropietario(Servicios.Rest.Dtos.Usuarios.PropietarioRestDto? propietario)
    {
        if (propietario == null) return null;

        return new ViewModels.PropietarioViewModel
        {
            Id = propietario.Id,
            Nombres = propietario.Nombres,
            Telefono = propietario.Telefono,
            Correo = propietario.Correo,
            Calificacion = propietario.Calificacion,
            Activo = propietario.Activo
        };
    }

    private List<EtiquetaEnum> ParseEtiquetas(List<string>? etiquetas)
    {
        var list = new List<EtiquetaEnum>();
        foreach (var etiqueta in etiquetas)
        {
            list.Add(ParseEnum<EtiquetaEnum>(etiqueta, EtiquetaEnum.ILUMINACIÓN));
        }
        return list;
    }

    private List<DeporteEnum> ParseDeportes(List<string>? deportes)
    {
        List<DeporteEnum> list = new List<DeporteEnum>();
        foreach(var deporte in deportes)
        {
            list.Add(ParseEnum<DeporteEnum>(deporte, DeporteEnum.FUTBOL));
        }
        return list;
    }

    private List<BloqueHorarioViewModel> ParseBloques(List<BloqueHorarioRestDto>? bloques)
    {
        List<BloqueHorarioViewModel> list = new List<BloqueHorarioViewModel>();
        foreach(var bloque in bloques)
        {
            list.Add(new BloqueHorarioViewModel
            {
                diaSemana = ParseEnum<DiaSemanaEnum>(bloque.diaSemana, DiaSemanaEnum.NO_VALIDO),
                estadoBloque = ParseEnum<EstadoBloqueEnum>(bloque.estadoBloque, EstadoBloqueEnum.NO_VALIDO),
                horaFin = bloque.horaFin,
                horaInicio = bloque.horaInicio,
                precio = bloque.precio

            });
        }
        return list;
    }

    protected override ReservaRestDto ToRest(ReservaViewModel source)
    {   
        if (source == null) return null;

        return new ReservaRestDto
        {
            idReserva = source.idReserva,
            estado = source.estado.ToString(),
            duracion = source.duracion,
            fechaHora = source.fechaHora,
            bloques = source.bloques?.Select(b => new BloqueHorarioRestDto
            {
                diaSemana = b.diaSemana.ToString(),
                estadoBloque = b.estadoBloque.ToString(),
                horaInicio = b.horaInicio,
                horaFin = b.horaFin,
                precio = b.precio
            }).ToList(),
            cancha = source.cancha == null ? null : new CanchaRestDto
            {
                idCancha = source.cancha.idCancha,
                nombre = source.cancha.nombre,
                direccion = source.cancha.direccion,
                descripcion = source.cancha.descripcion,
                disponible = source.cancha.disponible,
                activo = source.cancha.activo,
                imagenUrl = source.cancha.imagenUrl,
                precioBase = source.cancha.precioBase,
                promedioCalificacion = source.cancha.promedioCalificacion,
                etiquetas = source.cancha.etiquetas?.Select(e => e.ToString()).ToList(),
                deportes = source.cancha.deportes?.Select(d => d.ToString()).ToList(),
                bloques = source.cancha.bloques?.Select(b => new BloqueHorarioRestDto
                {
                    diaSemana = b.diaSemana.ToString(),
                    estadoBloque = b.estadoBloque.ToString(),
                    horaInicio = b.horaInicio,
                    horaFin = b.horaFin,
                    precio = b.precio
                }).ToList(),
                propietario = source.cancha.propietario == null ? null : new Servicios.Rest.Dtos.Usuarios.PropietarioRestDto
                {
                    Id = source.cancha.propietario.Id,
                    Nombres = source.cancha.propietario.Nombres,
                    Telefono = source.cancha.propietario.Telefono,
                    Correo = source.cancha.propietario.Correo,
                    Calificacion = source.cancha.propietario.Calificacion,
                    Activo = source.cancha.propietario.Activo
                }
            },
            cliente = source.cliente == null ? null : new ClienteRestDto
            {
                Id = source.cliente.Id,
                Nombres = source.cliente.Nombres,
                Telefono = source.cliente.Telefono,
                Correo = source.cliente.Correo,
                Calificacion = source.cliente.Calificacion,
                Activo = source.cliente.Activo,
                cuenta = source.cliente.cuenta == null ? null : new CuentaUsuarioRestDto
                {
                    Id = source.cliente.cuenta.Id,
                    UserName = source.cliente.cuenta.UserName,
                    Password = source.cliente.cuenta.Password,
                    Rol = source.cliente.cuenta.Rol.ToString(),
                    Activo = source.cliente.cuenta.Activo,
                    IntentosFallidos = source.cliente.cuenta.IntentosFallidos,
                    UltimaSesion = source.cliente.cuenta.UltimaSesion,
                    fechaBloqueo = source.cliente.cuenta.fechaBloqueo
                }
            },
            pago = source.pago == null ? null : new PagoRestDto
            {
                idPago = source.pago.idPago,
                monto = source.pago.monto,
                fechaPago = source.pago.fechaPago,
                metodoPago = source.pago.metodoPago.ToString(),
                comprobante = source.pago.comprobante == null ? null : new ComprobanteRestDto
                {
                    idComprobante = source.pago.comprobante.idComprobante,
                    numero = source.pago.comprobante.numero,
                    serie = source.pago.comprobante.serie,
                    FechaEmision = source.pago.comprobante.FechaEmision
                }
            }
        };
    }
}