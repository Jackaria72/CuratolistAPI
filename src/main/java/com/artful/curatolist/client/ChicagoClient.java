package com.artful.curatolist.client;

import com.artful.curatolist.model.ChicagoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ChicagoClient {

    private final WebClient webClient;
    private final String BASE_URL = "https://api.artic.edu/api/v1";

    @Autowired
    public ChicagoClient(WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.build();
    }

    public Mono<ChicagoPage> getChicagoArtwork(int page, int limit) {

        return webClient.get().uri(uriBuilder -> uriBuilder
                .path("/artworks/search")
                .queryParam("query[term][is_public_domain]","true")
                .queryParam("fields", "id,title,artist_title,date_start,date_end,date_display,image_id,is_public_domain")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .build())
                .retrieve().bodyToMono(ChicagoPage.class);
    }
}
