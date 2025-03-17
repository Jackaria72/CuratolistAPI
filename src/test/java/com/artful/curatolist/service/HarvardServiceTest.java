package com.artful.curatolist.service;

import com.artful.curatolist.TestUtilityMethods;
import com.artful.curatolist.builder.HarvardUriBuilder;
import com.artful.curatolist.client.HarvardClient;
import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.mapper.HarvardMapper;
import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HarvardServiceTest {
    @Mock
    private HarvardClient harvardClient;
    @Mock
    private HarvardMapper harvardMapper;
    @Mock
    private HarvardUriBuilder harvardUriBuilder;
    @InjectMocks
    private HarvardService harvardService;

    @Test
    void testHarvardServiceGetArt() {
        HarvardPage mockHarvardPage = TestUtilityMethods.getMockHarvardPage();
        List<CLArtwork> harvardMapped = TestUtilityMethods.getMockMappedHarvardArt();

        when(harvardUriBuilder.buildHarvardUri(anyInt(), any(), any())).thenReturn("test-uri");
        when(harvardClient.getHarvardArtwork("test-uri")).thenReturn(Mono.just(mockHarvardPage));
        when(harvardMapper.mapHarvardArt(mockHarvardPage)).thenReturn(harvardMapped);

        Mono<CLPage> results = harvardService.getArt(1, null, null);



        StepVerifier.create(results)
                .assertNext(clPage -> {
                    verify(harvardClient, times(1)).getHarvardArtwork(anyString());
                    verify(harvardMapper, times(1)).mapHarvardArt(mockHarvardPage);
                    assertNotNull(clPage);
                    assertEquals(harvardMapped.size(), clPage.artwork().size());
                    assertTrue(clPage.artwork().containsAll(harvardMapped));
                }).verifyComplete();
    }
    @Test
    void testGetArtThrowsExternalApiExceptionWhenApiFails() {
        when(harvardUriBuilder.buildHarvardUri(anyInt(), any(), any())).thenReturn("test-uri");
        when(harvardClient.getHarvardArtwork("test-uri")).thenReturn(Mono.error(new ExternalApiException("Harvard Error")));

        Mono<CLPage> result = harvardService.getArt(1, null, null);

        StepVerifier.create(result)
                .expectError(ExternalApiException.class)
                .verify();
    }
    @Test
    void testGetArtThrowsResourcesNotFoundExceptionWhenApiFailsWhenResourcesNotFound() {
        when(harvardUriBuilder.buildHarvardUri(anyInt(), any(), any())).thenReturn("test-uri");
        when(harvardClient.getHarvardArtwork("test-uri")).thenReturn(Mono.error(new ResourcesNotFoundException("Harvard Error")));

        Mono<CLPage> result = harvardService.getArt(1, null, null);

        StepVerifier.create(result)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }
}