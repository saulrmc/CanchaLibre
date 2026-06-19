package pe.edu.pucp.canchalibre.modelo.transaccion;

import pe.edu.pucp.canchalibre.modelo.cancha.Etiqueta;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Comprobante{
	private int idComprobante;
	private String serie;
	private String numero;
	private LocalDateTime fechaEmision;

	private double montoBloques;
	private final double comisionPlataforma = 5.00;
	private double valorVenta;
	private double igv;

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

	public double getComisionPlataforma(){return comisionPlataforma;};

	//private double precioHora; el precio de la Hora está contenido en un EsquemaPrecio
	//que tiene una Cancha, que asu vez tiene una Reserva
//	public double getPrecioHora() {
//		return precioHora;
//	}
//
//	public void setPrecioHora(double precioHora) {
//		this.precioHora = precioHora;
//	}

	public double getIgv() {
		return igv;
	}
	public void setIgv(double igv) {
		this.igv = igv;
	}

	public int getIdComprobante() {
		return idComprobante;
	}
	public void setIdComprobante(int idComprobante) {
		this.idComprobante = idComprobante;
	}
}
