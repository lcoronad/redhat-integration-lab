package co.com.redhat.integration.consultaSaldoGateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsultaSaldoRequest {
	
	@JsonProperty( value="numeroCuenta" )
	private String numeroCuenta;

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
}
