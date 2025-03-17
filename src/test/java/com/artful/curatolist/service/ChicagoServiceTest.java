package com.artful.curatolist.service;

import com.artful.curatolist.TestUtilityMethods;
import com.artful.curatolist.client.ChicagoClient;
import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.mapper.ChicagoMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.model.ChicagoPage;
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
class ChicagoServiceTest {
    @Mock
    private ChicagoClient chicagoClient;
    @Mock
    private ChicagoMapper chicagoMapper;
    @InjectMocks
    private ChicagoService chicagoService;

    @Test
    void testChicagoServiceGetArt() {
        ChicagoPage mockChicagoPage = TestUtilityMethods.getMockChicagoPage();
        List<CLArtwork> chicagoMapped = TestUtilityMethods.getMockMappedChicagoArt();

        when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.just(mockChicagoPage));
        when(chicagoMapper.mapChicagoArt(mockChicagoPage)).thenReturn(chicagoMapped);

        Mono<CLPage> results = chicagoService.getArt(1);



        StepVerifier.create(results)
                .assertNext(clPage -> {
                    verify(chicagoClient, times(1)).getChicagoArtwork(1);
                    verify(chicagoMapper, times(1)).mapChicagoArt(mockChicagoPage);
                    assertNotNull(clPage);
                    assertEquals(chicagoMapped.size(), clPage.artwork().size());
                    assertTrue(clPage.artwork().containsAll(chicagoMapped));
                }).verifyComplete();
    }
    @Test
    void testGetArtThrowsExternalApiExceptionWhenApiFails() {
                when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.error(new ExternalApiException("Chicago Error")));

        Mono<CLPage> result = chicagoService.getArt(1);

        StepVerifier.create(result)
                .expectError(ExternalApiException.class)
                .verify();
    }
    @Test
    void testGetArtThrowsResourcesNotFoundExceptionWhenApiFailsWhenResourcesNotFound() {
        when(chicagoClient.getChicagoArtwork(1)).thenReturn(Mono.error(new ResourcesNotFoundException("Chicago Error")));

        Mono<CLPage> result = chicagoService.getArt(1);

        StepVerifier.create(result)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }
}