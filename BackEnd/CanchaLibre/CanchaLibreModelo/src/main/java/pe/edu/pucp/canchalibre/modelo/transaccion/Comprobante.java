package pe.edu.pucp.canchalibre.modelo.transaccion;

import java.time.LocalDateTime;

public class Comprobante {
	private int idComprobante;
	private String serie;
	private String numero;
	private LocalDateTime fechaEmision;

	private double montoBloques; //subtotal canchas
	private static final double COMISION_PLATAFORMA = 5.00;
	private double valorVenta; //montoBloques+comision / 1.18 (precio ya incluye igv 18%)
	private double montoIgv; //montoBloques+comision - valorVenta

	public double getMontoIgv() {return montoIgv;}
	public void setMontoIgv(double montoIgv) {this.montoIgv = montoIgv;}

	public String getSerie() {return serie;}
	public void setSerie(String serie) {this.serie = serie;}

	public String getNumero() {return numero;}
	public void setNumero(String numero) {this.numero = numero;}

	public double getMontoBloques() {return montoBloques;}
	public void setMontoBloques(double montoBloques) {this.montoBloques = montoBloques;}

	public double getValorVenta() {return valorVenta;}
	public void setValorVenta(double valorVenta) {this.valorVenta = valorVenta;}

	public LocalDateTime getFechaEmision() {return fechaEmision;}
	public void setFechaEmision(LocalDateTime fechaEmision) {this.fechaEmision = fechaEmision;}

	public double getComisionPlataforma(){return COMISION_PLATAFORMA;}

	public int getIdComprobante() {
		return idComprobante;
	}
	public void setIdComprobante(int idComprobante) {
		this.idComprobante = idComprobante;
	}

}
