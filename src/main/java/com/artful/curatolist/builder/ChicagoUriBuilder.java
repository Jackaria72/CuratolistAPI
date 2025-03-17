package com.artful.curatolist.builder;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ChicagoUriBuilder {

    public String buildChicagoUri(int page, String searchQuery) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromPath("/artworks/search")
                .queryParam("query[term][is_public_domain]","true")
                .queryParam("fields", "id,title,artist_title,date_start,date_end,date_display,image_id,is_public_domain,medium_display,dimensions,classification_title,place_of_origin,technique_titles")
                .queryParam("page", page)
                .queryParam("limit", 100);
        if (searchQuery != null) {
            uri.queryParam("q",searchQuery);
        }

        return uri.toUriString();
    }
}
