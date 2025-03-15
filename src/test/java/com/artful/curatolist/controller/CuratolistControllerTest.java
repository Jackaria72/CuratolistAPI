package com.artful.curatolist.controller;

import com.artful.curatolist.model.CLArtwork;
import com.artful.curatolist.model.CLPage;
import com.artful.curatolist.service.CuratolistServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;


@WebFluxTest
class CuratolistControllerTest {
    @MockBean
    private CuratolistServiceImpl curatolistService;
    @InjectMocks
    private CuratolistController curatolistController;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetArtwork() throws Exception {
        when(curatolistService.getArt(1)).thenReturn(Mono.just(getMockPage()));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/curatolist/api/v1")
                                .queryParam("page", "1")
                                .queryParam("limit", "4")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pageInfo").isNotEmpty()
                .jsonPath("$.artwork").isNotEmpty()
                .jsonPath("$.artwork[0].title").isEqualTo(getMockPage().artwork().getFirst().title())
                .jsonPath("$.artwork[1].title").isEqualTo(getMockPage().artwork().get(1).title())
                .jsonPath("$.artwork[2].title").isEqualTo(getMockPage().artwork().get(2).title())
                .jsonPath("$.artwork[3].title").isEqualTo(getMockPage().artwork().getLast().title());

    }

    private static CLPage getMockPage() {
        CLPage.PageInfo pageInfo = new CLPage.PageInfo(2,1,2,1);
        List<CLArtwork> artworks = List.of(
        new CLArtwork("AIC1", "Test Art 1", "Test Artist 1" , "1800 - 1800", "1800", "Test ID 1", "Art Institute of Chicago"),
        new CLArtwork("AIC2", "Test Art 2", "Test Artist 2" , "1900 - 1900", "1900", "Test ID 2", "Art Institute of Chicago"),
        new CLArtwork("HVD1", "Test Art 1", "Test Artist 1" , "1800", "1800", "Test URL 1", "Harvard"),
        new CLArtwork("HVD2", "Test Art 2", "Test Artist 2" , "1900", "1900", "Test URL 2", "Harvard")
        );
        return new CLPage(pageInfo, artworks);
    }
}