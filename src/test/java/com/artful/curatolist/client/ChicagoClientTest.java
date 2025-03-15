package com.artful.curatolist.client;

import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.ChicagoPage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ChicagoClientTest {

    private static MockWebServer mockWebServer;
    private static ChicagoClient testChicagoClient;
    private static String mockResponse;

    @BeforeAll
    static void setUp() {
        mockWebServer  = new MockWebServer();
        WebClient.Builder mockWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString());
        testChicagoClient = new ChicagoClient(mockWebClient);
        mockResponse = "{" +
                "\"pagination\": {" +
                "\"total\": 60076," +
                "\"total_pages\": 60076" +
                "}," +
                "\"data\": [" +
                "{" +
                "\"id\": 1," +
                "\"title\": \"Test Title\"," +
                "\"date_display\": \"1800\"," +
                "\"artist_display\": \"Test Artist\"," +
                "\"is_public_domain\": true," +
                "\"image_id\": \"Test-image-id\"" +
                "}]" +
                "}";
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.close();
    }

    @Test
    void testGetChicagoArtwork() {
        mockWebServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json")
                        .setBody(mockResponse)
        );
        ChicagoPage results = testChicagoClient.getChicagoArtwork(1).block();

        assertNotNull(results);
        assertNotNull(results.data());
        assertEquals(1, results.data().size());
    }

    @Test
    void testChicagoThrowResourcesNotFoundWhenApiReturns404() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        Mono<ChicagoPage> results = testChicagoClient.getChicagoArtwork(1);

        StepVerifier.create(results)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }
    @Test
    void testChicagoThrowExternalApiWhenApiReturnsError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        Mono<ChicagoPage> results = testChicagoClient.getChicagoArtwork(1);

        StepVerifier.create(results)
                .expectError(ExternalApiException.class)
                .verify();
    }
}