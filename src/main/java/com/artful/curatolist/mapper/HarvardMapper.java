package com.artful.curatolist.mapper;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.HarvardPage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HarvardMapper {

    public List<CLArtwork> mapHarvardArt(HarvardPage page) {
        if (page == null || page.records() == null || page.records().isEmpty()) {
            return Collections.emptyList();
        }
        return page.records().stream()
                .map(art -> new CLArtwork(
                        art.id()+"_HVD",
                        art.title() == null ? "Unknown" : art.title(),
                        extractArtist(art.people()),
                        art.dated() == null ? "Unknown" : art.dated(),
                        art.period() == null ? "Unknown" : art.period(),
                        art.medium() == null ? "Unknown" : art.medium(),
                        art.technique() == null ? "unknown" : art.technique(),
                        art.classification() == null ? "Unknown" : art.classification(),
                        art.culture() == null ? "unknown" : art.culture(),
                        art.dimensions() == null ? "Unknown" : art.dimensions(),
                        art.primaryimageurl(),
                        "Harvard"
                )).collect(Collectors.toList());
    }

    public String extractArtist(List<HarvardPage.Person> people) {
        if (people == null || people.isEmpty() ||
                people.getFirst().name() == null || people.getFirst().role() == null) {
            return "Unknown Artist";
        } else if (people.getFirst().role().equalsIgnoreCase("Artist")) {
            return people.getFirst().name();
        }

        return "Unknown Artist";
    }

}
