package com.artful.curatolist.config;

import com.artful.curatolist.client.ChicagoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Bean
    public WebClient.Builder chicagoWebClientBuilder() {
        return WebClient.builder().baseUrl("https://api.artic.edu/api/v1");
    }
}
