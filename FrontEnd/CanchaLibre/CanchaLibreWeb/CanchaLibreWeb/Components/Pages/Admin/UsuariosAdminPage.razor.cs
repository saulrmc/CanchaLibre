using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class UsuariosAdminPage : ComponentBase
{
    // Modelo interno para los renglones de la tabla
    protected class UsuarioRow
    {
        public int Id { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string Identificador { get; set; } = string.Empty; // Correo o RUC
        public string Tipo { get; set; } = string.Empty; // "Cliente" o "Propietario"
        public string UltimaActividad { get; set; } = string.Empty;
        public string Estado { get; set; } = string.Empty; // "Activo" o "Bloqueado"
    }

    // Listas para administrar el estado
    private List<UsuarioRow> _usuariosMaster = new();
    protected List<UsuarioRow> UsuariosFiltrados { get; set; } = new();

    // Propiedades vinculadas a los filtros de la interfaz
    protected string TextoBusqueda { get; set; } = string.Empty;
    protected string FiltroTipo { get; set; } = "Todos";
    protected string FiltroEstado { get; set; } = "Todos";

    protected override void OnInitialized()
    {
        // Rellenamos con la data de muestra de tu diseño
        _usuariosMaster = new List<UsuarioRow>
        {
            new() { Id = 1, Nombre = "Carlos Mendoza", Identificador = "carlos.mendoza@email.com", Tipo = "Cliente", UltimaActividad = "10 mayo 2026", Estado = "Activo" },
            new() { Id = 2, Nombre = "Carlos Mendoza", Identificador = "carlos.mendoza@email.com", Tipo = "Cliente", UltimaActividad = "10 mayo 2026", Estado = "Activo" },
            new() { Id = 3, Nombre = "Juan Pérez", Identificador = "2020202020", Tipo = "Propietario", UltimaActividad = "10 mayo 2026", Estado = "Activo" },
            new() { Id = 4, Nombre = "Carlos Mendoza", Identificador = "carlos.mendoza@email.com", Tipo = "Cliente", UltimaActividad = "10 mayo 2026", Estado = "Activo" },
            new() { Id = 5, Nombre = "Carlos Mendoza", Identificador = "carlos.mendoza@email.com", Tipo = "Cliente", UltimaActividad = "10 mayo 2026", Estado = "Activo" },
            new() { Id = 6, Nombre = "Carlos Mendoza", Identificador = "carlos.mendoza@email.com", Tipo = "Cliente", UltimaActividad = "10 mayo 2026", Estado = "Activo" }
        };

        // Al inicio mostramos todos
        UsuariosFiltrados = new List<UsuarioRow>(_usuariosMaster);
    }

    // Ejecutado al hacer clic en el botón "Buscar"
    protected void FiltrarUsuarios()
    {
        var consulta = _usuariosMaster.AsEnumerable();

        // 1. Filtrar por caja de texto (Nombre, Correo o RUC)
        if (!string.IsNullOrWhiteSpace(TextoBusqueda))
        {
            consulta = consulta.Where(u => 
                u.Nombre.Contains(TextoBusqueda, StringComparison.OrdinalIgnoreCase) || 
                u.Identificador.Contains(TextoBusqueda, StringComparison.OrdinalIgnoreCase));
        }

        // 2. Filtrar por Tipo de Usuario
        if (FiltroTipo != "Todos")
        {
            consulta = consulta.Where(u => u.Tipo.Equals(FiltroTipo, StringComparison.OrdinalIgnoreCase));
        }

        // 3. Filtrar por Estado
        if (FiltroEstado != "Todos")
        {
            consulta = consulta.Where(u => u.Estado.Equals(FiltroEstado, StringComparison.OrdinalIgnoreCase));
        }

        UsuariosFiltrados = consulta.ToList();
    }
}