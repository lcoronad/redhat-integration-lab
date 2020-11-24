package co.com.redhat.integration.consulta.saldo.gateway.configurator;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Configuracion para Swagger.
 * 
 * @since 24/09/2020
 * @author Lazaro Miguel Coronado Torres
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    @Controller
    class SwaggerWelcome {
        @RequestMapping("/swagger-ui")
        public String redirectToUi() {
            return "redirect:/webjars/swagger-ui/index.html?url=/api/api-doc&validatorUrl=";
        }
    }

}
