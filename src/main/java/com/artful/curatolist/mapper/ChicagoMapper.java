package com.artful.curatolist.mapper;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.ChicagoPage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChicagoMapper {

    public List<CLArtwork> mapChicagoArt(ChicagoPage page) {
        if (page == null || page.data() == null || page.data().isEmpty()) {
            return Collections.emptyList();
        }
        return page.data().stream()
                .map(art -> new CLArtwork(
                        art.id()+"_AIC",
                        art.title() == null ? "Unknown" : art.title(),
                        art.artist_title() == null ? "Unknown" : art.artist_title(),
                        String.valueOf(art.date_end()),
                        art.date_display() == null ? "Unknown" : art.date_display(),
                        art.medium_display() == null ? "Unknown" : art.medium_display(),
                        art.technique_titles() != null && !art.technique_titles().isEmpty()? art.technique_titles().getFirst() : "Unknown",
                        art.classification_title() == null ? "Unknown" : art.classification_title(),
                        art.place_of_origin() == null ? "Unknown" : art.place_of_origin(),
                        art.dimensions() == null ? "Unknown" : art.dimensions(),
                        art.image_id() == null ? "No URL available" : "https://www.artic.edu/iiif/2/".concat(art.image_id()).concat("/full/843,/0/default.jpg"),
                        "Art Institute of Chicago"
                )).collect(Collectors.toList());
    }
}
