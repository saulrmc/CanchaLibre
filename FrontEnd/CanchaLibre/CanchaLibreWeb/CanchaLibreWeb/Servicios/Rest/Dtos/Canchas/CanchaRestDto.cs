namespace CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
public sealed class CanchaRestDto
{
    public int idCancha { get; set; }
    public string nombre { get; set; } = string.Empty;
    public string descripcion { get; set; } = string.Empty;
    public List<string> deportes { get; set; } = new List<string>();
    public string imagenUrl { get; set; } = string.Empty;
    public bool disponible { get; set; }
    public string direccion { get; set; } = string.Empty;
    public int IdPropietario { get; set; }
    public List<string>? etiquetas{ get; set; } = new List<string>();
}