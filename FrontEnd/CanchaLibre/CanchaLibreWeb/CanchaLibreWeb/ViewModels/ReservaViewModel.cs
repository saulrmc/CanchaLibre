using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class ReservaViewModel
{
    public int idReserva {get; set;}
    public EstadoReservaEnum estado {get; set;}

    [Required(ErrorMessage = "Debe tener un cliente")]
    public ClienteViewModel? cliente {get; set;}

    [Required(ErrorMessage = "Debe tener una cancha")]
    public CanchaViewModel? cancha {get; set;}

    [Required(ErrorMessage = "Debe tener un metodo de pago")]
    public PagoViewModel? pago {get; set;}
    public List<BloqueHorarioViewModel>? bloques {get; set;}
    public DateTime? fecha {get; set;}

/*
    public String getResumenBloquesTexto(Reserva reserva) {
            if (reserva == null || reserva.getBloques() == null || reserva.getBloques().isEmpty()) {
                return "No hay bloques seleccionados";
            }

            // Mapa para agrupar: Clave = Precio (Double), Valor = Cantidad de bloques con ese precio (Integer)
            java.util.Map<Double, Integer> conteoPrecios = new java.util.HashMap<>();

            // Contamos cuántos bloques hay de cada precio
            for (BloqueHorario bloque : reserva.getBloques()) {
                double precio = bloque.getPrecio();
                conteoPrecios.put(precio, conteoPrecios.getOrDefault(precio, 0) + 1);
            }

            // Construimos la cadena final (Ej: "2h x S/. 50.00, 1h x S/. 80.00")
            java.util.List<String> partes = new java.util.ArrayList<>();
            for (java.util.Map.Entry<Double, Integer> entrada : conteoPrecios.entrySet()) {
                partes.add(String.format("%dh x S/. %.2f", entrada.getValue(), entrada.getKey()));
            }

            // Unimos los elementos con comas
            return String.join(", ", partes);
*/
}