namespace CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
public sealed class CanchaRestDto {
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Descripcion { get; set; } = string.Empty;
    public List<string> Deportes { get; set; } = new List<string>();
    public string ImagenUrl { get; set; } = string.Empty;
    public bool Disponible { get; set; }
    public string Direccion { get; set; } = string.Empty;
    public List<string> etiquetas { get; set; } = new List<string>();
}