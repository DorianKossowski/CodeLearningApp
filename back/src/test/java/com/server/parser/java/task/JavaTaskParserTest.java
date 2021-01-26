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
            "class with modifiers: { \"x\", \"y\" }",
            "class with name: \"x\"",
            "class with constructor with name: \"t\" with args: { \"int\", - }",
            "class with field with modifiers: {\"public\", \"static\"} with type: \"int\" with name: \"t\" " +
                    "with value: \"1\" log info: \"t\"",
            "class log info: \"t\"",
    })
    void shouldParseClassRule(String input) {
        HELPER.shouldParseToEof(input, JavaTaskParser::classRule);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "method with name: \"x\"",
            "method with args: ",
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
            "statement with resolved: \"t\"",
            "statement log info: \"t\"",
            "statement with if: \"t\"",
            "statement with else if: \"t\"",
            "statement is in else",
            "statement with switch expression: \"t\"",
            "statement with switch label: \"t\"",
            "statement with for iteration: \"t\"",
            "statement with while iteration: \"t\"",
            "statement with do while iteration: \"t\""
    })
    void shouldParseStatementRule(String input) {
        HELPER.shouldParseToEof(input, JavaTaskParser::statementRule);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "variable with text: \"v\"",
            "variable log info: \"v\""
    })
    void shouldParseVariableRule(String input) {
        HELPER.shouldParseToEof(input, JavaTaskParser::variableRule);
    }
}
