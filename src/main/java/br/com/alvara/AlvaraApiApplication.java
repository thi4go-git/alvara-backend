package br.com.alvara;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class AlvaraApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AlvaraApiApplication.class, args);
    }

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/hello")
    public String hello() {
        return appName;
    }


}
