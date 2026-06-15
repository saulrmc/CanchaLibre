using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Login;

public partial class LogoutPage : ComponentBase {
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;

    protected override void OnInitialized() {
        NavigationManager.NavigateTo("/auth/logout", forceLoad: true);
    }
}
