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

    @Test
    void testExtractArtistCorrectlyExtractsArtist() {
        List<HarvardPage.Person> person1 = List.of(new HarvardPage.Person("Bob Ross", "Artist"));
        List<HarvardPage.Person> person2 = List.of(new HarvardPage.Person("Michael", "Coin Constituent"));
        List<HarvardPage.Person> person3 = List.of(new HarvardPage.Person("John", null));
        List<HarvardPage.Person> person4 = List.of(new HarvardPage.Person(null, null));
        List<HarvardPage.Person> person5 = null;

        String result1 = harvardMapper.extractArtist(person1);
        String result2 = harvardMapper.extractArtist(person2);
        String result3 = harvardMapper.extractArtist(person3);
        String result4 = harvardMapper.extractArtist(person4);
        String result5 = harvardMapper.extractArtist(person5);

        assertThat(result1).isEqualTo("Bob Ross");
        assertThat(result2).isEqualTo("Unknown Artist");
        assertThat(result3).isEqualTo("Unknown Artist");
        assertThat(result4).isEqualTo("Unknown Artist");
        assertThat(result5).isEqualTo("Unknown Artist");
    }
}