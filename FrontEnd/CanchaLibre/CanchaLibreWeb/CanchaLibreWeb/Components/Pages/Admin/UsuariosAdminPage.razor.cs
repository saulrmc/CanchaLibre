using CanchaLibreWeb.Servicios.Admin;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class UsuariosAdminPage : ComponentBase
{
    [Inject] private IAdminStateService AdminState { get; set; } = default!;

    protected string UrlReporteUsuarios =>
        $"{AdminState.ReportesPdfBaseUrl}usuarios";

    protected class UsuarioRow
    {
        public int Id { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string Identificador { get; set; } = string.Empty;
        public string Tipo { get; set; } = string.Empty;
        public string UltimaActividad { get; set; } = string.Empty;
        public string Estado { get; set; } = string.Empty;
    }

    private List<UsuarioRow> _usuariosMaster = new();
    protected List<UsuarioRow> UsuariosFiltrados { get; set; } = new();

    protected string TextoBusqueda { get; set; } = string.Empty;
    protected string FiltroTipo { get; set; } = "Todos";
    protected string FiltroEstado { get; set; } = "Todos";

    private bool datosCargados;
    private int totalClientes;
    private int totalPropietarios;
    private int actividadReciente;
    private int bloqueados;
    private string mensajeError = string.Empty;

    protected override async Task OnInitializedAsync()
    {
        try
        {
            await AdminState.InicializarAsync();

            if (!string.IsNullOrEmpty(AdminState.MensajeError))
            {
                mensajeError = AdminState.MensajeError;
                return;
            }

            var clientes = AdminState.Clientes;
            var propietarios = AdminState.Propietarios;

            totalClientes = clientes.Count;
            totalPropietarios = propietarios.Count;

            var ahora = DateTime.Now;
            var hace30Dias = ahora.AddDays(-30);

            var todosLosUsuarios = new List<UsuarioRow>();

            foreach (var c in clientes)
            {
                if (c is null) continue;
                var esBloqueado = c.Cuenta?.FechaBloqueo > DateTime.MinValue || (c.Cuenta?.IntentosFallidos ?? 0) >= 3;
                if (esBloqueado) bloqueados++;
                if (c.Cuenta?.UltimaSesion >= hace30Dias) actividadReciente++;

                todosLosUsuarios.Add(new UsuarioRow
                {
                    Id = c.Id,
                    Nombre = c.Nombres,
                    Identificador = c.Correo,
                    Tipo = "Cliente",
                    UltimaActividad = c.Cuenta?.UltimaSesion > DateTime.MinValue
                        ? c.Cuenta.UltimaSesion.ToString("dd MMM yyyy")
                        : "Nunca",
                    Estado = esBloqueado ? "Bloqueado" : "Activo"
                });
            }

            foreach (var p in propietarios)
            {
                if (p is null) continue;
                var esBloqueado = p.Cuenta?.FechaBloqueo > DateTime.MinValue || (p.Cuenta?.IntentosFallidos ?? 0) >= 3;
                if (esBloqueado) bloqueados++;
                if (p.Cuenta?.UltimaSesion >= hace30Dias) actividadReciente++;

                todosLosUsuarios.Add(new UsuarioRow
                {
                    Id = p.Id,
                    Nombre = p.Nombres,
                    Identificador = string.IsNullOrEmpty(p.Ruc) ? p.Correo : p.Ruc,
                    Tipo = "Propietario",
                    UltimaActividad = p.Cuenta?.UltimaSesion > DateTime.MinValue
                        ? p.Cuenta.UltimaSesion.ToString("dd MMM yyyy")
                        : "Nunca",
                    Estado = esBloqueado ? "Bloqueado" : "Activo"
                });
            }

            _usuariosMaster = todosLosUsuarios;
            UsuariosFiltrados = new List<UsuarioRow>(_usuariosMaster);
            datosCargados = true;
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al cargar usuarios: {ex.Message}";
        }
    }

    protected void FiltrarUsuarios()
    {
        var consulta = _usuariosMaster.AsEnumerable();

        if (!string.IsNullOrWhiteSpace(TextoBusqueda))
        {
            consulta = consulta.Where(u =>
                u.Nombre.Contains(TextoBusqueda, StringComparison.OrdinalIgnoreCase) ||
                u.Identificador.Contains(TextoBusqueda, StringComparison.OrdinalIgnoreCase));
        }

        if (FiltroTipo != "Todos")
            consulta = consulta.Where(u => u.Tipo.Equals(FiltroTipo, StringComparison.OrdinalIgnoreCase));

        if (FiltroEstado != "Todos")
            consulta = consulta.Where(u => u.Estado.Equals(FiltroEstado, StringComparison.OrdinalIgnoreCase));

        UsuariosFiltrados = consulta.ToList();
    }
}
