package com.server.parser.java;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JavaGrammarHelperTest {

    @Test
    void shouldCreateMethodName() {
        List<String> input = Arrays.asList("A", "B");

        assertThat(JavaGrammarHelper.createMethodName(input)).isEqualTo("A.B");
    }
}