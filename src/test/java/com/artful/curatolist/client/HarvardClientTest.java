package com.artful.curatolist.client;

import com.artful.curatolist.controller.exception.ExternalApiException;
import com.artful.curatolist.controller.exception.ResourcesNotFoundException;
import com.artful.curatolist.model.HarvardPage;
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

class HarvardClientTest {

    private static MockWebServer mockWebServer;
    private static HarvardClient testHarvardClient;
    private static String mockResponse;

    @BeforeAll
    static void setUp() {
        mockWebServer = new MockWebServer();
        WebClient.Builder mockWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString());
        testHarvardClient = new HarvardClient(mockWebClient, "testApiKey");
        mockResponse = "{" +
                "\"info\" : {" +
                "\"totalrecords\": 183815," +
                "\"pages\": 18382" +
                "}," +
                "\"records\": [" +
                "{" +
                "\"id\": 1," +
                "\"title\": \"Test Title\"," +
                "\"primaryimageurl\": \"test url\"," +
                "\"people\": [{" +
                "\"name\": \"Test Artist\"" +
                "}]," +
                "\"dated\": \"1858 - 1859\"," +
                "\"description\": \"19th century\"" +
                "}]" +
                "}";
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.close();
    }

    @Test
    void testGetHarvardArtwork() {
        mockWebServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json")
                        .setBody(mockResponse)
        );
        Mono<HarvardPage> results = testHarvardClient.getHarvardArtwork("test-uri");

        StepVerifier.create(results)
                .assertNext(harvardPage -> {
                    assertNotNull(harvardPage);
                    assertNotNull(harvardPage.records());
                    assertEquals(1, harvardPage.records().size());
                })
                .verifyComplete();    }

    @Test
    void testHarvardThrowResourcesNotFoundWhenApiReturns404() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        Mono<HarvardPage> results = testHarvardClient.getHarvardArtwork("test-uri");

        StepVerifier.create(results)
                .expectError(ResourcesNotFoundException.class)
                .verify();
    }
    @Test
    void testHarvardThrowExternalApiWhenApiReturnsError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        Mono<HarvardPage> results = testHarvardClient.getHarvardArtwork("test-uri");

        StepVerifier.create(results)
                .expectError(ExternalApiException.class)
                .verify();
    }
}