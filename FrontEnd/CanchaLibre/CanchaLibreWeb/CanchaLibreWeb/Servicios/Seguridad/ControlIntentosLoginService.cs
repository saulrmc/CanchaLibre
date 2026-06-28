namespace CanchaLibreWeb.Servicios.Seguridad;

// Lleva el conteo de intentos fallidos de login y el bloqueo temporal
public sealed class ControlIntentosLoginService
{
    private const int MaxIntentos = 3;
    private static readonly TimeSpan TiempoBloqueo = TimeSpan.FromMinutes(5);

    private readonly Dictionary<string, EstadoIntentos> _intentosPorCorreo =
        new(StringComparer.OrdinalIgnoreCase);

    public void RegistrarFallo(string correo)
    {
        if (!_intentosPorCorreo.TryGetValue(correo, out var estado))
        {
            estado = new EstadoIntentos();
            _intentosPorCorreo[correo] = estado;
        }

        estado.Intentos++;

        if (estado.Intentos >= MaxIntentos)
        {
            estado.FechaBloqueo = DateTime.Now;
        }
    }

    public void RegistrarExito(string correo)
    {
        _intentosPorCorreo.Remove(correo);
    }

    // Devuelve true si está bloqueado, y cuántos segundos faltan para liberarse.
    public bool EstaBloqueado(string correo, out int segundosRestantes)
    {
        segundosRestantes = 0;

        if (!_intentosPorCorreo.TryGetValue(correo, out var estado) || estado.FechaBloqueo is null)
        {
            return false;
        }

        var tiempoTranscurrido = DateTime.Now - estado.FechaBloqueo.Value;
        var tiempoRestante = TiempoBloqueo - tiempoTranscurrido;

        if (tiempoRestante <= TimeSpan.Zero)
        {
            _intentosPorCorreo.Remove(correo);
            return false;
        }

        segundosRestantes = (int)Math.Ceiling(tiempoRestante.TotalSeconds);
        return true;
    }

    private sealed class EstadoIntentos
    {
        public int Intentos { get; set; }
        public DateTime? FechaBloqueo { get; set; }
    }
}
