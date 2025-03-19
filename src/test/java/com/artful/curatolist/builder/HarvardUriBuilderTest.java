package com.artful.curatolist.builder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HarvardUriBuilderTest {

    private final String TEST_API_KEY = "test-api-key";
    private final HarvardUriBuilder uriBuilder = new HarvardUriBuilder(TEST_API_KEY);

    @Test
    void buildHarvardUri() {
        String expected1 = "/object?apikey="+TEST_API_KEY+"&fields=id,title,people,dated,period,medium,dimensions,classification,culture,technique,primaryimageurl&page=1&size=100&q=imagepermissionlevel:0&sort=id";
        String expected2 = "/object?apikey="+TEST_API_KEY+"&fields=id,title,people,dated,period,medium,dimensions,classification,culture,technique,primaryimageurl&page=7&size=100&q=imagepermissionlevel:0&keyword=cat&sort=classification.exact";

        String result1 = uriBuilder.buildHarvardUri(1, null, null, null);
        String result2 = uriBuilder.buildHarvardUri(7, "cat", "classification", null);

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }
}