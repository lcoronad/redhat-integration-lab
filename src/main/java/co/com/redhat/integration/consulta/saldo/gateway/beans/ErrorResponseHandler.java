package co.com.redhat.integration.consulta.saldo.gateway.beans;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.springframework.stereotype.Component;

import co.com.redhat.integration.consulta.saldo.gateway.model.ErrorResponse;

@Component("errorResponse")
public class ErrorResponseHandler {

	@Handler
	public ErrorResponse handler(Exchange exchange) {
		Object exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);

		ErrorResponse errorResponse = new ErrorResponse();

		if (exception instanceof BeanValidationException) {
			BeanValidationException beanValidationException = (BeanValidationException) exception;
			Set<ConstraintViolation<Object>> listErrors = beanValidationException.getConstraintViolations();
			
			Iterator<ConstraintViolation<Object>> listError = listErrors.iterator();
			while (listError.hasNext()) {
				ConstraintViolation<Object> constraint = listError.next();
				String message = constraint.getPropertyPath() + " - " + constraint.getMessage();
				errorResponse.setCodigo("400");
				errorResponse.setDescripcion(message);
			}
		}
		else if (exception instanceof NullPointerException) {
			NullPointerException nullPointerException = (NullPointerException) exception;
			String mensajeError = nullPointerException.getMessage();
			errorResponse.setCodigo("400");
			errorResponse.setDescripcion(mensajeError);
		}
		else if (exception instanceof SecurityException) {
			SecurityException securityException = (SecurityException) exception;
			String mensajeError = securityException.getMessage();
			errorResponse.setCodigo("400");
			errorResponse.setDescripcion(mensajeError);
		}
		else {
			Exception exc = (Exception) exception;
			errorResponse.setCodigo("400");
			errorResponse.setDescripcion(exc.getMessage());
		}

		return errorResponse;
	}
}
