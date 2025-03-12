package com.artful.curatolist.config;

import com.artful.curatolist.client.ChicagoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Bean
    @Qualifier("chicagoWebClientBuilder")
    public WebClient.Builder chicagoWebClientBuilder() {
        return WebClient.builder().baseUrl("https://api.artic.edu/api/v1");
    }

    @Bean
    @Qualifier("harvardWebClientBuilder")
    public WebClient.Builder harvardWebClientBuilder() {
        return WebClient.builder().baseUrl("https://api.harvardartmuseums.org");
    }
}
