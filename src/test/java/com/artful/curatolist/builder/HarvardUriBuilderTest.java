package com.artful.curatolist.builder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HarvardUriBuilderTest {

    private final HarvardUriBuilder uriBuilder = new HarvardUriBuilder();

    @Test
    void buildHarvardUri() {
        String expected1 = "&page=1&q=imagepermissionlevel:0&sort=id";
        String expected2 = "&page=7&q=imagepermissionlevel:0&keyword=cat&sort=classification.exact";

        String result1 = uriBuilder.buildHarvardUri(1, null, null, null);
        String result2 = uriBuilder.buildHarvardUri(7, "cat", "classification", null);

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }
}