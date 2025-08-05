package br.com.alvara;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@SpringBootApplication
@RestController
public class AlvaraApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        log.info("Informação: {}", "Aplicação RODANDO!");
        SpringApplication.run(AlvaraApiApplication.class, args);
    }
}
