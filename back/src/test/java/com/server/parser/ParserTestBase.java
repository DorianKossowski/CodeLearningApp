package com.server.parser;

import org.antlr.v4.runtime.*;

import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ParserTestBase<P extends Parser> {
    private static final int EOF = -1;

    private final Function<CodePointCharStream, Lexer> lexerConstructor;
    private final Function<TokenStream, P> parserConstructor;

    protected ParserTestBase(Function<CodePointCharStream, Lexer> lexerConstructor, Function<TokenStream, P> parserConstructor) {
        this.lexerConstructor = Objects.requireNonNull(lexerConstructor, "lexerConstructor cannot be null");
        this.parserConstructor = Objects.requireNonNull(parserConstructor, "parserConstructor cannot be null");
    }

    protected <PRC extends ParserRuleContext> PRC shouldParseToEof(String input, Function<P, PRC> parsingRule) {
        Lexer lexer = ParserBuilder.createLexer(input, lexerConstructor);
        TokenStream tokenStream = ParserBuilder.createTokenStream(lexer);

        P parser = ParserBuilder.createParser(parserConstructor, tokenStream);
        PRC parsingResult = parsingRule.apply(parser);

        assertThat(tokenStream.LA(1))
                .overridingErrorMessage("Should be EOF, got: ", tokenStream.LT(1))
                .isEqualTo(EOF);

        return parsingResult;
    }
}