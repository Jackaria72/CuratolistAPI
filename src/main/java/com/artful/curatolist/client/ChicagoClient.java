package com.artful.curatolist.client;

import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.ChicagoPage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class ChicagoClient {

    private final WebClient webClient;
    private final String BASE_URL = "https://api.artic.edu/api/v1";

    public ChicagoClient(@Qualifier("chicagoWebClientBuilder")WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.build();
    }

    public Mono<ChicagoPage> getChicagoArtwork(int page) {

        return webClient.get().uri(uriBuilder -> uriBuilder
                .path("/artworks/search")
                .queryParam("query[term][is_public_domain]","true")
                .queryParam("fields", "id,title,artist_title,date_start,date_end,date_display,image_id,is_public_domain")
                .queryParam("page", page)
                .queryParam("limit", 100)
                .build())
                .retrieve().bodyToMono(ChicagoPage.class).onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResourcesNotFoundException("Unable to find results from Chicago"));
                    }
                    return Mono.error(new ExternalApiException("Failed to fetch Chicago artwork data"));
                });
    }
}
