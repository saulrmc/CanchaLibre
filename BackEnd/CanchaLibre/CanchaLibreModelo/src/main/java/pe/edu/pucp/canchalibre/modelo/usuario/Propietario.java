package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Persona;

public class Propietario extends Persona {
    private double calificacion;
    private String RUC;
    private double saldo;

    public double getCalificacion() {return calificacion;}
    public void setCalificacion(double calificacion) {this.calificacion = calificacion;}

    public String getRUC() {return RUC;}
    public void setRUC(String RUC) {this.RUC = RUC;}

    public double getSaldo() {return saldo;}
    public void setSaldo(double saldo) {this.saldo = saldo;}
}
