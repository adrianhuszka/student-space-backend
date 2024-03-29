package hu.StudentSpace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Component
@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI gateWayOpenApi() {
        return new OpenAPI().info(new Info().title("StudentSpace API Gateway")
                .description("Documentation for the api, generated by SpringDoc OpenAPI")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("Adrián Gábor Huszka")
                        .email("adrian.huszka@gmail.com")));
    }
}
