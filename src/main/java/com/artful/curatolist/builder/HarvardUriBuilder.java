package com.artful.curatolist.builder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class HarvardUriBuilder {

    public String buildHarvardUri(int page, String searchQuery, String sortTerm, String filters) {
        String uri = "&page="+page;
        if (filters != null) {
            String[] splitFilter = filters.split(":",2);
            String filterType = "";
            switch (splitFilter[0].toLowerCase()) {
                case "classification" -> filterType = "classification";
                case "technique" -> filterType = "technique";
                case "medium" -> filterType = "medium";
            }
            uri += "&q=imagepermissionlevel:0 AND "+filterType+":"+splitFilter[1];
        } else {
            uri += "&q=imagepermissionlevel:0";
        }
        if (searchQuery != null) {
            uri += "&keyword=" + searchQuery;
        }
        if (sortTerm == null || sortTerm.isBlank()) {
            uri += "&sort=id";
        } else {
            switch (sortTerm.toLowerCase()) {
                default ->  uri += "&sort=id";
                case "name" -> uri += "&sort=title.exact";
                case "classification" -> uri += "&sort=classification.exact";
            }
        }
        System.out.println("Harvard: "+uri);
        return uri;
    }
}
