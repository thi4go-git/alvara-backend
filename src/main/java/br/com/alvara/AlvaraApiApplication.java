package br.com.alvara;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@Slf4j
@SpringBootApplication
public class AlvaraApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        // Carregar valores do arquivo .env
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        log.info("Informação: {}", "Aplicação RODANDO!");
        SpringApplication.run(AlvaraApiApplication.class, args);
    }
}
