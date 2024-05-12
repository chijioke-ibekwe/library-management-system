package dev.chijiokeibekwe.librarymanagementsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.host}")
    private String host;

    @Bean
    public OpenAPI definition() {
        Server server = new Server();
        server.setUrl(this.host);
        server.setDescription("Development");

        Contact contact = new Contact();
        contact.setName("Chijioke Ibekwe");
        contact.setEmail("ibekwe.chijioke18@gmail.com");

        return new OpenAPI()
                .info(new Info()
                        .title("Library Management Web Service APIs")
                        .version("1.0.0")
                        .description("APIs exposing the endpoints to the library management backend service")
                        .contact(contact)
                )
                .servers(List.of(server));
    }
}
