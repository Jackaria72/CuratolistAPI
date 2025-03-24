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
                List.of(new HarvardPage.HarvardArt(1,"Test Title 1", List.of(new HarvardPage.Person("Test Artist 1","Artist")), "1800", "1800", "Medium", "Technique", "Classification", "Culture", "Dimensions","Test URL 1",0),
                        new HarvardPage.HarvardArt(2,"Test Title 2", List.of(new HarvardPage.Person("Test Artist 2","Artist")), "1850", "1850", "Medium", "Technique", "Classification", "Culture", "Dimensions","Test URL 2",0),
                        new HarvardPage.HarvardArt(3,"Test Title 3", List.of(new HarvardPage.Person("Test Artist 3","Artist")), "1900", "1900", "Medium", "Technique", "Classification", "Culture", "Dimensions","Test URL 3",0),
                        new HarvardPage.HarvardArt(4,"Test Title 4", List.of(new HarvardPage.Person("Test Artist 4","Artist")), "1950", "1950", "Medium", "Technique", "Classification", "Culture", "Dimensions","Test URL 4",0),
                        new HarvardPage.HarvardArt(5,"Test Title 5", List.of(new HarvardPage.Person("Test Artist 5","Artist")), "2000", "2000", "Medium", "Technique", "Classification", "Culture", "Dimensions","Test URL 5",0)
));
    }

    public static ChicagoPage getMockChicagoPage() {
        return new ChicagoPage(new ChicagoPage.ChicagoPageInfo(5,1),
                List.of(new ChicagoPage.ChicagoArt(1,"Test Title 1","Test Artist 1", 1800,1800,"1800", "Medium", List.of("Technique"), "Classification", "Origin", "Dimensions","Test ID 1", true),
                        new ChicagoPage.ChicagoArt(2,"Test Title 2","Test Artist 2", 1850,1850,"1850", "Medium", List.of("Technique"), "Classification", "Origin", "Dimensions","Test ID 2", true),
                        new ChicagoPage.ChicagoArt(3,"Test Title 3","Test Artist 3", 1900,1900,"1900", "Medium", List.of("Technique"), "Classification", "Origin", "Dimensions","Test ID 3", true),
                        new ChicagoPage.ChicagoArt(4,"Test Title 4","Test Artist 4", 1950,1950,"1950", "Medium", List.of("Technique"), "Classification", "Origin", "Dimensions","Test ID 4", true),
                        new ChicagoPage.ChicagoArt(5,"Test Title 5","Test Artist 5", 2000,2000,"2000", "Medium", List.of("Technique"), "Classification", "Origin", "Dimensions","Test ID 5", true)


                ));
    }

    public static List<CLArtwork> getMockMappedHarvardArt() {
        return List.of(
                new CLArtwork("1_HVD", "Test Title 1", "Test Artist 1" , "1800", "1800", "Medium","Technique","Classification","Culture","Dimensions","Test URL 1","Harvard"),
                new CLArtwork("2_HVD", "Test Title 2", "Test Artist 2" , "1850", "1850", "Medium","Technique","Classification","Culture","Dimensions","Test URL 2","Harvard"),
                new CLArtwork("3_HVD", "Test Title 3", "Test Artist 3" , "1900", "1900", "Medium","Technique","Classification","Culture","Dimensions","Test URL 3","Harvard"),
                new CLArtwork("4_HVD", "Test Title 4", "Test Artist 4" , "1950", "1950", "Medium","Technique","Classification","Culture","Dimensions","Test URL 4","Harvard"),
                new CLArtwork("5_HVD", "Test Title 5", "Test Artist 5" , "2000", "2000", "Medium","Technique","Classification","Culture","Dimensions","Test URL 5","Harvard")
        );
    }

    public static List<CLArtwork> getMockMappedChicagoArt() {
        return List.of(
                new CLArtwork("1_AIC","Test Title 1","Test Artist 1", "1800", "1800", "Medium", "Technique", "Classification","Origin","Dimensions", "https://www.artic.edu/iiif/2/Test ID 1/full/843,/0/default.jpg", "Art Institute of Chicago"),
                new CLArtwork("2_AIC","Test Title 2","Test Artist 2", "1850", "1850", "Medium", "Technique", "Classification","Origin","Dimensions", "https://www.artic.edu/iiif/2/Test ID 2/full/843,/0/default.jpg", "Art Institute of Chicago"),
                new CLArtwork("3_AIC","Test Title 3","Test Artist 3", "1900", "1900", "Medium", "Technique", "Classification","Origin","Dimensions", "https://www.artic.edu/iiif/2/Test ID 3/full/843,/0/default.jpg", "Art Institute of Chicago"),
                new CLArtwork("4_AIC","Test Title 4","Test Artist 4", "1950", "1950", "Medium", "Technique", "Classification","Origin","Dimensions", "https://www.artic.edu/iiif/2/Test ID 4/full/843,/0/default.jpg", "Art Institute of Chicago"),
                new CLArtwork("5_AIC","Test Title 5","Test Artist 5", "2000", "2000", "Medium", "Technique", "Classification","Origin","Dimensions", "https://www.artic.edu/iiif/2/Test ID 5/full/843,/0/default.jpg", "Art Institute of Chicago")
        );
    }

    public static CLPage getMockCLPage() {
        CLPage.PageInfo pageInfo = new CLPage.PageInfo(5,1,5,1, 10,2);
        List<CLArtwork> artworks = Stream.concat(
                getMockMappedChicagoArt().stream(),
                getMockMappedHarvardArt().stream()
        ).toList();
        return new CLPage(pageInfo, artworks);
    }

    public static CLPage getMockCLPageChicagoOnly() {
        return new CLPage(
                new CLPage.PageInfo(5,1,0,0, 5, 1),
                getMockMappedChicagoArt()
        );
    }

    public static CLPage getMockCLPageHarvardOnly() {
        return new CLPage(
                new CLPage.PageInfo(0,0,5,1, 5,1),
                getMockMappedHarvardArt()
        );
    }
}
