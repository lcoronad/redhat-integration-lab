package co.com.redhat.integration.consulta.saldo.gateway.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsultaSaldoResponse {
	
	@JsonProperty( value="saldo" )
	private BigDecimal saldo;
	@JsonProperty( value="fecha" )
	private Date fecha;
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
