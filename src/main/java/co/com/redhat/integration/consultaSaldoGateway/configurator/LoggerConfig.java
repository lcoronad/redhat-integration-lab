package co.com.redhat.integration.consultaSaldoGateway.configurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Logger.
 * 
 * @since 24/09/2020
 * @author Lazaro Miguel Coronado Torres
 * @version 1.0
 */
@Configuration
public class LoggerConfig {
    
    @Value("${camel.springboot.name}")
    private String loggerName;
    
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(loggerName);
    }

}
