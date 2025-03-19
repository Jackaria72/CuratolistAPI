package com.artful.curatolist.controller;

import com.artful.curatolist.TestUtilityMethods;
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

import static org.mockito.ArgumentMatchers.*;
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
        when(curatolistService.getArt(anyInt(), anyString(), any(), any(), any())).thenReturn(Mono.just(TestUtilityMethods.getMockCLPage()));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/curatolist/api/v1/art")
                                .queryParam("page", "1")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.pageInfo").isNotEmpty()
                .jsonPath("$.artwork").isNotEmpty();

        for (int i = 0; i < TestUtilityMethods.getMockCLPage().artwork().size(); i++) {
            webTestClient.get()
                    .uri(uriBuilder ->
                            uriBuilder.path("/curatolist/api/v1/art")
                                    .queryParam("page", "1")
                                    .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.artwork[" + i + "].id")
                    .isEqualTo(TestUtilityMethods.getMockCLPage().artwork().get(i).id())
                    .jsonPath("$.artwork[" + i + "].title")
                    .isEqualTo(TestUtilityMethods.getMockCLPage().artwork().get(i).title())
                    .jsonPath("$.artwork[" + i + "].artist")
                    .isEqualTo(TestUtilityMethods.getMockCLPage().artwork().get(i).artist())
                    .jsonPath("$.artwork[" + i + "].date")
                    .isEqualTo(TestUtilityMethods.getMockCLPage().artwork().get(i).date())
                    .jsonPath("$.artwork[" + i + "].imageUrl")
                    .isEqualTo(TestUtilityMethods.getMockCLPage().artwork().get(i).imageUrl());
        }
    }

}