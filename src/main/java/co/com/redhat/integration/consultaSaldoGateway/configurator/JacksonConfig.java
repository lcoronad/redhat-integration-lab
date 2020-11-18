package co.com.redhat.integration.consultaSaldoGateway.configurator;

import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.redhat.integration.consultaSaldoGateway.model.ConsultaSaldoResponse;

@Configuration
public class JacksonConfig {
    
    @Bean("responseJackson")
    public JacksonDataFormat createJacksonFormat() {
    	JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setPrettyPrint(true);
        jacksonDataFormat.setUnmarshalType(ConsultaSaldoResponse.class);
        jacksonDataFormat.setEnableJaxbAnnotationModule(true);
	
        return jacksonDataFormat;
    }

}
