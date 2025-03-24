package com.artful.curatolist.builder;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class ChicagoUriBuilder {

    public String buildChicagoUri(int page, String searchQuery, String sortTerm, String filters) {

        String uri = "&page="+page;

        if (filters != null) {
            String[] splitFilter = filters.split(":",2);
            String filterType = "";
            switch (splitFilter[0]) {
                case "Classification" -> filterType = "classification_title";
                case "Technique" -> filterType = "technique_titles";
                case "Medium" -> filterType = "medium_display";
            }
            String filterQuery = "query[bool][must][term][is_public_domain]=true&query[bool][filter][match_phrase][" + filterType + "]=" + splitFilter[1];
            uri += ("&" + filterQuery);
        } else {
            // Manually add query parameter with square brackets (without encoding)
            String filterQuery = "query[bool][must][term][is_public_domain]=true";
            uri += ("&" + filterQuery);
        }
        if (searchQuery != null) {
            uri += "&q=" + searchQuery;
        }
        if (sortTerm == null || sortTerm.isBlank()) {
            uri += "&sort="+"id";
        } else {
            switch (sortTerm.toLowerCase()) {
                default ->  uri += "&sort=id";
                case "title" -> uri += "&sort=title.keyword";
                case "classification" -> uri += "&sort=classification_title.keyword";
            }
        }
        System.out.println("Chicago: "+uri);
        return uri;
    }
}
