package com.server.parser;

import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ParserAcceptanceTestBase<P extends Parser> extends ParserTestBase<P> {
    private final Map<String, String> testCases;

    protected ParserAcceptanceTestBase(Function<CodePointCharStream, Lexer> lexerConstructor,
                                       Function<TokenStream, P> parserConstructor) {
        super(lexerConstructor, parserConstructor);
        testCases = ParserAcceptanceTestCasesProvider.getTestCases(getPath());
    }

    private Stream<Arguments> testCasesMethodSource() {
        return testCases.entrySet().stream()
                .map(testCaseEntrySet -> Arguments.of(testCaseEntrySet.getKey(), testCaseEntrySet.getValue()));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCasesMethodSource")
    void shouldParseAll(String caseName, String testCase) {
        shouldParseToEof(testCase, getParsingRule());
    }

    protected abstract String getPath();

    protected abstract Function<P, ParserRuleContext> getParsingRule();
}
