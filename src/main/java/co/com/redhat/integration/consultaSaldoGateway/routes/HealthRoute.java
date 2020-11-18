package co.com.redhat.integration.consultaSaldoGateway.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ruta de health check.
 * 
 * @since 24/09/2020
 * @author Lazaro Miguel Coronado Torres
 * @version 1.0
 */
@Component
public class HealthRoute extends RouteBuilder {

	private Logger logger = LoggerFactory.getLogger(HealthRoute.class);
	private static String MENSSAGE_LOG = "Service: consulta-saldo-gateway | Route: HealthRoute | Message: ";

	@Autowired
	private CamelContext camelContext;

	@Override
	public void configure() throws Exception {

		camelContext.setUseMDCLogging(Boolean.TRUE);

		from("direct:health")
			.id("health")
			.streamCaching("true")
			.setBody().constant("Status Service: OK")
			.log(LoggingLevel.DEBUG, logger, MENSSAGE_LOG + "Response Health Check: ${body}")
		.end();
	}

}
