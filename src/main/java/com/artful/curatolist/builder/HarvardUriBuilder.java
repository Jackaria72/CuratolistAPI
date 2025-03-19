package com.artful.curatolist.builder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class HarvardUriBuilder {
    private final String API_KEY;

    public HarvardUriBuilder(@Value("${apikey.harvard}") String apiKey) {
        this.API_KEY = apiKey;
    }

    public String buildHarvardUri(int page, String searchQuery, String sortTerm, String filters) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromPath("/object")
                .queryParam("apikey", API_KEY)
                .queryParam("fields", "id,title,people,dated,period,medium,dimensions,classification,culture,technique,primaryimageurl")
                .queryParam("page", page)
                .queryParam("size", 100);
        if (filters != null) {
            uri.queryParam("q", "imagepermissionlevel:0 AND "+ filters);
        } else {
            uri.queryParam("q", "imagepermissionlevel:0");
        }
        if (searchQuery != null) {
            uri.queryParam("keyword", searchQuery);
        }
        if (sortTerm == null || sortTerm.isBlank()) {
            uri.queryParam("sort", "id");
        } else {
            switch (sortTerm.toLowerCase()) {
                default ->  uri.queryParam("sort", "id");
                case "title" -> uri.queryParam("sort", "title.exact");
                case "classification" -> uri.queryParam("sort", "classification.exact");
            }
        }
        return uri.toUriString();
    }
}
