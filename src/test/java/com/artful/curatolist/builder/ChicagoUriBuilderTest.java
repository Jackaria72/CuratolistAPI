package com.artful.curatolist.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChicagoUriBuilderTest {

    private final ChicagoUriBuilder uriBuilder = new ChicagoUriBuilder();

    @Test
    void testBuildChicagoUriBuildsCorrectUri() {
        String expected1 = "/artworks/search?query%5Bterm%5D%5Bis_public_domain%5D=true&fields=id,title,artist_title,date_start,date_end,date_display,image_id,is_public_domain,medium_display,dimensions,classification_title,place_of_origin,technique_titles&page=1&limit=100&sort=id";
        String expected2 = "/artworks/search?query%5Bterm%5D%5Bis_public_domain%5D=true&fields=id,title,artist_title,date_start,date_end,date_display,image_id,is_public_domain,medium_display,dimensions,classification_title,place_of_origin,technique_titles&page=7&limit=100&q=cat&sort=classification_title.keyword";

        String results1 = uriBuilder.buildChicagoUri(1, null, null);
        String results2 = uriBuilder.buildChicagoUri(7, "cat", "classification");

        assertEquals(expected1,results1);
        assertEquals(expected2,results2);
    }
}