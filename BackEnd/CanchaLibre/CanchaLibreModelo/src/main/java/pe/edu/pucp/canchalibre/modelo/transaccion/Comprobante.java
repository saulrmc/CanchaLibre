package pe.edu.pucp.canchalibre.modelo.transaccion;

import pe.edu.pucp.canchalibre.modelo.Registro;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.time.LocalDateTime;

public class Comprobante extends Registro {
	private String serie;
	private String numero;
	private LocalDateTime fechaEmision;

	private double montoBloques;
	private static final double comisionPlataforma = 5.00;
	private double valorVenta;
	private static final double igv = 0.18;
	private Reserva reserva; //RF14
	private double montoTotal;

	public double getMontoTotal() {//montoTotal = montoBloques + (montoBloques * igv) + comisionPlataforma
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

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
}
