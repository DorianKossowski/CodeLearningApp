package com.server.parser.java.task;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaTaskGrammarHelperTest {

    @Test
    void shouldGetFromStringLiteral() {
        String literal = "\"A\\\"B\\\"\"";

        assertThat(JavaTaskGrammarHelper.getFromStringLiteral(literal)).isEqualTo("A\"B\"");
    }
}