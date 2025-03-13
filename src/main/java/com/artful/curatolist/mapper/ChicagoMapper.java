package com.artful.curatolist.mapper;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.ChicagoPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChicagoMapper {

    public List<CLArtwork> mapChicagoArt(ChicagoPage page) {
        return page.data().stream()
                .map(art -> new CLArtwork(
                        "AIC"+String.valueOf(art.id()),
                        art.title(),
                        art.artist_title(),
                        String.valueOf(art.date_start())+" - "+String.valueOf(art.date_end()),
                        art.date_display(),
                        art.image_id(),
                        "Art Institute of Chicago"
                )).collect(Collectors.toList());
    }
}
