package com.artful.curatolist;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.ChicagoPage;
import com.artful.curatolist.model.HarvardPage;

import java.util.List;
import java.util.stream.Stream;

public class TestUtilityMethods {

    public static HarvardPage getMockHarvardPage() {
        return new HarvardPage(new HarvardPage.HarvardPageInfo(5,1),
                List.of(new HarvardPage.HarvardArt(1,"Test Title 1", List.of(new HarvardPage.Person("Test Artist 1")), "1800", "1800", "Test URL 1"),
                        new HarvardPage.HarvardArt(2,"Test Title 2", List.of(new HarvardPage.Person("Test Artist 2")), "1850", "1850", "Test URL 2"),
                        new HarvardPage.HarvardArt(3,"Test Title 3", List.of(new HarvardPage.Person("Test Artist 3")), "1900", "1900", "Test URL 3"),
                        new HarvardPage.HarvardArt(4,"Test Title 4", List.of(new HarvardPage.Person("Test Artist 4")), "1950", "1950", "Test URL 4"),
                        new HarvardPage.HarvardArt(5,"Test Title 5", List.of(new HarvardPage.Person("Test Artist 5")), "2000", "2000", "Test URL 5")
));
    }

    public static ChicagoPage getMockChicagoPage() {
        return new ChicagoPage(new ChicagoPage.ChicagoPageInfo(5,1),
                List.of(new ChicagoPage.ChicagoArt(1,"Test Title 1","Test Artist 1", 1800,1800,"1800", "Test ID 1"),
                        new ChicagoPage.ChicagoArt(2,"Test Title 2","Test Artist 2", 1850,1850,"1850", "Test ID 2"),
                        new ChicagoPage.ChicagoArt(3,"Test Title 3","Test Artist 3", 1900,1900,"1900", "Test ID 3"),
                        new ChicagoPage.ChicagoArt(4,"Test Title 4","Test Artist 4", 1950,1950,"1950", "Test ID 4"),
                        new ChicagoPage.ChicagoArt(5,"Test Title 5","Test Artist 5", 2000,2000,"2000", "Test ID 5")


                ));
    }

    public static List<CLArtwork> getMockMappedHarvardArt() {
        return List.of(
                new CLArtwork("HVD1", "Test Title 1", "Test Artist 1" , "1800", "1800", "Test URL 1", "Harvard"),
                new CLArtwork("HVD2", "Test Title 2", "Test Artist 2" , "1850", "1850", "Test URL 2", "Harvard"),
                new CLArtwork("HVD3", "Test Title 3", "Test Artist 3" , "1900", "1900", "Test URL 3", "Harvard"),
                new CLArtwork("HVD4", "Test Title 4", "Test Artist 4" , "1950", "1950", "Test URL 4", "Harvard"),
                new CLArtwork("HVD5", "Test Title 5", "Test Artist 5" , "2000", "2000", "Test URL 5", "Harvard")
        );
    }

    public static List<CLArtwork> getMockMappedChicagoArt() {
        return List.of(
                new CLArtwork("AIC1","Test Title 1","Test Artist 1", "1800", "1800", "Test ID 1", "Art Institute of Chicago"),
                new CLArtwork("AIC2","Test Title 2","Test Artist 2", "1850", "1850", "Test ID 2", "Art Institute of Chicago"),
                new CLArtwork("AIC3","Test Title 3","Test Artist 3", "1900", "1900", "Test ID 3", "Art Institute of Chicago"),
                new CLArtwork("AIC4","Test Title 4","Test Artist 4", "1950", "1950", "Test ID 4", "Art Institute of Chicago"),
                new CLArtwork("AIC5","Test Title 5","Test Artist 5", "2000", "2000", "Test ID 5", "Art Institute of Chicago")
        );
    }

    public static CLPage getMockCLPage() {
        CLPage.PageInfo pageInfo = new CLPage.PageInfo(5,1,5,1);
        List<CLArtwork> artworks = Stream.concat(
                getMockMappedChicagoArt().stream(),
                getMockMappedHarvardArt().stream()
        ).toList();
        return new CLPage(pageInfo, artworks);
    }
}
