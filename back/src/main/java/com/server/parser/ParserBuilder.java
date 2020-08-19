package com.server.parser;

import com.server.parser.util.exception.PrintableParseExceptionErrorStrategy;
import org.antlr.v4.runtime.*;

import java.util.function.Function;

public class ParserBuilder {

    public static <P extends Parser> P build(String input, Function<CodePointCharStream, Lexer> lexerConstructor,
                                             Function<TokenStream, P> parserConstructor) {
        Lexer lexer = createLexer(input, lexerConstructor);
        TokenStream tokenStream = createTokenStream(lexer);
        return createParser(parserConstructor, tokenStream);
    }

    public static Lexer createLexer(String input, Function<CodePointCharStream, Lexer> lexerConstructor) {
        CodePointCharStream codePointCharStream = CharStreams.fromString(input);
        return lexerConstructor.apply(codePointCharStream);
    }

    public static CommonTokenStream createTokenStream(Lexer lexer) {
        return new CommonTokenStream(lexer);
    }

    public static <P extends Parser> P createParser(Function<TokenStream, P> parserConstructor,
                                                    TokenStream tokenStream) {
        P parser = parserConstructor.apply(tokenStream);
        parser.setErrorHandler(new PrintableParseExceptionErrorStrategy());
        return parser;
    }
}