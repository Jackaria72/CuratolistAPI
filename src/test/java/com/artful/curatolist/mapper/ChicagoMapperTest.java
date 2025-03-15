package com.artful.curatolist.mapper;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.ChicagoPage;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChicagoMapperTest {

    private final ChicagoMapper chicagoMapper = new ChicagoMapper();

    @Test
    void mapChicagoArt() {
        ChicagoPage.ChicagoPageInfo pageInfo = new ChicagoPage.ChicagoPageInfo(2,1);
        ChicagoPage.ChicagoArt art1 = new ChicagoPage.ChicagoArt(1,"Test Art 1","Test Artist 1", 1800,1800,"1800", "Test ID 1");
        ChicagoPage.ChicagoArt art2 = new ChicagoPage.ChicagoArt(2,"Test Art 2","Test Artist 2", 1900,1900,"1900", "Test ID 2");
        ChicagoPage mockChicagoPage = new ChicagoPage(pageInfo, List.of(art1, art2));

        List<CLArtwork> result = chicagoMapper.mapChicagoArt(mockChicagoPage);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst())
                .extracting(CLArtwork::id, CLArtwork::title, CLArtwork::artist, CLArtwork::date, CLArtwork::period, CLArtwork::imageUrl, CLArtwork::source)
                .containsExactly("AIC1", "Test Art 1", "Test Artist 1" , "1800", "1800", "Test ID 1", "Art Institute of Chicago");

        assertThat(result.getLast())
                .extracting(CLArtwork::id, CLArtwork::title, CLArtwork::artist, CLArtwork::date, CLArtwork::period, CLArtwork::imageUrl, CLArtwork::source)
                .containsExactly("AIC2", "Test Art 2", "Test Artist 2" , "1900", "1900", "Test ID 2", "Art Institute of Chicago");

    }

    @Test
    void testMapChicagoArtReturnsEmptyListWhenNoResults() {
        ChicagoPage.ChicagoPageInfo pageInfo = new ChicagoPage.ChicagoPageInfo(2,1);
        List<ChicagoPage.ChicagoArt> chicagoArt = Collections.emptyList();
        ChicagoPage mockEmptyListChicagoPage = new ChicagoPage(pageInfo, chicagoArt);
        ChicagoPage mockNullChicagoPage = new ChicagoPage(null, null);

        List<CLArtwork> nullResult = chicagoMapper.mapChicagoArt(mockNullChicagoPage);
        List<CLArtwork> emptyListResult = chicagoMapper.mapChicagoArt(mockEmptyListChicagoPage);

        assertThat(nullResult).isEmpty();
        assertThat(emptyListResult).isEmpty();
    }
}