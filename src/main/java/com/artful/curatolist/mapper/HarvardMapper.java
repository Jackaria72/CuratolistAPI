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
                        "HVD"+art.id(),
                        art.title() == null ? "Unknown" : art.title(),
                        art.people() == null || art.people().isEmpty() ? "Unknown" : art.people().getFirst().name(),
                        art.dated() == null ? "Unknown" : art.dated(),
                        art.period() == null ? "Unknown" : art.period(),
                        art.primaryimageurl(),
                        "Harvard"
                )).collect(Collectors.toList());
    }
}
