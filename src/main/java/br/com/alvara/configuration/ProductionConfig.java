package br.com.alvara.configuration;

import br.com.alvara.anottation.ProductionAnottation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@ProductionAnottation
public class ProductionConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public CommandLineRunner executar() {
        return args -> {
            System.out.println("######### " + appName + " #########");
            System.out.println("######### EXECUTANDO AMBIENTE DE PROCUÇÃO #########");
        };
    }
}
