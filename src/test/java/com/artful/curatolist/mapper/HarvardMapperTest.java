package com.artful.curatolist.mapper;

import com.artful.curatolist.TestUtilityMethods;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.HarvardPage;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HarvardMapperTest {

    private final HarvardMapper harvardMapper = new HarvardMapper();

    @Test
    void mapHarvardArt() {
        HarvardPage mockHarvardPage = TestUtilityMethods.getMockHarvardPage();
        List<CLArtwork> expected = TestUtilityMethods.getMockMappedHarvardArt();

        List<CLArtwork> result = harvardMapper.mapHarvardArt(mockHarvardPage);

        assertThat(result).hasSize(expected.size());
        for (int i = 0; i < result.size(); i++) {
            CLArtwork actual = result.get(i);
            CLArtwork exp = expected.get(i);
            assertThat(actual).isEqualTo(exp);
        }
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