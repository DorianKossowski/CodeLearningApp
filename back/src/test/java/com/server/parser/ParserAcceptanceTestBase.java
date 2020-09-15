package com.server.parser;

import com.server.app.service.TaskVerificationService;
import com.server.app.service.impl.TaskVerificationServiceImpl;
import com.server.parser.util.AcceptanceTestCaseFetcher;
import com.server.parser.util.AcceptanceTestCaseModel;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ParserAcceptanceTestBase<P extends Parser> {
    private static final TaskVerificationService VERIFICATION_SERVICE = new TaskVerificationServiceImpl();
    private final ParserTestHelper<P> helper;
    private final Map<String, String> testCases;

    protected ParserAcceptanceTestBase(Function<CodePointCharStream, Lexer> lexerConstructor,
                                       Function<TokenStream, P> parserConstructor) {
        helper = new ParserTestHelper<>(lexerConstructor, parserConstructor);
        testCases = ParserAcceptanceTestCasesProvider.getTestCases(getPath());
    }

    private Stream<Arguments> testCasesMethodSource() {
        return testCases.entrySet().stream()
                .map(testCaseEntrySet -> Arguments.of(testCaseEntrySet.getKey(), testCaseEntrySet.getValue()));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCasesMethodSource")
    void shouldParseAll(@SuppressWarnings("unused") String caseName, String testCase) {
        AcceptanceTestCaseModel testCaseModel = AcceptanceTestCaseFetcher.fetchModelJson(testCase);
        VERIFICATION_SERVICE.verify(testCaseModel.getInput(), testCaseModel.getTask());
    }

    protected abstract String getPath();

    protected abstract Function<P, ParserRuleContext> getParsingRule();
}
