package com.artful.curatolist.service;

import com.artful.curatolist.client.ChicagoClient;
import com.artful.curatolist.client.HarvardClient;
import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.mapper.ChicagoMapper;
import com.artful.curatolist.mapper.HarvardMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.ChicagoPage;
import com.artful.curatolist.model.HarvardPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuratolistServiceImplTest {
    @Mock
    private ChicagoClient chicagoClient;
    @Mock
    private ChicagoMapper chicagoMapper;
    @Mock
    private HarvardClient harvardClient;
    @Mock
    private HarvardMapper harvardMapper;
    @InjectMocks
    private CuratolistServiceImpl curatolistService;

    @Test
    void testGetArt() {
        ChicagoPage mockChicagoPage = new ChicagoPage(new ChicagoPage.ChicagoPageInfo(1,1),
                List.of(new ChicagoPage.ChicagoArt(1,"Test Art 1","Test Artist 1", 1800,1800,"1800", "Test ID 1")));
        HarvardPage mockHarvardPage = new HarvardPage(new HarvardPage.HarvardPageInfo(1,1),
                List.of(new HarvardPage.HarvardArt(1,"Test Art 1", List.of(new HarvardPage.Person("Test Artist 1")), "1800", "1800", "Test URL 1")));
        List<CLArtwork> chicagoMapped = List.of(new CLArtwork("AIC1","Test Title 1","Test Artist 1", "1800 - 1800", "1800", "Test ID 1", "Art Institute of Chicago"));
        List<CLArtwork> harvardMapped = List.of(new CLArtwork("HVD1", "Test Art 1", "Test Artist 1" , "1800", "1800", "Test URL 1", "Harvard"));

        when(chicagoClient.getChicagoArtwork(1,1)).thenReturn(Mono.just(mockChicagoPage));
        when(harvardClient.getHarvardArtwork(1,1)).thenReturn(Mono.just(mockHarvardPage));
        when(chicagoMapper.mapChicagoArt(mockChicagoPage)).thenReturn(chicagoMapped);
        when(harvardMapper.mapHarvardArt(mockHarvardPage)).thenReturn(harvardMapped);

        CLPage results = curatolistService.getArt(1,2).block();

        verify(harvardClient, times(1)).getHarvardArtwork(1, 1);
        verify(chicagoClient, times(1)).getChicagoArtwork(1, 1);
        verify(harvardMapper, times(1)).mapHarvardArt(mockHarvardPage);
        verify(chicagoMapper, times(1)).mapChicagoArt(mockChicagoPage);

        assertNotNull(results);
        assertEquals(2, results.artwork().size());
        assertTrue(results.artwork().containsAll(chicagoMapped));
        assertTrue(results.artwork().containsAll(harvardMapped));

    }

    @Test
    void testGetArtReturnsPartialResultsWhenHarvardFails() {
        ChicagoPage mockChicagoPage = new ChicagoPage(new ChicagoPage.ChicagoPageInfo(1,1),
                List.of(new ChicagoPage.ChicagoArt(1,"Test Art 1","Test Artist 1", 1800,1800,"1800", "Test ID 1")));
        List<CLArtwork> chicagoMapped = List.of(new CLArtwork("AIC1","Test Title 1","Test Artist 1", "1800 - 1800", "1800", "Test ID 1", "Art Institute of Chicago"));

        when(harvardClient.getHarvardArtwork(1,1)).thenReturn(Mono.error(new ExternalApiException("Harvard API Error")));
        when(chicagoClient.getChicagoArtwork(1,1)).thenReturn(Mono.just(mockChicagoPage));
        when(chicagoMapper.mapChicagoArt(mockChicagoPage)).thenReturn(chicagoMapped);

        Mono<CLPage> result = curatolistService.getArt(1,2);

        StepVerifier.create(result)
                .assertNext(clPage -> {
                    assertFalse(clPage.artwork().isEmpty());
                    assertEquals(0, clPage.pageInfo().harvardTotal());
                }).verifyComplete();

    }

    @Test
    void testGetArtReturnsPartialResultsWhenChicagoFails() {
        HarvardPage mockHarvardPage = new HarvardPage(new HarvardPage.HarvardPageInfo(1,1),
                List.of(new HarvardPage.HarvardArt(1,"Test Art 1", List.of(new HarvardPage.Person("Test Artist 1")), "1800", "1800", "Test URL 1")));
        List<CLArtwork> harvardMapped = List.of(new CLArtwork("HVD1", "Test Art 1", "Test Artist 1" , "1800", "1800", "Test URL 1", "Harvard"));


        when(chicagoClient.getChicagoArtwork(1,1)).thenReturn(Mono.error(new ExternalApiException("Chicago API Error")));
        when(harvardClient.getHarvardArtwork(1,1)).thenReturn(Mono.just(mockHarvardPage));
        when(harvardMapper.mapHarvardArt(mockHarvardPage)).thenReturn(harvardMapped);

        Mono<CLPage> result = curatolistService.getArt(1,2);

        StepVerifier.create(result)
                .assertNext(clPage -> {
                    assertFalse(clPage.artwork().isEmpty());
                    assertEquals(0, clPage.pageInfo().chicagoTotal());
                }).verifyComplete();

    }

    @Test
    void testGetArtThrowsExceptionWhenBothApisFail() {
        when(harvardClient.getHarvardArtwork(1,1)).thenReturn(Mono.error(new ExternalApiException("Harvard Error")));
        when(chicagoClient.getChicagoArtwork(1,1)).thenReturn(Mono.error(new ExternalApiException("Chicago Error")));

        Mono<CLPage> result = curatolistService.getArt(1,2);

        StepVerifier.create(result)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }
}