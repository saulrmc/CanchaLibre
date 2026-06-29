using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Common;

public partial class ToastReservaExitosa : ComponentBase
{
    [Parameter] public bool Mostrar { get; set; }
    [Parameter] public string Titulo { get; set; } = "¡Reserva confirmada!";
    [Parameter] public string Mensaje { get; set; } = string.Empty;
}