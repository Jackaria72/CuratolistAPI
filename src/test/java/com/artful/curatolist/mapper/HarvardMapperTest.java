package com.artful.curatolist.mapper;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.HarvardPage;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HarvardMapperTest {

    private final HarvardMapper harvardMapper = new HarvardMapper();

    @Test
    void mapHarvardArt() {
        HarvardPage.HarvardPageInfo pageInfo = new HarvardPage.HarvardPageInfo(2,1);
        HarvardPage.HarvardArt art1 = new HarvardPage.HarvardArt(1,"Test Art 1", List.of(new HarvardPage.Person("Test Artist 1")), "1800", "1800", "Test URL 1");
        HarvardPage.HarvardArt art2 = new HarvardPage.HarvardArt(2,"Test Art 2", List.of(new HarvardPage.Person("Test Artist 2")), "1900", "1900", "Test URL 2");
        HarvardPage mockHarvardPage = new HarvardPage(pageInfo, List.of(art1, art2));

        List<CLArtwork> result = harvardMapper.mapHarvardArt(mockHarvardPage);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst())
                .extracting(CLArtwork::id, CLArtwork::title, CLArtwork::artist, CLArtwork::date, CLArtwork::period, CLArtwork::imageUrl, CLArtwork::source)
                .containsExactly("HVD1", "Test Art 1", "Test Artist 1" , "1800", "1800", "Test URL 1", "Harvard");

        assertThat(result.getLast())
                .extracting(CLArtwork::id, CLArtwork::title, CLArtwork::artist, CLArtwork::date, CLArtwork::period, CLArtwork::imageUrl, CLArtwork::source)
                .containsExactly("HVD2", "Test Art 2", "Test Artist 2" , "1900", "1900", "Test URL 2", "Harvard");

    }

    @Test
    void testMapHarvardArtReturnsEmptyListWhenNoResults() {
        HarvardPage.HarvardPageInfo pageInfo = new HarvardPage.HarvardPageInfo(2,1);
        List<HarvardPage.HarvardArt> harvardArt = Collections.emptyList();
        HarvardPage mockEmptyListHarvardPage = new HarvardPage(pageInfo, harvardArt);
        HarvardPage mockNullHarvardPage = new HarvardPage(null, null);

        List<CLArtwork> nullResult = harvardMapper.mapHarvardArt(mockNullHarvardPage);
        List<CLArtwork> emptyListResult = harvardMapper.mapHarvardArt(mockEmptyListHarvardPage);

        assertThat(nullResult).isEmpty();
        assertThat(emptyListResult).isEmpty();
    }
}