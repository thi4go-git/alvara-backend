package br.com.alvara;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AlvaraApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("ERROR: Args null ao Startar!!");
            System.exit(1);
        }
        SpringApplication.run(AlvaraApiApplication.class, args);
    }
}
