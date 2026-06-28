using CanchaLibreWeb.Components;
using CanchaLibreWeb.Servicios.Canchas;
using CanchaLibreWeb.Servicios.Cuentas;
using CanchaLibreWeb.Servicios.Reservas;
using CanchaLibreWeb.Servicios.Usuarios;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Components.Authorization;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services
    .AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(opciones =>
    {
        opciones.LoginPath = "/Login";
        opciones.AccessDeniedPath = "/Login";
        opciones.SlidingExpiration = true;
        opciones.ExpireTimeSpan = TimeSpan.FromHours(8);
    });

builder.Services.AddAuthorization();
builder.Services.AddCascadingAuthenticationState();
builder.Services.AddScoped<AuthenticationStateProvider, CustomAuthStateProvider>();

builder.Services.AddHttpContextAccessor();
builder.Services.AddHttpClient();
builder.Services.AddScoped<ICuentasUsuarioServiceClient, CuentasUsuarioRestClient>();
builder.Services.AddScoped<IPropietariosServiceClient, PropietariosServiceRestClient>();
builder.Services.AddScoped<IClientesServiceClient, ClientesServiceRestClient>();
builder.Services.AddScoped<IReservasServiceClient, ReservasServiceRestClient>();
builder.Services.AddScoped<ICanchasServiceClient, CanchasServiceRestClient>();
builder.Services.AddScoped<IAuthServiceClient, AuthServiceRestClient>();
builder.Services.AddScoped<CanchaLibreWeb.Servicios.Seguridad.ControlIntentosLoginService>();
builder.Services.AddSingleton<CanchaLibreWeb.Servicios.Notificaciones.NotificacionService>();

//add more RestClient

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseAuthentication();
app.UseAuthorization();
//app.MapAuthEndpoints();

app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
