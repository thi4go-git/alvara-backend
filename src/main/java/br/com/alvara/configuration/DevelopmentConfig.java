package br.com.alvara.configuration;

import br.com.alvara.anottation.DevelopmentAnottation;
import br.com.alvara.service.implementation.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@DevelopmentAnottation
public class DevelopmentConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Bean
    public CommandLineRunner executar() {
        return args -> {
            System.out.println("######### " + appName + " #########");
            System.out.println("######### EXECUTANDO AMBIENTE DE DESENVOLVIMENTO #########");
        };
    }


}

