package com.artful.curatolist.service;

import com.artful.curatolist.TestUtilityMethods;
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
        ChicagoPage mockChicagoPage = TestUtilityMethods.getMockChicagoPage();
        HarvardPage mockHarvardPage = TestUtilityMethods.getMockHarvardPage();
        List<CLArtwork> chicagoMapped = TestUtilityMethods.getMockMappedChicagoArt();
        List<CLArtwork> harvardMapped = TestUtilityMethods.getMockMappedHarvardArt();

        when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.just(mockChicagoPage));
        when(harvardClient.getHarvardArtwork(1)).thenReturn(Mono.just(mockHarvardPage));
        when(chicagoMapper.mapChicagoArt(mockChicagoPage)).thenReturn(chicagoMapped);
        when(harvardMapper.mapHarvardArt(mockHarvardPage)).thenReturn(harvardMapped);

        Mono<CLPage> results = curatolistService.getArt(1);



        StepVerifier.create(results)
                .assertNext(clPage -> {
                    verify(harvardClient, times(1)).getHarvardArtwork(1);
                    verify(chicagoClient, times(1)).getChicagoArtwork(1);
                    verify(harvardMapper, times(1)).mapHarvardArt(mockHarvardPage);
                    verify(chicagoMapper, times(1)).mapChicagoArt(mockChicagoPage);
                    assertNotNull(clPage);
                    assertEquals(harvardMapped.size() + chicagoMapped.size(), clPage.artwork().size());
                    assertTrue(clPage.artwork().containsAll(chicagoMapped));
                    assertTrue(clPage.artwork().containsAll(harvardMapped));
                }).verifyComplete();
    }

    @Test
    void testGetArtReturnsPartialResultsWhenHarvardFails() {
        ChicagoPage mockChicagoPage = TestUtilityMethods.getMockChicagoPage();
        List<CLArtwork> chicagoMapped = TestUtilityMethods.getMockMappedChicagoArt();

        when(harvardClient.getHarvardArtwork(1)).thenReturn(Mono.error(new ExternalApiException("Harvard API Error")));
        when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.just(mockChicagoPage));
        when(chicagoMapper.mapChicagoArt(mockChicagoPage)).thenReturn(chicagoMapped);

        Mono<CLPage> result = curatolistService.getArt(1);

        StepVerifier.create(result)
                .assertNext(clPage -> {
                    assertFalse(clPage.artwork().isEmpty());
                    assertEquals(0, clPage.pageInfo().harvardTotal());
                    assertEquals(chicagoMapped.size(), clPage.artwork().size());
                }).verifyComplete();

    }

    @Test
    void testGetArtReturnsPartialResultsWhenChicagoFails() {
        HarvardPage mockHarvardPage = TestUtilityMethods.getMockHarvardPage();
        List<CLArtwork> harvardMapped = TestUtilityMethods.getMockMappedHarvardArt();

        when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.error(new ExternalApiException("Chicago API Error")));
        when(harvardClient.getHarvardArtwork(1)).thenReturn(Mono.just(mockHarvardPage));
        when(harvardMapper.mapHarvardArt(mockHarvardPage)).thenReturn(harvardMapped);

        Mono<CLPage> result = curatolistService.getArt(1);

        StepVerifier.create(result)
                .assertNext(clPage -> {
                    assertFalse(clPage.artwork().isEmpty());
                    assertEquals(0, clPage.pageInfo().chicagoTotal());
                    assertEquals(harvardMapped.size(), clPage.artwork().size());
                }).verifyComplete();

    }

    @Test
    void testGetArtThrowsExceptionWhenBothApisFail() {
        when(harvardClient.getHarvardArtwork(1)).thenReturn(Mono.error(new ExternalApiException("Harvard Error")));
        when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.error(new ExternalApiException("Chicago Error")));

        Mono<CLPage> result = curatolistService.getArt(1);

        StepVerifier.create(result)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }
}