package co.com.redhat.integration.consultaSaldoGateway.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Ruta para llamar al servicio de anular guias.
 * 
 * @since 24/09/2020
 * @author Lazaro Miguel Coronado Torres
 * @version 1.0
 */
@Component
public class ConsultaSaldoRoute extends RouteBuilder {

	private Logger logger = LoggerFactory.getLogger(ConsultaSaldoRoute.class);
	private static String MESSAGE_LOG = "Service: consulta-saldo-gateway | Route: ConsultaSaldoRoute | Message: ";
    
    @Override
    public void configure() throws Exception {
        onException(BeanValidationException.class)
        	.handled(true)
        	.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
        		.log(LoggingLevel.ERROR, logger, MESSAGE_LOG + "BeanValidationException description: ${exception.message}")
            .bean("errorResponse")
            .marshal().json(JsonLibrary.Jackson);

        onException(Exception.class)
            .handled(true)
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
        		.log(LoggingLevel.ERROR, logger, MESSAGE_LOG + "Exception description: ${exception.message}")
            .bean("errorResponse")
            .marshal().json(JsonLibrary.Jackson);
        
        //Ruta principal
        from("direct:consulta-saldo")
	        	.log(LoggingLevel.INFO, logger, MESSAGE_LOG + "Inicia ruta consulta-saldo")
		    //Convierte el JSON a objeto
	        .marshal().json(JsonLibrary.Jackson)
	        	.log(LoggingLevel.DEBUG, logger, MESSAGE_LOG + "URL del servicio a consumir: {{ws.endpoint.consultar-saldo}}")
	        //Consume el servicio de consultar saldo
	        .toD("{{ws.endpoint.consultar-saldo}}?httpClient.soTimeout={{ws.endpoint.timeout}}&bridgeEndpoint=true&throwExceptionOnFailure=false")
	        //Convierte el objeto response a JSON
	        .unmarshal().json(JsonLibrary.Jackson)
	        	.log(LoggingLevel.INFO, logger, MESSAGE_LOG + "Respuesta servicio consultar-saldo: ${body}")
        .end();
    }

}
