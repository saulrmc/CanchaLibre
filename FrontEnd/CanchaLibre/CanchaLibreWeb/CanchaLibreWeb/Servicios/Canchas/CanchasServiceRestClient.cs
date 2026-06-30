using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Canchas;

public class CanchasServiceRestClient : BaseRestServiceClient<CanchaViewModel, CanchaRestDto>, ICanchasServiceClient
{
    public CanchasServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory)
    {
    }

    private const string ResourcePath = "canchas";

    public List<CanchaViewModel> Listar()
    {
        var payload = Api.Get<List<CanchaRestDto>>(ResourcePath);

        var response = new List<CanchaViewModel>(payload.Count);
        foreach (var item in payload)
        {
            response.Add(ToViewModel(item));
        }

        return response;
    }

    public List<CanchaViewModel> ListarPorPropietario(string userName)
    {
        var payload = Api.Get<List<CanchaRestDto>>($"{ResourcePath}/propietario/{userName}");
        var response = new List<CanchaViewModel>(payload.Count);
        foreach (var item in payload)
        {
            response.Add(ToViewModel(item));
        }
        return response;
    }

    public CanchaViewModel? Obtener(int id)
    {
        try
        {
            var payload = Api.Get<CanchaRestDto>($"{ResourcePath}/{id}");
            return ToViewModel(payload);
        }
        catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound)
        {
            return null;
        }
    }

    public void Guardar(CanchaViewModel modelo, Estado estado)
    {
        var payload = ToRest(modelo);
        switch (estado)
        {
            case Estado.Nuevo:
                Api.Post(ResourcePath, payload);
                break;
            case Estado.Modificado:
                Api.Put($"{ResourcePath}/{modelo.id}", payload);
                break;
            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id)
    {
        Api.Delete($"{ResourcePath}/{id}");
    }

    protected override CanchaViewModel ToViewModel(CanchaRestDto source)
    {
        string direccionLimpia = source.direccion ?? string.Empty;
        string distritoDetectado = string.Empty;

        if (direccionLimpia.Contains(" - "))
        {
            var partes = direccionLimpia.Split(new[] { " - " }, StringSplitOptions.None);
            if (partes.Length >= 2)
            {
                direccionLimpia = partes[0];
                distritoDetectado = partes[1];
            }
        }

        return new CanchaViewModel
        {
            id = source.id,
            activo = source.activo,
            nombre = source.nombre,
            descripcion = source.descripcion,

            direccion = direccionLimpia,
            distrito = distritoDetectado,

            imagenUrl = source.imagenUrl,
            precioBase = source.precioBase,
            promedioCalificacion = source.promedioCalificacion,
            deportes = ParseEnumDeportes(source.deportes),
            bloques = ParseBloques(source.bloques),
            etiquetas = ParseEnumEtiquetas(source.etiquetas),
            propietario = source.propietario != null ? new PropietarioViewModel
            {
                Id = source.propietario.Id,
                Nombres = source.propietario.Nombres ?? string.Empty,
                Correo = source.propietario.Correo ?? string.Empty,
                Telefono = source.propietario.Telefono ?? string.Empty,
                Cuenta = source.propietario.Cuenta != null ? new CuentaUsuarioViewModel
                {
                    UserName = source.propietario.Cuenta.UserName ?? string.Empty,
                    Password = source.propietario.Cuenta.Password ?? string.Empty,
                    Activo = source.propietario.Cuenta.Activo,
                    Rol = RolEnum.PROPIETARIO,
                    IntentosFallidos = source.propietario.Cuenta.IntentosFallidos,
                    UltimaSesion = source.propietario.Cuenta.UltimaSesion ?? DateTime.MinValue,
                    FechaBloqueo = source.propietario.Cuenta.FechaBloqueo ?? DateTime.MinValue
                } : null,
                Calificacion = source.propietario.Calificacion,
                Ruc = source.propietario.Ruc ?? string.Empty,
                Saldo = source.propietario.Saldo
            } : null
        };
    }

    private List<BloqueHorarioViewModel> ParseBloques(List<BloqueHorarioRestDto>? bloques)
    {
        if (bloques == null || !bloques.Any()) return new List<BloqueHorarioViewModel>();
        List<BloqueHorarioViewModel> bloquesView = new List<BloqueHorarioViewModel>();
        foreach (var bloque in bloques)
        {
            bloquesView.Add(
                new BloqueHorarioViewModel
                {
                    id = bloque.id,
                    diaSemana = toDiaSemana(bloque.diaSemana),
                    estadoBloque = toEstadoBloque(bloque.estadoBloque),
                    horaFin = bloque.horaFin,
                    horaInicio = bloque.horaInicio,
                    precio = bloque.precio
                }
            );
        }
        return bloquesView;
    }

    private EstadoBloqueEnum toEstadoBloque(string estadoBloque)
    {
        if (Enum.TryParse<EstadoBloqueEnum>(estadoBloque, true, out EstadoBloqueEnum estadoConvertido))
        {
            return estadoConvertido;
        }
        return EstadoBloqueEnum.NO_VALIDO;
    }

    private DiaSemanaEnum toDiaSemana(string diaSemana)
    {
        if (Enum.TryParse<DiaSemanaEnum>(diaSemana, true, out DiaSemanaEnum diaConvertido))
        {
            return diaConvertido;
        }
        return DiaSemanaEnum.NO_VALIDO;
    }

    private List<DeporteEnum> ParseEnumDeportes(List<string>? deportes)
    {
        if (deportes == null || !deportes.Any()) return new List<DeporteEnum>();
        var result = new List<DeporteEnum>();

        foreach (var deporte in deportes)
        {
            var deporteNormalizado = deporte.ToUpper() switch
            {
                "FUTBOL" => "Fútbol",
                _ => deporte
            };

            if (Enum.TryParse<DeporteEnum>(deporteNormalizado, true, out var parsed))
            {
                result.Add(parsed);
            }
        }
        return result;
    }

    private List<EtiquetaEnum> ParseEnumEtiquetas(List<string>? etiquetas)
    {
        if (etiquetas == null || !etiquetas.Any()) return new List<EtiquetaEnum>();
        var result = new List<EtiquetaEnum>();

        foreach (var etiqueta in etiquetas)
        {
            var etiquetaNormalizada = etiqueta.ToUpper() switch
            {
                "ILUMINACION" => "ILUMINACIÓN",
                "BANOS" => "BAÑOS",
                _ => etiqueta
            };

            if (Enum.TryParse<EtiquetaEnum>(etiquetaNormalizada, true, out var parsed))
            {
                result.Add(parsed);
            }
        }
        return result;
    }

    protected override CanchaRestDto ToRest(CanchaViewModel source)
    {
        return new CanchaRestDto
        {
            id = source.id,
            activo = source.activo,
            nombre = source.nombre,
            descripcion = source.descripcion,

            direccion = $"{source.direccion} - {source.distrito}",

            imagenUrl = source.imagenUrl,
            precioBase = source.precioBase,
            promedioCalificacion = source.promedioCalificacion,

            deportes = ParseStringDeportes(source.deportes),
            etiquetas = ParseStringEtiquetas(source.etiquetas),

            bloques = source.bloques?.Select(b => new BloqueHorarioRestDto
            {
                id = b.id,
                diaSemana = b.diaSemana.ToString(),
                estadoBloque = b.estadoBloque.ToString(),
                horaInicio = b.horaInicio,
                horaFin = b.horaFin,
                precio = b.precio,
            }).ToList(),

            propietario = source.propietario != null ? new PropietarioRestDto
            {
                Id = source.propietario.Id,
                Nombres = source.propietario.Nombres,
                Correo = source.propietario.Correo,
                Telefono = source.propietario.Telefono,
                Calificacion = source.propietario.Calificacion,
                Ruc = source.propietario.Ruc,
                Saldo = source.propietario.Saldo,
                Cuenta = source.propietario.Cuenta != null ? new CuentaUsuarioRestDto
                {
                    UserName = source.propietario.Cuenta.UserName,
                    Password = source.propietario.Cuenta.Password,
                    Activo = source.propietario.Cuenta.Activo,
                    Rol = "PROPIETARIO",
                    IntentosFallidos = source.propietario.Cuenta.IntentosFallidos,
                    UltimaSesion = source.propietario.Cuenta.UltimaSesion,
                    FechaBloqueo = source.propietario.Cuenta.FechaBloqueo
                } : null
            } : null
        };
    }

    private List<string> ParseStringDeportes(List<DeporteEnum>? deportes)
    {
        if (deportes == null) return new List<string>();
        return deportes.Select(d =>
        {
            var nombreLimpio = d.ToString().ToUpper();
            return nombreLimpio switch
            {
                "FÚTBOL" => "FUTBOL",
                _ => nombreLimpio
            };
        }).ToList();
    }

    private List<string> ParseStringEtiquetas(List<EtiquetaEnum>? etiquetas)
    {
        if (etiquetas == null) return new List<string>();
        return etiquetas.Select(e =>
            {
                var nombreLimpio = e.ToString().ToUpper();
                return nombreLimpio switch
                {
                    "ILUMINACIÓN" => "ILUMINACION",
                    "BAÑOS" => "BANOS",
                    _ => nombreLimpio
                };
            }).ToList();
        }
    }