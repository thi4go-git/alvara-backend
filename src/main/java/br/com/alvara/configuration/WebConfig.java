package br.com.alvara.configuration;

import org.springframework.core.Ordered;
import org.springframework.web.filter.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
        List<String> todos = Arrays.asList("*");
        //
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(todos);// libera para todo mundo
        corsConfiguration.setAllowedHeaders(todos);
        corsConfiguration.setAllowedMethods(todos);
        corsConfiguration.setAllowCredentials(true);
        //import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
        //import org.springframework.web.filter.*;
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        //
        CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean<CorsFilter> filter =
                new FilterRegistrationBean<>(corsFilter);
        //
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }
}
