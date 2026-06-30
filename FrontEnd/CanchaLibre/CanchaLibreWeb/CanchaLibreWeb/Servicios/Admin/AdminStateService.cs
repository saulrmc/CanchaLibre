using CanchaLibreWeb.Servicios.Canchas;
using CanchaLibreWeb.Servicios.Reservas;
using CanchaLibreWeb.Servicios.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Admin;

public sealed class AdminStateService : IAdminStateService
{
    private readonly IClientesServiceClient _clientesService;
    private readonly IPropietariosServiceClient _propietariosService;
    private readonly ICanchasServiceClient _canchasService;
    private readonly IReservasServiceClient _reservasService;
    private readonly IConfiguration _configuration;

    private List<ClienteViewModel>? _clientes;
    private List<PropietarioViewModel>? _propietarios;
    private List<CanchaViewModel>? _canchas;
    private List<ReservaViewModel>? _reservas;

    public AdminStateService(
        IClientesServiceClient clientesService,
        IPropietariosServiceClient propietariosService,
        ICanchasServiceClient canchasService,
        IReservasServiceClient reservasService,
        IConfiguration configuration)
    {
        _clientesService = clientesService;
        _propietariosService = propietariosService;
        _canchasService = canchasService;
        _reservasService = reservasService;
        _configuration = configuration;
    }

    public List<ClienteViewModel> Clientes => _clientes ?? new();
    public List<PropietarioViewModel> Propietarios => _propietarios ?? new();
    public List<CanchaViewModel> Canchas => _canchas ?? new();
    public List<ReservaViewModel> Reservas => _reservas ?? new();
    public bool DatosCargados { get; private set; }
    public string MensajeError { get; private set; } = string.Empty;
    public string ReportesPdfBaseUrl { get; private set; } = string.Empty;

    public async Task InicializarAsync()
    {
        if (DatosCargados) return;
        if (string.IsNullOrEmpty(ReportesPdfBaseUrl))
        {
            ReportesPdfBaseUrl = _configuration.GetValue<string>("RestResources:ReportesPdfBaseUrl") ?? "http://localhost:8080/reportes/";
        }

        try
        {
            var clientesTask = Task.Run(() => _clientesService.Listar());
            var propietariosTask = Task.Run(() => _propietariosService.Listar());
            var canchasTask = Task.Run(() => _canchasService.Listar());
            var reservasTask = Task.Run(() => _reservasService.Listar());

            await Task.WhenAll(clientesTask, propietariosTask, canchasTask, reservasTask);

            _clientes = await clientesTask;
            _propietarios = await propietariosTask;
            _canchas = await canchasTask;
            _reservas = await reservasTask;

            DatosCargados = true;
        }
        catch (Exception ex)
        {
            MensajeError = $"Error al cargar datos: {ex.Message}";
        }
    }

    public void ForzarRecarga()
    {
        _clientes = null;
        _propietarios = null;
        _canchas = null;
        _reservas = null;
        DatosCargados = false;
        MensajeError = string.Empty;
    }
}
