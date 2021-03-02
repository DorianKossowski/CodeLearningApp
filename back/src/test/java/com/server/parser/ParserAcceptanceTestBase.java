package com.server.parser;

import com.server.app.model.dto.VerificationResultDto;
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
    void shouldVerifyAll(@SuppressWarnings("unused") String caseName, String testCase) {
        AcceptanceTestCaseModel testCaseModel = AcceptanceTestCaseFetcher.fetchModel(testCase);
        VerificationResultDto resultDto = VERIFICATION_SERVICE.verify(testCaseModel.getTask(), testCaseModel.getInput());
        if (resultDto.getException() != null) {
            resultDto.getException().printStackTrace();
            assert false;
        }
        assert resultDto.getErrorMessage() == null;
        if (!equalsIgnoreNewlineStyle(resultDto.getOutput(), testCaseModel.getOutput())) {
            System.out.printf("Different output!\nExpected:\n%s\nActual:\n%s%n", testCaseModel.getOutput(), resultDto.getOutput());
            assert false;
        }
    }

    private static boolean equalsIgnoreNewlineStyle(String s1, String s2) {
        return s1 != null && s2 != null && normalizeLineEnds(s1).equals(normalizeLineEnds(s2));
    }

    private static String normalizeLineEnds(String s) {
        return s.replace("\r\n", "\n").replace('\r', '\n');
    }

    protected abstract String getPath();

    protected abstract Function<P, ParserRuleContext> getParsingRule();
}
