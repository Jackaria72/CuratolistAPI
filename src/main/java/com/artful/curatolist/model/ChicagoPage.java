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
            String image_id
    ){}
}
