namespace CanchaLibreWeb.Components.Common
{
    public class Distrito
    {
        public required string IdOficial { get; set; }  // Lo que va a la BD (ej: "SAN MIGUEL")
        public required string NombreDisplay { get; set; } // Lo que ve el usuario (ej: "San Miguel (SM)")
    }
}
