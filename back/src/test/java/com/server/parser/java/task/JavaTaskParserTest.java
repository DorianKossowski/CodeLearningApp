package com.server.parser.java.task;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaTaskParserTest {
    private static final ParserTestHelper<JavaTaskParser> HELPER = new ParserTestHelper<>(JavaTaskLexer::new,
            JavaTaskParser::new);

    @ParameterizedTest
    @ValueSource(strings = {
            "method with name: \"x\"",
            "method with args: {\"x\", -}",
            "method with args: {-, \"x\"}, {\"x\", \"x\"}",
            "method with modifiers: {\"x\"}",
            "method with modifiers: {\"x\", \"x\"}",
            "method with result: \"x\""
    })
    void shouldParseMethodRule(String input) {
        HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "statement in method: \"m\"",
            "statement with text: \"t\"",
            "statement with resolved: \"t\""
    })
    void shouldParseStatementRule(String input) {
        HELPER.shouldParseToEof(input, JavaTaskParser::statementRule);
    }
}
