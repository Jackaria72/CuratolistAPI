package com.artful.curatolist.model;

import java.util.List;

public record ChicagoPage(ChicagoPageInfo pagination, List<ChicagoArt> data) {
    public record ChicagoPageInfo(
            int total,
            int total_pages
    ){}
    public record ChicagoArt(
            int id,
            String title,
            String artist_title,
            int date_start,
            int date_end,
            String date_display,
            String medium_display,
            List<String> technique_titles,
            String classification_title,
            String place_of_origin,
            String dimensions,
            String image_id,
            Boolean is_public_domain
    ){}
}
