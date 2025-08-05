package br.com.alvara.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;



@Configuration
@OpenAPIDefinition(
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Value("${spring.info.app.version}")
    private String versaoSistema;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Alvarás WEB -")
                        .description("API Para manipular Alvarás")
                        .contact(new Contact()
                                .name("Thiago Junior")
                                .email("thi4go19@gmail.com")
                                .url("https://www.linkedin.com/in/thiago-amorim-melo/")
                        )
                        .version(versaoSistema)
        );
    }

}
