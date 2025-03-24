package com.artful.curatolist.client;

import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.HarvardPage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class HarvardClient {
    private final WebClient webClient;
    private final String API_KEY;

    public HarvardClient(@Qualifier("harvardWebClientBuilder") WebClient.Builder webClientBuilder, @Value("${apikey.harvard}") String apiKey) {
        this.webClient = webClientBuilder.build();
        this.API_KEY = apiKey;
    }

    public Mono<HarvardPage> getHarvardArtwork(String uriExt) {
        String uri = UriComponentsBuilder.fromPath("/object")
                .queryParam("apikey", API_KEY)
                .queryParam("fields", "id,title,people,dated,description,medium,dimensions,classification,culture,technique,primaryimageurl")
                .queryParam("size", 100).toUriString();

        return webClient.get().uri(uri+uriExt)
                .retrieve().bodyToMono(HarvardPage.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResourcesNotFoundException("Unable to find results from Harvard"));
                    }
                    return Mono.error(new ExternalApiException("Failed to fetch Harvard artwork data"));
                });
    }
}
