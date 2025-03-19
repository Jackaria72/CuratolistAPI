package com.artful.curatolist.builder;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class ChicagoUriBuilder {

    public String buildChicagoUri(int page, String searchQuery, String sortTerm, String filters) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromPath("/artworks/search")
                .queryParam("fields", "id,title,artist_title,date_start,date_end,date_display,image_id,is_public_domain,medium_display,dimensions,classification_title,place_of_origin,technique_titles")
                .queryParam("page", page)
                .queryParam("limit", 100)
                .queryParam("query[bool][must][term][is_public_domain]","true");
        if (filters != null) {
            String[] splitFilter = filters.split(":",2);
            uri.queryParam("query[bool][filter][match_phrase]["+splitFilter[0]+"]", splitFilter[1]);
        }
        if (searchQuery != null) {
            uri.queryParam("q",searchQuery);
        }
        if (sortTerm == null || sortTerm.isBlank()) {
            uri.queryParam("sort", "id");
        } else {
            switch (sortTerm.toLowerCase()) {
                default ->  uri.queryParam("sort", "id");
                case "title" -> uri.queryParam("sort", "title.keyword");
                case "classification" -> uri.queryParam("sort", "classification_title.keyword");
            }
        }
        return uri.toUriString();
    }
}
