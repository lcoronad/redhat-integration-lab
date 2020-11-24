package co.com.redhat.integration.consulta.saldo.gateway.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import co.com.redhat.integration.consulta.saldo.gateway.model.ConsultaSaldoRequest;
import co.com.redhat.integration.consulta.saldo.gateway.model.ConsultaSaldoResponse;


/**
 * Ruta principal del servicio que expone los diferentes metodos.
 * 
 * @since 24/09/2020
 * @author Lazaro Miguel Coronado Torres
 * @version 1.0
 */
@Component
public class RestMainRoute extends RouteBuilder {

    @Value("${service.rest.context-path}")
    private String contextPath;

    @Autowired
    private Environment env;

    @Override
    public void configure() throws Exception {
		onException(BeanValidationException.class)
		    .handled(true)
		    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
			.bean("errorResponse")
			.marshal()
			.json(JsonLibrary.Jackson);
	
		onException(Exception.class)
		    .handled(true)
		    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
		    .bean("errorResponse")
		    .marshal()
		    .json(JsonLibrary.Jackson);
				
		restConfiguration()
			.component("servlet")
			.bindingMode(RestBindingMode.json)
		    .dataFormatProperty("prettyPrint", "true")
		    .enableCORS(true)
		    .contextPath(contextPath)
		    .apiContextPath("/api-doc")
		    .apiProperty("api.title", env.getProperty("api.title"))
		    .apiProperty("api.version", env.getProperty("api.version"));
	
		rest(env.getProperty("service.rest.uri"))
			.description(env.getProperty("service.rest.description"))
		    .consumes("application/json")
		    .produces("application/json")
		
		//Metodo post para consulta saldo
	    .post(env.getProperty("service.rest.consulta.saldo.uri"))
	    	.description(env.getProperty("service.rest.consulta.saldo.description"))
    	    .type(ConsultaSaldoRequest.class)
    	    .outType(ConsultaSaldoResponse.class)
	    	.description(env.getProperty("service.rest.consulta.saldo.description"))
	    	.responseMessage()
    	    	.code(200)
    	    .endResponseMessage()
    	    .to("direct:consulta-saldo")
        
        //Metodo get para el health check
        .get(env.getProperty("service.rest.health.uri"))
            .description(env.getProperty("service.rest.health.description"))
            .to("direct:health");
    }
}
