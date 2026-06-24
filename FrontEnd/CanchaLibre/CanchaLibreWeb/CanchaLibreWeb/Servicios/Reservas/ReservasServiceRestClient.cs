using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Reservas;
public class ReservasServiceRestClient : BaseRestServiceClient<ReservaViewModel, ReservaRestDto>, IReservasServiceClient
{
    public ReservasServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) { }
    private const string ResourcePath = "reservas";
    public List<ReservaViewModel> ListarPorCliente(int idCliente)
    {
        var payload = Api.Get<List<ReservaRestDto>>($"{ResourcePath}/cliente/{idCliente}");

        var response = new List<ReservaViewModel>(payload.Count);
        foreach (var item in payload)
        {
            response.Add(ToViewModel(item));
        }
        return response;
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

    private ClienteViewModel ParseCliente(Rest.Dtos.Usuarios.ClienteRestDto? cliente)
    {
        return new ClienteViewModel
        {
            Activo = cliente.Activo,
            Calificacion = cliente.Calificacion,
            Correo = cliente.Correo,
            Cuenta = ParseCuenta(cliente.cuenta),
            Id = cliente.Id,
            Nombres = cliente.Nombres,
            Telefono = cliente.Telefono
        };
    }

    private CuentaUsuarioViewModel ParseCuenta(Rest.Dtos.Cuentas.CuentaUsuarioRestDto? cuenta)
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

    private CanchaViewModel ParseCancha(Rest.Dtos.Canchas.CanchaRestDto? cancha)
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

    private PropietarioViewModel ParsePropietario(Rest.Dtos.Usuarios.PropietarioRestDto? propietario)
    {
        if (propietario == null) return null;

        return new PropietarioViewModel
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

    private List<BloqueHorarioViewModel> ParseBloques(List<Rest.Dtos.Canchas.BloqueHorarioRestDto>? bloques)
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
            bloques = source.bloques?.Select(b => new Rest.Dtos.Canchas.BloqueHorarioRestDto
            {
                diaSemana = b.diaSemana.ToString(),
                estadoBloque = b.estadoBloque.ToString(),
                horaInicio = b.horaInicio,
                horaFin = b.horaFin,
                precio = b.precio
            }).ToList(),
            cancha = source.cancha == null ? null : new Rest.Dtos.Canchas.CanchaRestDto
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
                bloques = source.cancha.bloques?.Select(b => new Rest.Dtos.Canchas.BloqueHorarioRestDto
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
            cliente = source.cliente == null ? null : new Rest.Dtos.Usuarios.ClienteRestDto
            {
                Id = source.cliente.Id,
                Nombres = source.cliente.Nombres,
                Telefono = source.cliente.Telefono,
                Correo = source.cliente.Correo,
                Calificacion = source.cliente.Calificacion,
                Activo = source.cliente.Activo,
                cuenta = source.cliente.Cuenta == null ? null : new Rest.Dtos.Cuentas.CuentaUsuarioRestDto
                {
                    Id = source.cliente.Cuenta.Id,
                    UserName = source.cliente.Cuenta.UserName,
                    Password = source.cliente.Cuenta.Password,
                    Rol = source.cliente.Cuenta.Rol.ToString(),
                    Activo = source.cliente.Cuenta.Activo,
                    IntentosFallidos = source.cliente.Cuenta.IntentosFallidos,
                    UltimaSesion = source.cliente.Cuenta.UltimaSesion,
                    fechaBloqueo = source.cliente.Cuenta.fechaBloqueo
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