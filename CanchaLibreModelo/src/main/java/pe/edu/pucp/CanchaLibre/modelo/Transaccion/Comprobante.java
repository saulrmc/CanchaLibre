package pe.edu.pucp.CanchaLibre.modelo.Transaccion;

import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;

import java.time.LocalDateTime;

public class Comprobante{
	private int idComprobante;
	private double igv;
	//private double precioHora; el precio de la Hora está contenido en un EsquemaPrecio
	//que tiene una Cancha, que asu vez tiene una Reserva
	private LocalDateTime fechaEmision;
	private Reserva reserva;

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

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
