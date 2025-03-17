package com.artful.curatolist.client;

import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.HarvardPage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class HarvardClient {
    private final WebClient webClient;

    public HarvardClient(@Qualifier("harvardWebClientBuilder") WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<HarvardPage> getHarvardArtwork(String uri) {

        return webClient.get().uri(uri)
                .retrieve().bodyToMono(HarvardPage.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResourcesNotFoundException("Unable to find results from Harvard"));
                    }
                    return Mono.error(new ExternalApiException("Failed to fetch Harvard artwork data"));
                });
    }
}
