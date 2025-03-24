package com.artful.curatolist.client;

import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.ChicagoPage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class ChicagoClient {

    private final WebClient webClient;

    public ChicagoClient(@Qualifier("chicagoWebClientBuilder")WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.build();
    }

    public Mono<ChicagoPage> getChicagoArtwork(String uriExt) {
        String uri = UriComponentsBuilder.fromPath("/artworks/search")
                .queryParam("fields", "id,title,artist_title,date_start,date_end,description,image_id,is_public_domain,medium_display,dimensions,classification_title,place_of_origin,technique_titles")
                .queryParam("limit", 100)
                .toUriString();

        return webClient.get().uri(uri+uriExt)
                .retrieve().bodyToMono(ChicagoPage.class).onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResourcesNotFoundException("Unable to find results from Chicago"));
                    }
                    return Mono.error(new ExternalApiException("Failed to fetch Chicago artwork data"));
                });
    }
}
