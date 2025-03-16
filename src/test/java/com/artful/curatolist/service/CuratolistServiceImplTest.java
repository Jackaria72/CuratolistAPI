package com.artful.curatolist.service;

import com.artful.curatolist.TestUtilityMethods;
import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.CLPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuratolistServiceImplTest {
    @Mock
    private ChicagoService chicagoService;
    @Mock
    private HarvardService harvardService;
    @InjectMocks
    private CuratolistServiceImpl curatolistService;

    @Test
    void testGetArtGetsAllArtWithBothParam() {
        CLPage chicagoExpected = TestUtilityMethods.getMockCLPageChicagoOnly();
        CLPage harvardExpected = TestUtilityMethods.getMockCLPageHarvardOnly();

        when(chicagoService.getArt(1)).thenReturn(Mono.just(chicagoExpected));
        when(harvardService.getArt(1)).thenReturn(Mono.just(harvardExpected));

        Mono<CLPage> results = curatolistService.getArt(1, "both");

        StepVerifier.create(results)
                .assertNext(clPage -> {
                    verify(harvardService, times(1)).getArt(1);
                    verify(chicagoService, times(1)).getArt(1);
                    assertNotNull(clPage);
                    assertEquals(harvardExpected.artwork().size() + chicagoExpected.artwork().size(), clPage.artwork().size());
                    assertTrue(clPage.artwork().containsAll(chicagoExpected.artwork()));
                    assertTrue(clPage.artwork().containsAll(harvardExpected.artwork()));
                }).verifyComplete();
    }

    @Test
    void testGetArtGetsOnlyHarvardArtWithHarvardParam() {
        CLPage harvardExpected = TestUtilityMethods.getMockCLPageHarvardOnly();

        when(harvardService.getArt(1)).thenReturn(Mono.just(harvardExpected));

        Mono<CLPage> results = curatolistService.getArt(1, "harvard");

        StepVerifier.create(results)
                .assertNext(clPage -> {
                    verify(harvardService, times(1)).getArt(1);
                    verify(chicagoService, times(0)).getArt(anyInt());
                    assertNotNull(clPage);
                    assertEquals(harvardExpected.artwork().size(), clPage.artwork().size());
                    assertTrue(clPage.artwork().containsAll(harvardExpected.artwork()));
                }).verifyComplete();
    }

    @Test
    void testGetArtGetsOnlyChicagoArtWithChicagoParam() {
        CLPage chicagoExpected = TestUtilityMethods.getMockCLPageChicagoOnly();

        when(chicagoService.getArt(1)).thenReturn(Mono.just(chicagoExpected));

        Mono<CLPage> results = curatolistService.getArt(1, "chicago");

        StepVerifier.create(results)
                .assertNext(clPage -> {
                    verify(chicagoService, times(1)).getArt(1);
                    verify(harvardService, times(0)).getArt(anyInt());
                    assertNotNull(clPage);
                    assertEquals(chicagoExpected.artwork().size(), clPage.artwork().size());
                    assertTrue(clPage.artwork().containsAll(chicagoExpected.artwork()));
                }).verifyComplete();
    }

    @Test
    void testGetArtReturnsPartialResultsWhenHarvardFails() {
        CLPage expected = TestUtilityMethods.getMockCLPageChicagoOnly();

        when(harvardService.getArt(1)).thenReturn(Mono.error(new ExternalApiException("Harvard API Error")));
        when(chicagoService.getArt(1)).thenReturn(Mono.just(expected));

        Mono<CLPage> result = curatolistService.getArt(1, "both");

        StepVerifier.create(result)
                .assertNext(clPage -> {
                    assertFalse(clPage.artwork().isEmpty());
                    assertEquals(0, clPage.pageInfo().harvardTotal());
                    assertEquals(expected.artwork().size(), clPage.artwork().size());
                }).verifyComplete();
    }

    @Test
    void testGetArtReturnsPartialResultsWhenChicagoFails() {
        CLPage expected = TestUtilityMethods.getMockCLPageHarvardOnly();

        when(chicagoService.getArt(1)).thenReturn(Mono.error(new ExternalApiException("Chicago API Error")));
        when(harvardService.getArt(1)).thenReturn(Mono.just(expected));

        Mono<CLPage> result = curatolistService.getArt(1, "both");

        StepVerifier.create(result)
                .assertNext(clPage -> {
                    assertFalse(clPage.artwork().isEmpty());
                    assertEquals(0, clPage.pageInfo().chicagoTotal());
                    assertEquals(expected.artwork().size(), clPage.artwork().size());
                }).verifyComplete();
    }

    @Test
    void testGetArtThrowsExceptionWhenBothApisFail() {
        when(harvardService.getArt(1)).thenReturn(Mono.error(new ExternalApiException("Harvard Error")));
        when(chicagoService.getArt(1)).thenReturn(Mono.error(new ExternalApiException("Chicago Error")));

        Mono<CLPage> result = curatolistService.getArt(1, "both");

        StepVerifier.create(result)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }

    @Test
    void testGetArtThrowsExceptionWhenHarvardFailsWithSingleSourceParam() {
        when(harvardService.getArt(1)).thenReturn(Mono.error(new ExternalApiException("Harvard Error")));

        Mono<CLPage> result = curatolistService.getArt(1, "harvard");

        StepVerifier.create(result)
                .expectError(ExternalApiException.class)
                .verify();
    }

    @Test
    void testGetArtThrowsExceptionWhenChicagoFailsWithSingleSourceParam() {
        when(chicagoService.getArt(1)).thenReturn(Mono.error(new ExternalApiException("Chicago Error")));

        Mono<CLPage> result = curatolistService.getArt(1, "chicago");

        StepVerifier.create(result)
                .expectError(ExternalApiException.class)
                .verify();
    }
}