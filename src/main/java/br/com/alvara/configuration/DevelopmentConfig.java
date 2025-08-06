package br.com.alvara.configuration;

import br.com.alvara.anottation.DevelopmentAnottation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@DevelopmentAnottation
public class DevelopmentConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DevelopmentConfig.class);

    @Bean
    public CommandLineRunner executar() {
        return args -> LOG.info("::: EXECUTANDO AMBIENTE DE DESENVOLVIMENTO :::");
    }


}

