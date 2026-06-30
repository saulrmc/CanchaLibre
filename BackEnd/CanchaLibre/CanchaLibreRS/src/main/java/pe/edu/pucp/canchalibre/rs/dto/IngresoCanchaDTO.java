package pe.edu.pucp.canchalibre.rs.dto;

public class IngresoCanchaDTO {
    private int id;
    private String nombre;
    private double ingresos;
    private int reservas;
    private int horasOcupacion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getIngresos() { return ingresos; }
    public void setIngresos(double ingresos) { this.ingresos = ingresos; }
    public int getReservas() { return reservas; }
    public void setReservas(int reservas) { this.reservas = reservas; }
    public int getHorasOcupacion() { return horasOcupacion; }
    public void setHorasOcupacion(int horasOcupacion) { this.horasOcupacion = horasOcupacion; }
}
