package com.artful.curatolist.builder;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChicagoUriBuilderTest {

    private final ChicagoUriBuilder uriBuilder = new ChicagoUriBuilder();

    @Test
    void testBuildChicagoUriBuildsCorrectUri() {
        String expected1 = "&page=1&query[bool][must][term][is_public_domain]=true&sort=id";
        String expected2 = "&page=7&query[bool][must][term][is_public_domain]=true&q=cat&sort=classification_title.keyword";

        String results1 = uriBuilder.buildChicagoUri(1, null, null, null);
        String results2 = uriBuilder.buildChicagoUri(7, "cat", "classification", null);

        assertEquals(expected1,results1);
        assertEquals(expected2,results2);
    }
}