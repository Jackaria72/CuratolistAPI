package com.artful.curatolist.client;

import com.artful.curatolist.model.HarvardPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HarvardClient {
    private final WebClient webClient;
    private final String BASE_URL = "";
    private final String API_KEY;

    public HarvardClient(WebClient.Builder webClientBuilder,
                         @Value("${apikey.harvard}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.API_KEY = apiKey;
    }

    public Mono<HarvardPage> getHarvardArtwork(int page, int limit) {

        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/object")
                        .queryParam("apikey",API_KEY)
                        .queryParam("q", "imagepermissionlevel:0")
                        .queryParam("fields", "id,title,people,dated,period,primaryimageurl")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve().bodyToMono(HarvardPage.class);
    }
}
