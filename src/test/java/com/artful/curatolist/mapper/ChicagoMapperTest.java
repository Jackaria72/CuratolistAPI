package com.artful.curatolist.mapper;

import com.artful.curatolist.TestUtilityMethods;
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
        ChicagoPage mockChicagoPage = TestUtilityMethods.getMockChicagoPage();
        List<CLArtwork> expected = TestUtilityMethods.getMockMappedChicagoArt();

        List<CLArtwork> result = chicagoMapper.mapChicagoArt(mockChicagoPage);

        assertThat(result).hasSize(expected.size());
        for (int i = 0; i < result.size(); i++) {
            CLArtwork actual = result.get(i);
            CLArtwork exp = expected.get(i);
            assertThat(actual).isEqualTo(exp);
        }
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