package tech.brito.apiresttexoit.core;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI() {
        var info = new Info()
                .title("TEXO IT API REST")
                .version("v1")
                .description("Rest API dos vencedores da categoria Pior Filme do Golden Raspberry Awards.");

        return new OpenAPI().info(info);
    }
}
