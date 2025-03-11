package model;

import java.util.List;

public record ChicagoPage(PageInfo pagination, List<ChicagoArt> data) {
    public record PageInfo(
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
