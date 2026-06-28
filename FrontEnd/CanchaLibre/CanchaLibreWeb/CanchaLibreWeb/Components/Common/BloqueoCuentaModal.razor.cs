using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Common;

public partial class BloqueoCuentaModal : ComponentBase, IDisposable
{
    [Parameter] public bool Mostrar { get; set; }
    [Parameter] public int SegundosIniciales { get; set; }
    [Parameter] public EventCallback OnFinalizado { get; set; }

    private int _segundosRestantes;
    private CancellationTokenSource? _cts;

    private string TiempoFormateado =>
        $"{_segundosRestantes / 60:00}:{_segundosRestantes % 60:00}";

    protected override void OnParametersSet()
    {
        if (Mostrar && _cts is null)
        {
            _segundosRestantes = SegundosIniciales;
            IniciarCuentaRegresiva();
        }
    }

    private void IniciarCuentaRegresiva()
    {
        _cts = new CancellationTokenSource();
        var token = _cts.Token;

        _ = Task.Run(async () =>
        {
            while (_segundosRestantes > 0 && !token.IsCancellationRequested)
            {
                await Task.Delay(1000, token);
                _segundosRestantes--;
                await InvokeAsync(StateHasChanged);
            }

            if (!token.IsCancellationRequested)
            {
                await InvokeAsync(async () =>
                {
                    Mostrar = false;
                    await OnFinalizado.InvokeAsync();
                });
            }
        }, token);
    }

    public void Dispose()
    {
        _cts?.Cancel();
        _cts?.Dispose();
    }
}