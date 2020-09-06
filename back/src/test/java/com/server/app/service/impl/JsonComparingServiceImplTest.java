package com.server.app.service.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonComparingServiceImplTest {
    private final JsonComparingServiceImpl service = new JsonComparingServiceImpl();

    @Test
    void shouldBeEqual() {
        String json1 = "{\n" +
                "  \"A\" : {\n" +
                "    \"B\" : {\n" +
                "      \"C\" : \"value\"\n" +
                "    }";
        String json2 = "{\"A\":{\"B\":{\"C\":\"value\"}";

        assertThat(service.areEqual(json1, json2)).isTrue();
    }

    @Test
    void shouldNotBeEqualWhenWhiteSpaceInsideQuotes() {
        String json1 = "{\n" +
                "  \"A\" : \"value1 value2\"\n" +
                "}";
        String json2 = "{\"A\":\"value1value2\"}";

        assertThat(service.areEqual(json1, json2)).isFalse();
    }

    @Test
    void shouldCorrectlyRemoveWhiteSpacesWhenEscapedQuotes() {
        String input = "\"A\" : \"value1 \\\"value2 value3\\\"\"";
        String output = "\"A\":\"value1 \\\"value2 value3\\\"\"";

        assertThat(service.removeWhiteSpaces(input)).isEqualTo(output);
    }
}