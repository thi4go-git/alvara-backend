package br.com.alvara.configuration;

import br.com.alvara.anottation.ProductionAnottation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@ProductionAnottation
public class ProductionConfig {

    @Value("${spring.application.name}")
    private String appName;

    private static final Logger LOG = LoggerFactory.getLogger(ProductionConfig.class);


    @Bean
    public CommandLineRunner executar() {
        return args -> {
            LOG.info("######### " + appName + " #########");
            LOG.info("######### EXECUTANDO AMBIENTE DE PROCUÇÃO #########");
        };
    }
}
